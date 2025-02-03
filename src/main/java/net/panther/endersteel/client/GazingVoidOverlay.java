package net.panther.endersteel.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.effect.ModEffects;

public class GazingVoidOverlay implements HudRenderCallback {
    private static final Identifier ENDER_EYE_TEXTURE = Identifier.of(EnderSteel.MOD_ID, "textures/mob_effect/gazing_void.png");
    private static final Identifier END_PORTAL_TEXTURE = Identifier.of("minecraft", "textures/entity/end_portal.png");
    private static final float PULSE_SPEED = 2.0f; // Complete pulse cycle every 0.5 seconds
    private static final float MIN_ALPHA = 0.3f;
    private static final float MAX_ALPHA = 0.8f;

    public static void register() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !client.player.hasStatusEffect(ModEffects.GAZING_VOID)) {
            return;
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Update alpha for pulsating effect
        long currentTime = System.currentTimeMillis();

        // Calculate pulsating alpha using sine wave
        float alpha = (float) (MIN_ALPHA + (MAX_ALPHA - MIN_ALPHA) *
                (0.5 + 0.5 * Math.sin(currentTime * PULSE_SPEED * 0.001 * Math.PI)));

        // Set up rendering parameters
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        
        // Draw end portal background
        RenderSystem.setShaderTexture(0, END_PORTAL_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha * 0.3f);
        drawContext.drawTexture(END_PORTAL_TEXTURE, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

        // Draw the eye of ender
        RenderSystem.setShaderTexture(0, ENDER_EYE_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        
        // Calculate eye size and position (centered, 1/3 of screen height)
        int eyeSize = screenHeight / 3;
        int eyeX = (screenWidth - eyeSize) / 2;
        int eyeY = (screenHeight - eyeSize) / 2;
        
        drawContext.drawTexture(ENDER_EYE_TEXTURE, eyeX, eyeY, 0, 0, eyeSize, eyeSize, eyeSize, eyeSize);

        // Reset render states
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
