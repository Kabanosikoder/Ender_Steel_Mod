package net.panther.endersteel.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.panther.endersteel.config.ModConfig;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class EnderSteelArmorMixin {
    @Unique
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    @Unique
    private static final String EVASION_COOLDOWN_KEY = "evasion_cooldown";

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void onInventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if ((Object) this instanceof EnderSteelArmorItem armorItem && entity instanceof PlayerEntity player && !world.isClient) {
            // Only process chestplate
            if (slot != 38) { // 38 is the Chestplate slot in inventory
                return;
            }

            NbtCompound nbt = (NbtCompound) stack.getComponents();

            // Initialize charges if not set
            if (!nbt.contains(EVASION_CHARGES_KEY)) {
                nbt.putInt(EVASION_CHARGES_KEY, ModConfig.MAX_EVASION_CHARGES);
            }

            // Cooldown
            int cooldown = nbt.getInt(EVASION_COOLDOWN_KEY);
            if (cooldown > 0) {
                nbt.putInt(EVASION_COOLDOWN_KEY, cooldown - 1);

                if (cooldown == 1) {
                    armorItem.setCharges(stack, ModConfig.MAX_EVASION_CHARGES);
                }
            }
        }
    }
}
