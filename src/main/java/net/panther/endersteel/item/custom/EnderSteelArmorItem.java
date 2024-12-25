package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class EnderSteelArmorItem extends ArmorItem {
    private static final String CHARGES_KEY = "evasion_charges";
    private static final int MAX_CHARGES = 5;

    public EnderSteelArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.endersteel.armor.set_bonus").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("item.endersteel.armor.teleport_ability").formatted(Formatting.BLUE));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public int getCharges(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.contains(CHARGES_KEY) ? nbt.getInt(CHARGES_KEY) : MAX_CHARGES;
    }

    public void setCharges(ItemStack stack, int charges) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(CHARGES_KEY, Math.max(0, Math.min(charges, MAX_CHARGES)));
    }

    public boolean hasCharges(ItemStack stack) {
        return getCharges(stack) > 0;
    }

    public void consumeCharge(ItemStack stack) {
        int currentCharges = getCharges(stack);
        if (currentCharges > 0) {
            setCharges(stack, currentCharges - 1);
        }
    }

    public boolean isFullyCharged(ItemStack stack) {
        return getCharges(stack) >= MAX_CHARGES;
    }

    public void addCharge(ItemStack stack) {
        int currentCharges = getCharges(stack);
        if (currentCharges < MAX_CHARGES) {
            setCharges(stack, currentCharges + 1);
        }
    }
}
