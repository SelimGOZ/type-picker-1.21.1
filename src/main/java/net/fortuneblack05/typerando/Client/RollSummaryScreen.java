package net.fortuneblack05.typerando.Client;

import net.fortuneblack05.typerando.Types;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class RollSummaryScreen extends Screen {
    private final List<String> names;
    private final List<Integer> typeIds;

    private int tick = 0;

    public RollSummaryScreen(List<String> names, List<Integer> typeIds) {
        super(Text.literal("TypeRando Results"));
        this.names = names;
        this.typeIds = typeIds;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Leave this empty!
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void tick() {
        tick++;
        if (tick > 200) { // ~10 seconds
            MinecraftClient.getInstance().setScreen(null);
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int x = 10;
        int y = 10;

        ctx.drawTextWithShadow(textRenderer, Text.literal("TYPE RANDO RESULTS"), x, y, 0xFFFFFF);
        y += 14;

        int maxLines = Math.min(names.size(), 18);
        for (int i = 0; i < maxLines; i++) {
            String player = names.get(i);
            int id = typeIds.get(i);
            Types type = Types.fromId(id).orElse(Types.NORMAL);

            ctx.drawTextWithShadow(textRenderer,
                    Text.literal(player + " -> " + type.displayName + " (#" + id + ")"),
                    x, y, 0xDDDDDD);
            y += 12;
        }

        super.render(ctx, mouseX, mouseY, delta);
    }
}