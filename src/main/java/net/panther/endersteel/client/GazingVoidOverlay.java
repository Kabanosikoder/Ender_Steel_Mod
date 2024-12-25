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
    private static final Identifier ENDER_EYE_TEXTURE = new Identifier(EnderSteel.MOD_ID, "textures/effect/gazing_void.png");
    private float alpha = 0.0f;
    private long lastUpdateTime = 0;

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && client.player.hasStatusEffect(ModEffects.GAZING_VOID)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            
            // Pulse the alpha for a more dramatic effect
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > 50) { // Update every 50ms
                alpha = 0.3f + (float)(Math.sin(currentTime * 0.005) * 0.2); // Oscillate between 0.1 and 0.5
                lastUpdateTime = currentTime;
            }
            
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            // Draw the ender eye overlay centered on screen
            int baseSize = Math.min(width, height);
            int size = (int)(baseSize * 0.8); // Make it slightly smaller
            context.drawTexture(ENDER_EYE_TEXTURE, 
                    (width - size) / 2, (height - size) / 2,  // Position
                    0, 0,  // UV start
                    size, size,  // Size on screen
                    size, size); // Texture size
            
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
    }
}
