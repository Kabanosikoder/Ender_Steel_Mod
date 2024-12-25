package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.effect.ModEffects;

import java.util.List;

public class EnderSteelScytheItem extends SwordItem {
    private static final String VOID_GAZE_ACTIVE_KEY = "VoidGazeActive";
    private static final int ABILITY_COOLDOWN = 300; // 15 seconds in ticks

    public EnderSteelScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        if (EnchantmentHelper.getLevel(ModEnchantments.GAZING_VOID, stack) > 0) {
            if (!player.getItemCooldownManager().isCoolingDown(this)) {
                setVoidGazeActive(stack, true);
                
                // Play activation sound and effect
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);
                
                player.sendMessage(Text.literal("Void Gaze activated!").formatted(Formatting.DARK_PURPLE), true);
                
                // Set cooldown
                player.getItemCooldownManager().set(this, ABILITY_COOLDOWN);
                
                return TypedActionResult.success(stack);
            } else {
                player.sendMessage(Text.literal("Ability is on cooldown!").formatted(Formatting.RED), true);
            }
        }
        
        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isVoidGazeActive(stack)) {
            // Apply effects
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0)); // 5 seconds
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4)); // 5 seconds, very slow
            
            // Apply custom void gaze effect for the overlay
            target.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, 100, 0));
            
            // Effect sound
            target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.PLAYERS, 1.0F, 0.5F);
            
            // Reset the active state
            setVoidGazeActive(stack, false);
        }
        
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (EnchantmentHelper.getLevel(ModEnchantments.GAZING_VOID, stack) > 0) {
            tooltip.add(Text.literal("Right-click to activate Void Gaze").formatted(Formatting.DARK_PURPLE));
            tooltip.add(Text.literal("Next hit blinds and immobilizes target").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    private static boolean isVoidGazeActive(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getBoolean(VOID_GAZE_ACTIVE_KEY);
    }

    private static void setVoidGazeActive(ItemStack stack, boolean active) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean(VOID_GAZE_ACTIVE_KEY, active);
    }
}
