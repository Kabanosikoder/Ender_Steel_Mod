package net.panther.endersteel.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;

public class EvasionChargesOverlay implements HudRenderCallback {
    private static final Identifier CHARGE_EMPTY =  Identifier.of(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_empty.png");
    private static final Identifier CHARGE_FULL = Identifier.of(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_full.png");
    private static final int TEXTURE_SIZE = 9;
    private static final int MAX_CHARGES = 5;


    private boolean isWearingFullEnderSteelArmor(PlayerEntity player) {
        for (ItemStack armorPiece : player.getArmorItems()) {
            if (!(armorPiece.getItem() instanceof EnderSteelArmorItem)) {
                return false;
            }
        }
        return true;
    }

    private int getEvasionCharges(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (chestplate.getItem() instanceof EnderSteelArmorItem armorItem) {
            return armorItem.getCharges(chestplate);
        }
        return 0;
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null || !isWearingFullEnderSteelArmor(player)) {
            return;
        }

        int charges = getEvasionCharges(player);
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Position above the armor bar, which is at y=-45
        int x = screenWidth / 2 - 91; // Same alignment as vanilla bars
        int y = screenHeight - 59;    // 4 pixels above armor bar

        // Draw charge icons from left to right
        for (int i = 0; i < MAX_CHARGES; i++) {
            int iconX = x + (i * (TEXTURE_SIZE + 1));
            Identifier texture = i < charges ? CHARGE_FULL : CHARGE_EMPTY;
            
            // Draw the texture using vanilla-style parameters
            drawContext.drawTexture(
                texture,         // texture to use
                iconX,          // x position
                y,              // y position
                0,              // u offset in texture
                0,              // v offset in texture
                TEXTURE_SIZE,   // width to draw
                TEXTURE_SIZE,   // height to draw
                TEXTURE_SIZE,   // texture width
                TEXTURE_SIZE    // texture height
            );
        }
    }
}