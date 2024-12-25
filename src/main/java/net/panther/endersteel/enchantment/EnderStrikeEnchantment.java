package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.panther.endersteel.item.custom.EnderSteelSwordItem;

public class EnderStrikeEnchantment extends Enchantment {
    public EnderStrikeEnchantment() {
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
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof EnderSteelSwordItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.ENDER_STREAK;
    }

    @Override
    public boolean isTreasure() {
        return true;  // This will hide it from the enchantment table
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;  // This will hide it from villager trades
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;  // This will hide it from random loot
    }
}
