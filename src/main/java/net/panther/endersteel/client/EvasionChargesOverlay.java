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
    private static final int TEXTURE_SIZE = 8;
    private static final int MAX_CHARGES = 5;
    private static final int RECHARGE_TIME = 1400; // 70 seconds in ticks

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;

        // Check if wearing full Ender Steel armor
        if (!isWearingFullEnderSteelArmor(player)) return;

        ItemStack chestplate = player.getInventory().getArmorStack(2);
        int charges = getEvasionCharges(player);
        int cooldown = getCooldown(chestplate);
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Position the bar above the armor bar
        int startX = screenWidth / 2 - (MAX_CHARGES * TEXTURE_SIZE) / 2;
        int startY = screenHeight - 59;

        // Draw the charges
        for (int i = 0; i < MAX_CHARGES; i++) {
            int x = startX + (i * TEXTURE_SIZE);
            
            if (i < charges) {
                // Full charge
                drawContext.drawTexture(CHARGE_FULL, x, startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
            } else if (i == charges && cooldown > 0) {
                // Recharging charge - draw both textures with alpha based on progress
                float progress = 1.0f - (cooldown / (float)RECHARGE_TIME);
                
                // Draw empty background
                drawContext.drawTexture(CHARGE_EMPTY, x, startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
                
                // Draw filling charge with alpha
                drawContext.setShaderColor(1.0f, 1.0f, 1.0f, progress);
                drawContext.drawTexture(CHARGE_FULL, x, startY, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
                drawContext.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            } else {
                // Empty charge
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
    
    private int getCooldown(ItemStack chestplate) {
        if (chestplate.getItem() instanceof EnderSteelArmorItem) {
            NbtCompound nbt = chestplate.getNbt();
            if (nbt != null) {
                return nbt.getInt("evasion_cooldown");
            }
        }
        return 0;
    }
}
