package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
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
    private static final String COOLDOWN_KEY = "evasion_cooldown";
    private static final int MAX_CHARGES = 5;
    private static final int CHARGE_COOLDOWN_TICKS = 480;

    public EnderSteelArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        
        // Initialize charges if not set
        if (!stack.getOrCreateNbt().contains(CHARGES_KEY)) {
            setCharges(stack, MAX_CHARGES);  // Initialize to max charges
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.endersteel.armor.set_bonus").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("item.endersteel.armor.teleport_ability").formatted(Formatting.BLUE));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public int getCharges(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.contains(CHARGES_KEY) ? nbt.getInt(CHARGES_KEY) : 0;  // Return 0 if not set
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
            // Start cooldown if we just used our last charge
            if (currentCharges == 1) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putInt(COOLDOWN_KEY, CHARGE_COOLDOWN_TICKS);
            }
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