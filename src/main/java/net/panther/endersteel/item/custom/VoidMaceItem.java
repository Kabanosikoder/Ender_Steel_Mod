package net.panther.endersteel.item.custom;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
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
        super(settings.attributeModifiers(MaceItem.createAttributeModifiers()));
        this.material = material;
    }

    private void applyEyeEffects(LivingEntity target, int sockets) {
    }

    private void applyPearlEffects(LivingEntity target, int sockets) {
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

    private void applyWindBurstEffect(World world, LivingEntity attacker, LivingEntity target, int windBurstLevel, int sockets) {
        if (windBurstLevel <= 0) return;
        
        // Combined range calculation
        double baseRange = 2.0 + (sockets * 1.5); // Base from sockets
        double range = baseRange + (windBurstLevel * 1.5); // Enhanced by wind burst
        
        // Combined pull strength
        float basePullStrength = 0.3f;
        float pullStrength = basePullStrength + (windBurstLevel * 0.1f);
        
        // Get all nearby entities (excluding attacker)
        List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
            LivingEntity.class,
            target.getBoundingBox().expand(range),
            entity -> entity != attacker
        );
        
        for (LivingEntity entity : nearbyEntities) {
            // Calculate direction (pull towards target for universal effect)
            Vec3d direction = target.getPos().subtract(entity.getPos()).normalize();
            double distance = target.distanceTo(entity);
            double distanceFactor = 1 + ((range - distance) / range);
            
            // Apply pull effect
            entity.addVelocity(
                direction.x * pullStrength * distanceFactor,
                direction.y * 0.5 + 0.2,
                direction.z * pullStrength * distanceFactor
            );
            
            // Apply slowness - combined from both effects
            int slownessDuration = 40 + (sockets * 40) + (windBurstLevel * 20);
            int slownessAmplifier = Math.min(
                Math.max(0, sockets - 1) + windBurstLevel, 
                3
            );
            
            entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                slownessDuration,
                slownessAmplifier,
                false,
                false
            ));
            
            // Particles - using end rod particles from universal effect
            if (world instanceof ServerWorld serverWorld) {
                int particleCount = 5 + (windBurstLevel * 2);
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    particleCount,
                    0.3, 0.3, 0.3,
                    0.1
                );
            }
        }
        
        // Sound - using ender dragon flap from universal effect
        float volume = 0.7f + (windBurstLevel * 0.1f);
        world.playSound(
            null,
            target.getX(),
            target.getY(),
            target.getZ(),
            SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
            SoundCategory.PLAYERS,
            volume,
            0.8f + world.random.nextFloat() * 0.4f
        );
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.postHit(stack, target, attacker);
        
        if (result) {
            int sockets = getFilledSockets(stack);
            if (sockets > 0) {
                // Check if Wind Burst enchantment is present
                boolean hasWindBurst = EnchantmentHelper.getEnchantments(Enchantment.get).containsKey(stack);

                if (hasWindBurst) {
                    // Apply effect with socket count
                    applyWindBurstEffect(attacker.getWorld(), attacker, target, 1, sockets);
                    
                    // Consume one socket
                    int newSockets = Math.max(0, sockets - 1);
                    stack.set(EnderSteelDataComponents.VOID_MACE_SOCKETS, newSockets);
                    
                    // Play a sound when socket is consumed
                    attacker.getWorld().playSound(
                        null,
                        attacker.getX(),
                        attacker.getY(),
                        attacker.getZ(),
                        SoundEvents.ENTITY_ENDER_EYE_DEATH,
                        SoundCategory.PLAYERS,
                        0.5f,
                        1.0f
                    );
                }
                
                // Apply specific socket effects if we still have sockets left
                if (sockets > 1) {
                    String socketType = getSocketType(stack);
                    if (EYE_TYPE.equals(socketType)) {
                        applyEyeEffects(target, sockets - 1);
                    } else if (PEARL_TYPE.equals(socketType)) {
                        applyPearlEffects(target, sockets - 1);
                    }
                }
            }
        }
        
        return result;
    }
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 1000;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
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