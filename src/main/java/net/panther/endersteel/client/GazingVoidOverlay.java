package net.panther.endersteel.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.effect.ModEffects;

public class GazingVoidOverlay implements HudRenderCallback {
    private static final Identifier ENDER_EYE_TEXTURE = new Identifier(EnderSteel.MOD_ID, "textures/mob_effect/gazing_void.png");
    private float alpha = 0.0f;
    private long lastUpdateTime = 0;

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && client.player.hasStatusEffect(ModEffects.GAZING_VOID)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            
            // Pulsing effect
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > 50) { // Update every 50ms
                alpha = 0.3f + (float)(Math.sin(currentTime * 0.005) * 0.2); // Oscillate between 0.1 and 0.5
                lastUpdateTime = currentTime;
            }
            
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
            RenderSystem.setShaderTexture(0, ENDER_EYE_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            // Draw the eye texture in the center of the screen
            int baseSize = Math.min(width, height);
            int size = (int)(baseSize * 0.8); // (80% of screen height/width)
            context.drawTexture(
                ENDER_EYE_TEXTURE,
                width / 2 - size / 2,
                height / 2 - size / 2,
                0, 0,
                size, size,
                size, size
            );
            
            // Reset render system
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
    }
}
