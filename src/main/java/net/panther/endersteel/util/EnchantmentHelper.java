package net.panther.endersteel.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class EnchantmentHelper {
    public static int getLevel(Enchantment enchantment, ItemStack stack) {
        NbtList enchantments = stack.getEnchantments();
        for (int i = 0; i < enchantments.size(); i++) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            if (nbtCompound.getString("id").equals(enchantment.getTranslationKey())) {
                return nbtCompound.getInt("lvl");
            }
        }
        return 0;
    }
}
