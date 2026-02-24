package net.fortuneblack05.typerando.mixin;

import net.fortuneblack05.typerando.TypePicker;
import net.fortuneblack05.typerando.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    // Notice we inject at "RETURN" here instead of "HEAD"
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void appendLogoToDisplayName(CallbackInfoReturnable<Text> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.getWorld().isClient) {
            // --- CLIENT SIDE (Floating Nametags) ---
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getNetworkHandler() != null) {
                PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
                if (entry != null && entry.getDisplayName() != null) {
                    // Copy the TAB name directly
                    cir.setReturnValue(entry.getDisplayName());
                }
            }
        } else {
            // --- SERVER SIDE (Chat Messages & Death Messages) ---
            Optional<Types> role = TypePicker.MANAGER.getRole(player.getUuid());
            if (role.isPresent()) {
                // Grab whatever Minecraft generated, copy it, and stick the logo on the end
                Text originalName = cir.getReturnValue();
                Text newName = originalName.copy()
                        .append(Text.literal(" " + role.get().tabIcon).setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                cir.setReturnValue(newName);
            }
        }
    }
}