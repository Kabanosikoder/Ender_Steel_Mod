package net.panther.endersteel.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;

public class EvasionChargesOverlay implements HudRenderCallback {
    private static final Identifier CHARGE_EMPTY = new Identifier(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_empty.png");
    private static final Identifier CHARGE_FULL = new Identifier(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_full.png");
    private static final int TEXTURE_SIZE = 9;
    private static final int MAX_CHARGES = 5;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;
        if (!isWearingFullEnderSteelArmor(player)) return;

        ItemStack chestplate = player.getInventory().getArmorStack(2);
        int charges = getEvasionCharges(player);
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int startX = screenWidth / 2 - (MAX_CHARGES * TEXTURE_SIZE) / 2 - 69;  // Aligned armor bar
        int startY = screenHeight - 59;

        for (int i = 0; i < MAX_CHARGES; i++) {
            int x = startX + (i * TEXTURE_SIZE);
            
            if (i < charges) {
                drawContext.drawTexture(CHARGE_FULL, x, startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
            } else {
                drawContext.drawTexture(CHARGE_EMPTY, x, startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
            }
        }
    }

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
}