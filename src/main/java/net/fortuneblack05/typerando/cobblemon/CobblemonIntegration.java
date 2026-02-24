package net.fortuneblack05.typerando.cobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.fortuneblack05.typerando.TypePicker;
import net.fortuneblack05.typerando.Types;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class CobblemonIntegration {

    public static void register() {

        // 1. Prevent throwing out the Pokemon into the world with 'R'
        CobblemonEvents.POKEMON_SENT_PRE.subscribe(Priority.NORMAL, event -> {

            Pokemon pokemon = event.getPokemon();

            // Grab the player from the Pokemon object itself
            ServerPlayerEntity player = pokemon.getOwnerPlayer();

            if (player != null) {
                Optional<Types> assigned = TypePicker.MANAGER.getRole(player.getUuid());
                if (assigned.isPresent()) {
                    if (!isMatchingType(pokemon, assigned.get())) {

                        event.cancel(); // Stops the Pokeball from opening
                        player.sendMessage(Text.literal("§cYou are a " + assigned.get().displayName + " trainer! You cannot send out " + pokemon.getSpecies().getName() + "."), true);
                    }
                }
            }
            return Unit.INSTANCE;
        });

        // 2. Prevent entering battles (Tournaments, Wild, Trainer) with invalid Pokemon
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, event -> {

            for (ServerPlayerEntity player : event.getBattle().getPlayers()) {
                Optional<Types> assigned = TypePicker.MANAGER.getRole(player.getUuid());

                if (assigned.isPresent()) {

                    boolean hasInvalid = false;
                    for (Pokemon partyMon : Cobblemon.INSTANCE.getStorage().getParty(player)) {
                        if (partyMon != null) {

                            // Check if the species name implies it is an egg to prevent the isEgg() crash
                            boolean isEgg = partyMon.getSpecies().getName().toLowerCase().contains("egg");

                            if (!isEgg && !isMatchingType(partyMon, assigned.get())) {
                                hasInvalid = true;
                                break;
                            }
                        }
                    }

                    if (hasInvalid) {
                        event.cancel(); // Kicks everyone out of the battle instantly
                        player.sendMessage(Text.literal("§cBattle Cancelled! You have a non-" + assigned.get().displayName + " type Pokemon in your party. Please deposit it in your PC!"), false);
                    }
                }
            }
            return Unit.INSTANCE;
        });
    }

    // Helper method to safely compare strings like "cobblemon:fire" with "Fire"
    private static boolean isMatchingType(Pokemon pokemon, Types assignedType) {
        String allowed = assignedType.displayName.toLowerCase();

        String type1 = pokemon.getPrimaryType().getName().toLowerCase();
        String type2 = pokemon.getSecondaryType() != null ? pokemon.getSecondaryType().getName().toLowerCase() : "";

        return type1.contains(allowed) || type2.contains(allowed);
    }
}