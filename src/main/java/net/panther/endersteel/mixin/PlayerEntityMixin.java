package net.panther.endersteel.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.panther.endersteel.component.EnderSteelDataComponents;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.enchantment.effect.RepulsiveShriekEffect;
import net.panther.endersteel.event.EnderSteelArmorEvents;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamage(ServerWorld world, DamageSource source, float amount,
                                CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.isBlocking()) return;

        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (chestplate.getItem() instanceof EnderSteelArmorItem) {
            RegistryEntry<Enchantment> shriekEnchantment =
                    player.getWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.REPULSIVE_SHRIEK)
                            .orElse(null);

            if (shriekEnchantment != null) {
                int shriekLevel = EnchantmentHelper.getLevel(shriekEnchantment, chestplate);
                if (shriekLevel > 0 && source.getAttacker() != null) {
                    int evasionCharges = chestplate.getOrDefault(EnderSteelDataComponents.EVASION_CHARGES, 0);
                    boolean isLastCharge = evasionCharges == 1;

                    if (EnderSteelArmorEvents.useCharge(player)) {
                        RepulsiveShriekEffect.onPlayerDamaged(player, source.getAttacker(), amount, isLastCharge);
                        cir.setReturnValue(false);
                        cir.cancel();
                    }
                }
            }
        }
    }
}