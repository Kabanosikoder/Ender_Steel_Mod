package net.panther.endersteel.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.panther.endersteel.effect.ModEffects;

public class GazingVoidOverlay implements HudRenderCallback {
    private static final Identifier EYE_TEXTURE = Identifier.of("endersteel", "textures/gui/gazing_void.png");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.hasStatusEffect(ModEffects.GAZING_VOID)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            float tickDelta = tickCounter.getTickDelta(false);

            int size = Math.min(width, height) / 2;
            int x = width / 2 - size / 2;
            int y = height / 2 - size / 2;

            // Save current transformation
            MatrixStack matrices = drawContext.getMatrices();
            matrices.push();

            // Pulsing
            float pulse = (float) Math.sin((client.player.age + tickDelta) * 0.3f) * 0.15f + 1f;
            matrices.translate(width / 2f, height / 2f, 0);
            matrices.scale(pulse, pulse, 1f);
            matrices.translate(-width / 2f, -height / 2f, 0);

            // Draw Eye
            drawContext.drawTexture(
                    EYE_TEXTURE,
                    x, y, 0, 0,
                    size, size,
                    size, size
            );

            matrices.pop();
        }
    }
}