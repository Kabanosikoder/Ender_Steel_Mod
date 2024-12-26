package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantments;
import net.panther.endersteel.item.custom.EnderSteelSwordItem;

public class EnderStreakEnchantment extends Enchantment {
    public EnderStreakEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 15;
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof EnderSteelSwordItem;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != Enchantments.SHARPNESS;
    }
}
