package net.panther.endersteel.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.enchantment.RepulsiveShriekEnchantment;
import net.panther.endersteel.event.EnderSteelArmorEvents;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // Don't handle damage if player is blocking with shield
        if (player.isBlocking()) {
            return;
        }

        // Check for Repulsive Shriek enchantment
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (chestplate.getItem() instanceof EnderSteelArmorItem) {
            int shriekLevel = EnchantmentHelper.getLevel(ModEnchantments.REPULSIVE_SHRIEK, chestplate);
            
            // If we have Repulsive Shriek, use it instead of teleporting
            if (shriekLevel > 0 && source.getAttacker() != null) {
                RepulsiveShriekEnchantment shriek = (RepulsiveShriekEnchantment) ModEnchantments.REPULSIVE_SHRIEK;
                boolean isLastCharge = chestplate.getOrCreateNbt().getInt("evasion_charges") == 1;
                
                // Use a charge to repulse enemies
                if (EnderSteelArmorEvents.useCharge(player)) {
                    shriek.onPlayerDamaged(player, source.getAttacker(), amount, isLastCharge);
                    cir.setReturnValue(false);
                    cir.cancel();
                }
                return;
            }
        }

        // No Repulsive Shriek, try normal evasion teleport
        if (EnderSteelArmorEvents.tryEvade(player, source)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
