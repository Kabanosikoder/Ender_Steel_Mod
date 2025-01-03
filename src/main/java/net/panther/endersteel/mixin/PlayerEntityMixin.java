package net.panther.endersteel.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.panther.endersteel.event.EnderSteelArmorEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (EnderSteelArmorEvents.handleDamage(player, source)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
