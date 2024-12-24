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

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && client.player.hasStatusEffect(ModEffects.GAZING_VOID)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
            RenderSystem.setShaderTexture(0, ENDER_EYE_TEXTURE);
            
            // Draw the ender eye overlay centered on screen
            int size = Math.min(width, height);
            context.drawTexture(ENDER_EYE_TEXTURE, 
                    (width - size) / 2, (height - size) / 2,  // Position
                    0, 0,  // UV start
                    size, size,  // Size on screen
                    size, size); // Texture size
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
    }
}
