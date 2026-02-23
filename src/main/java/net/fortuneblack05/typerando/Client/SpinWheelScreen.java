package net.fortuneblack05.typerando.Client;

import net.fortuneblack05.typerando.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.Random;

public class SpinWheelScreen extends Screen {
    private final Types finalType;
    private final int totalSpinTicks;

    private int tick = 0;
    private Types displayType = Types.NORMAL;
    private final Random rng = new Random();

    public SpinWheelScreen(Types finalType, int totalSpinTicks) {
        super(Text.literal("TypeRando"));
        this.finalType = finalType;
        this.totalSpinTicks = Math.max(20, totalSpinTicks);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Leave this empty!
    }

    @Override
    public void tick() {
        tick++;

        double progress = (double) tick / (double) totalSpinTicks;
        progress = Math.min(1.0, Math.max(0.0, progress));

        int interval = (int) (2 + (progress * progress) * 10);
        interval = Math.max(1, interval);

        if (tick < totalSpinTicks) {
            if (tick % interval == 0) {
                int id = 1 + rng.nextInt(18);
                displayType = Types.fromId(id).orElse(Types.NORMAL);

                if (client != null) {
                    client.getSoundManager().play(
                            PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0f)
                    );
                }
            }
        } else {
            displayType = finalType;

            if (tick == totalSpinTicks && client != null) {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 1.0f));
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), 1.0f));
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), 1.0f));
            }

            if (tick > totalSpinTicks + 40) {
                MinecraftClient.getInstance().setScreen(null);
            }
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // no background = transparent overlay
        int cx = this.width / 2;
        int cy = this.height / 2;

        Text top = (tick < totalSpinTicks) ? Text.literal("ROLLING...") : Text.literal("YOUR TYPE:");
        ctx.drawCenteredTextWithShadow(this.textRenderer, top, cx, cy - 30, 0xFFFFFF);

        ctx.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal(displayType.displayName.toUpperCase()),
                cx, cy - 5, 0xFFFFFF);

        ctx.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("#" + displayType.id),
                cx, cy + 15, 0xAAAAAA);

        super.render(ctx, mouseX, mouseY, delta);
    }
}