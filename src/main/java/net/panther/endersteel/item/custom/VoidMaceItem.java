package net.panther.endersteel.item.custom;

import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.panther.endersteel.component.EnderSteelDataComponents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidMaceItem extends MaceItem {
    private static final int MAX_SOCKETS = 4;
    private static final String EYE_TYPE = "eye";
    private static final String PEARL_TYPE = "pearl";
    private final ToolMaterial material;

    public VoidMaceItem(ToolMaterial material, Settings settings) {
        super(settings);
        this.material = material;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.postHit(stack, target, attacker);
        if (result) {
            int sockets = getFilledSockets(stack);
            if (sockets > 0) {
                String socketType = getSocketType(stack);
                if (EYE_TYPE.equals(socketType)) {
                    applyEyeEffects(target, sockets);
                } else if (PEARL_TYPE.equals(socketType)) {
                    applyPearlEffects(target, sockets);
                }
            }
        }
        return result;
    }

    private void applyEyeEffects(LivingEntity target, int sockets) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20 * sockets, 0));
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * sockets * 2, 0));
    }

    private void applyPearlEffects(LivingEntity target, int sockets) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * sockets, 1));
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * sockets, 0));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
    
        int filledSockets = getFilledSockets(stack);
        String socketType = getSocketType(stack);
    
        tooltip.add(Text.translatable("tooltip.void_mace.sockets", filledSockets, MAX_SOCKETS)
                .formatted(Formatting.GRAY));
    
        if (socketType.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.void_mace.unsocketed").formatted(Formatting.DARK_GRAY));
        } else {
            String typeKey = "tooltip.void_mace.socket_type." + socketType;
            tooltip.add(Text.translatable(typeKey, filledSockets).formatted(Formatting.DARK_PURPLE));
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ItemStack offhandStack = user.getOffHandStack();
        
        // Check if player is holding an Eye of Ender or Ender Pearl in offhand
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            if (offhandStack.isOf(Items.ENDER_EYE)) {
                if (addSocket(stack, EYE_TYPE, user)) {
                    offhandStack.decrement(1);
                    user.sendMessage(Text.translatable("message.void_mace.socket_added", "Eye of Ender"), true);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                        SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 
                        0.5f, 1.0f);
                    return TypedActionResult.success(stack);
                }
            } else if (offhandStack.isOf(Items.ENDER_PEARL)) {
                if (addSocket(stack, PEARL_TYPE, user)) {
                    offhandStack.decrement(1);
                    user.sendMessage(Text.translatable("message.void_mace.socket_added", "Ender Pearl"), true);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                        SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 
                        0.5f, 1.0f);
                    return TypedActionResult.success(stack);
                }
            }
        }
        
        // Default behavior if not socketing
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    public static int getFilledSockets(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.VOID_MACE_SOCKETS, 0);
    }

    public static String getSocketType(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.SOCKET_TYPE, "");
    }

    public static boolean canAddSocket(ItemStack stack, String type) {
        int currentSockets = getFilledSockets(stack);
        String currentType = getSocketType(stack);
        return currentSockets < MAX_SOCKETS && (currentType.isEmpty() || currentType.equals(type));
    }

    public static boolean addSocket(ItemStack stack, String type, @Nullable PlayerEntity player) {
        if (!canAddSocket(stack, type)) {
            String currentType = getSocketType(stack);
            if (!currentType.isEmpty() && !currentType.equals(type) && player != null) {
                player.sendMessage(Text.translatable("message.void_mace.wrong_socket_type")
                    .formatted(Formatting.RED), true);
            }
            return false;
        }
        
        int currentSockets = getFilledSockets(stack);
        stack.set(EnderSteelDataComponents.VOID_MACE_SOCKETS, currentSockets + 1);
        stack.set(EnderSteelDataComponents.SOCKET_TYPE, type);
        
        if (player != null) {
            player.playSound(SoundEvents.ITEM_LODESTONE_COMPASS_LOCK
                , 0.5f, 1.0f);
        }
        
        return true;
    }
}