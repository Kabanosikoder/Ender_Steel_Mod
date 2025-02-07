package net.panther.endersteel.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class EnderSteelArmorItem extends ArmorItem {
    private static final String CHARGES_KEY = "evasion_charges";
    private static final int MAX_CHARGES = 5;

    public EnderSteelArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()) {
            if(entity instanceof PlayerEntity player) {
                if(hasFullSuitOfArmorOn(player) && hasCorrectArmorOn(player)) {
                    // Initialize charges if needed
                    NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
                    if (!nbtComponent.contains(CHARGES_KEY)) {
                        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.putInt(CHARGES_KEY, MAX_CHARGES));
                    }
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean hasFullSuitOfArmorOn(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        ItemStack leggings = player.getInventory().getArmorStack(1);
        ItemStack breastplate = player.getInventory().getArmorStack(2);
        ItemStack helmet = player.getInventory().getArmorStack(3);

        return !helmet.isEmpty() && !breastplate.isEmpty()
                && !leggings.isEmpty() && !boots.isEmpty();
    }

    private boolean hasCorrectArmorOn(PlayerEntity player) {
        for (ItemStack armorStack: player.getInventory().armor) {
            if(!(armorStack.getItem() instanceof EnderSteelArmorItem)) {
                return false;
            }
        }

        return true;
    }

    // Charge management methods
    public int getCharges(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        return nbtComponent.contains(CHARGES_KEY) ? nbtComponent.getNbt().getInt(CHARGES_KEY) : 0;
    }

    public void setCharges(ItemStack stack, int charges) {
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> 
            nbt.putInt(CHARGES_KEY, Math.max(0, Math.min(charges, MAX_CHARGES)))
        );
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}