package net.fortuneblack05.typerando.Client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fortuneblack05.typerando.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Random;

public class TypePickerOverlay {
    // Expose these so our Mixin can check them later
    public static boolean isSpinning = false;
    public static boolean isSummary = false;

    // Spin State
    private static Types finalType = Types.NORMAL;
    private static int totalSpinTicks = 0;
    private static int spinTick = 0;
    private static Types displayType = Types.NORMAL;
    private static final Random rng = new Random();

    // Summary State
    private static List<String> summaryNames;
    private static List<Integer> summaryTypeIds;
    private static int summaryTick = 0;

    public static void startSpin(Types type, int ticks) {
        finalType = type;
        totalSpinTicks = Math.max(20, ticks);
        spinTick = 0;
        isSpinning = true;
        isSummary = false;
    }

    public static void startSummary(List<String> names, List<Integer> ids) {
        summaryNames = names;
        summaryTypeIds = ids;
        summaryTick = 0;
        isSummary = true;
        isSpinning = false;
    }

    public static void register() {
        // 1. Background Tick Logic
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (isSpinning) {
                spinTick++;
                double progress = Math.min(1.0, (double) spinTick / totalSpinTicks);
                int interval = Math.max(1, (int) (2 + (progress * progress) * 10));

                if (spinTick < totalSpinTicks) {
                    if (spinTick % interval == 0) {
                        displayType = Types.fromId(1 + rng.nextInt(18)).orElse(Types.NORMAL);
                        if (client.getSoundManager() != null) {
                            client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0f));
                        }
                    }
                } else {
                    displayType = finalType;
                    if (spinTick == totalSpinTicks && client.getSoundManager() != null) {
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 1.0f));
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), 1.0f));
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), 1.0f));
                    }
                    if (spinTick > totalSpinTicks + 40) {
                        isSpinning = false; // Turn off the overlay
                    }
                }
            }

            if (isSummary) {
                summaryTick++;
                if (summaryTick > 200) {
                    isSummary = false;
                }
            }
        });

        // 2. Rendering on the screen (allowing free movement)
        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.textRenderer == null) return;

            int cx = ctx.getScaledWindowWidth() / 2;
            int cy = ctx.getScaledWindowHeight() / 2;

            if (isSpinning) {
                Text top = (spinTick < totalSpinTicks) ? Text.literal("ROLLING...") : Text.literal("YOUR TYPE:");
                ctx.drawCenteredTextWithShadow(client.textRenderer, top, cx, cy - 30, 0xFFFFFF);

                ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal(displayType.displayName.toUpperCase()), cx, cy - 5, 0xFFFFFF);

                ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal("#" + displayType.id), cx, cy + 15, 0xAAAAAA);
            }

            if (isSummary) {
                int x = 10;
                int y = 10;
                ctx.drawTextWithShadow(client.textRenderer, Text.literal("TYPE RANDO RESULTS"), x, y, 0xFFFFFF);
                y += 14;

                int maxLines = Math.min(summaryNames.size(), 18);
                for (int i = 0; i < maxLines; i++) {
                    String player = summaryNames.get(i);
                    int id = summaryTypeIds.get(i);
                    Types type = Types.fromId(id).orElse(Types.NORMAL);

                    ctx.drawTextWithShadow(client.textRenderer,
                            Text.literal(player + " -> " + type.displayName + " (#" + id + ")"),
                            x, y, 0xDDDDDD);
                    y += 12;
                }
            }
        });
    }
}