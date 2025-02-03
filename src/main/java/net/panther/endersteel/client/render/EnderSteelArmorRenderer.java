package net.panther.endersteel.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class EnderSteelArmorRenderer {
    private static final Identifier CHARGE_EMPTY = Identifier.of(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_empty.png");
    private static final Identifier CHARGE_FULL = Identifier.of(EnderSteel.MOD_ID, "textures/item/ender_steel_armor_full.png");
    
    public static void renderChargeBar(DrawContext context, ItemStack stack, int x, int y) {
        if (!(stack.getItem() instanceof EnderSteelArmorItem armorItem)) return;
        
        int charges = armorItem.getCharges(stack);
        int maxCharges = 5;
        
        // Draw charge indicators
        for (int i = 0; i < maxCharges; i++) {
            int barX = x + (i * 8); // 8 pixels between each charge
            Identifier texture = i < charges ? CHARGE_FULL : CHARGE_EMPTY;
            context.drawTexture(texture, barX, y, 0, 0, 8, 8, 8, 8);
        }
    }
}
