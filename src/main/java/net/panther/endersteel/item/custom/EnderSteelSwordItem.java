package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.panther.endersteel.enchantment.ModEnchantments;

import java.util.List;
import java.util.Random;

public class EnderSteelSwordItem extends SwordItem {
    private static final String STORED_PEARLS_KEY = "StoredPearls";
    private static final String TELEPORT_STREAK_KEY = "TeleportStreak";
    private static final int MAX_STORED_PEARLS = 10;
    private final Random random = new Random();

    public EnderSteelSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && target.isAlive()) {
            int storedPearls = getStoredPearls(stack);
            if (storedPearls > 0) {
                // Check for Ender's Edge enchantment
                if (EnchantmentHelper.getLevel(ModEnchantments.ENDERS_EDGE, stack) > 0) {
                    if (random.nextDouble() < 0.5) { // 50% chance
                        if (teleportEntity(target, attacker.getWorld())) {
                            playTeleportEffects(target);
                            setStoredPearls(stack, storedPearls - 1);
                        }
                    }
                } else {
                    // Original behavior with stored pearls
                    int streak = getStreak(stack);
                    double chance = 0.1 + (streak * 0.025); // Base 10% + 2.5% per streak

                    if (random.nextDouble() < chance) {
                        if (teleportEntity(target, attacker.getWorld())) {
                            playTeleportEffects(target);
                            
                            // Consume pearl and update streak
                            setStoredPearls(stack, storedPearls - 1);
                            setStreak(stack, streak + 1);
                            
                            // Apply bonus damage
                            target.damage(target.getDamageSources().magic(), 2.5f);
                        }
                    } else {
                        setStreak(stack, 0);
                    }
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    private boolean teleportEntity(LivingEntity entity, World world) {
        double radius = 5.0;
        double theta = random.nextDouble() * 2 * Math.PI;
        
        double dx = radius * Math.cos(theta);
        double dz = radius * Math.sin(theta);
        
        double newX = entity.getX() + dx;
        double newZ = entity.getZ() + dz;
        double newY = entity.getY();
        
        entity.teleport(newX, newY, newZ);
        return true;
    }

    private void playTeleportEffects(Entity entity) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                ParticleTypes.PORTAL,
                entity.getX(), entity.getY() + 0.5, entity.getZ(),
                50, // particle count
                0.5, 0.5, 0.5, // spread
                0.1 // speed
            );
            
            serverWorld.playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                1.0F, 1.0F
            );
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        // Allow pearl storage regardless of enchantments
        ItemStack offhand = player.getOffHandStack();
        if (offhand.getItem().toString().contains("ender_pearl")) {
            int currentPearls = getStoredPearls(stack);
            
            if (currentPearls < MAX_STORED_PEARLS) {
                if (!player.getAbilities().creativeMode) {
                    offhand.decrement(1);
                }
                
                setStoredPearls(stack, currentPearls + 1);
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.PLAYERS,
                        0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                
                return TypedActionResult.success(stack);
            }
        }
        
        return TypedActionResult.pass(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int storedPearls = getStoredPearls(stack);
        tooltip.add(Text.translatable("item.endersteel.sword.stored_pearls", storedPearls, MAX_STORED_PEARLS)
                .formatted(Formatting.AQUA));

        if (EnchantmentHelper.getLevel(ModEnchantments.ENDERS_EDGE, stack) > 0) {
            tooltip.add(Text.translatable("item.endersteel.sword.enders_edge").formatted(Formatting.DARK_PURPLE));
        } else {
            tooltip.add(Text.translatable("item.endersteel.sword.store_instruction")
                    .formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.endersteel.sword.teleport_chance")
                    .formatted(Formatting.DARK_PURPLE));
        }
    }

    private static int getStoredPearls(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(STORED_PEARLS_KEY);
    }

    private static void setStoredPearls(ItemStack stack, int count) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(STORED_PEARLS_KEY, count);
    }

    private static int getStreak(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(TELEPORT_STREAK_KEY);
    }

    private static void setStreak(ItemStack stack, int streak) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(TELEPORT_STREAK_KEY, streak);
    }
}
