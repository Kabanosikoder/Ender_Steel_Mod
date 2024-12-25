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
    private static final Identifier CHARGE_EMPTY = new Identifier(EnderSteel.MOD_ID, "textures/gui/evasion_charge_empty.png");
    private static final Identifier CHARGE_FULL = new Identifier(EnderSteel.MOD_ID, "textures/gui/evasion_charge_full.png");
    private static final int TEXTURE_SIZE = 9;
    private static final int MAX_CHARGES = 5;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;

        // Check if wearing full Ender Steel armor
        if (!isWearingFullEnderSteelArmor(player)) return;

        int charges = getEvasionCharges(player);
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Position the bar above the hot bar
        int startX = screenWidth / 2 - (MAX_CHARGES * TEXTURE_SIZE) / 2;
        int startY = screenHeight - 50;

        // Draw the charges
        for (int i = 0; i < MAX_CHARGES; i++) {
            Identifier texture = i < charges ? CHARGE_FULL : CHARGE_EMPTY;
            drawContext.drawTexture(texture, startX + (i * TEXTURE_SIZE), startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }

    private boolean isWearingFullEnderSteelArmor(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (!(stack.getItem() instanceof EnderSteelArmorItem)) {
                return false;
            }
        }
        return true;
    }

    private int getEvasionCharges(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2); // 2 is chestplate slot
        if (chestplate.isEmpty() || !(chestplate.getItem() instanceof EnderSteelArmorItem)) {
            return 0;
        }

        NbtCompound nbt = chestplate.getOrCreateNbt();
        return nbt.getInt("evasion_charges");
    }
}
