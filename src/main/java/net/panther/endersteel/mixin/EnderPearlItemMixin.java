package net.panther.endersteel.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.panther.endersteel.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin extends Item {
    public EnderPearlItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void preventPearlThrow(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        // Check if the off-hand has an ender pearl and main hand has a mace
        if ((hand == Hand.OFF_HAND && user.getMainHandStack().isOf(ModItems.VOID_MACE)) ||
            (hand == Hand.MAIN_HAND && user.getOffHandStack().isOf(Items.ENDER_PEARL) && user.getMainHandStack().isOf(ModItems.VOID_MACE))) {
            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
        }
    }
}

@Mixin(Item.class)
abstract class EnderEyeItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void preventEyeThrow(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);

        if (!stack.isOf(Items.ENDER_EYE)) return;
        // Check if the off-hand has an ender eye and main hand has a mace
        if ((hand == Hand.OFF_HAND && user.getMainHandStack().isOf(ModItems.VOID_MACE)) ||
            (hand == Hand.MAIN_HAND && user.getOffHandStack().isOf(Items.ENDER_EYE) && user.getMainHandStack().isOf(ModItems.VOID_MACE))) {
            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
        }
    }
}