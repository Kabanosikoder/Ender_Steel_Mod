package net.panther.endersteel.item.custom;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.panther.endersteel.datagen.EnchantmentGenerator;
import net.panther.endersteel.item.EndSteelToolMaterial;
import net.panther.endersteel.component.EnderSteelDataComponents;
import net.panther.endersteel.util.TeleportUtil;

import java.util.List;
import java.util.Random;

public class EnderSteelSwordItem extends SwordItem {
    private static final int MAX_STORED_PEARLS = 10;
    private static final float STREAK_BASE_CHANCE = 0.5f; // 50% base chance
    private static final Random random = new Random();

    public EnderSteelSwordItem(EndSteelToolMaterial enderSteel, Settings settings) {
        super(EndSteelToolMaterial.ENDER_STEEL, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int storedPearls = getStoredPearls(stack);
        tooltip.add(Text.translatable("tooltip.ender_steel.sword.pearls_stored", storedPearls, MAX_STORED_PEARLS)
                .formatted(Formatting.DARK_PURPLE));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ItemStack offhandStack = player.getOffHandStack();

        if (offhandStack.getItem() == net.minecraft.item.Items.ENDER_PEARL) {
            int storedPearls = getStoredPearls(stack);

            if (storedPearls < MAX_STORED_PEARLS) {
                if (!player.isCreative()) {
                    offhandStack.decrement(1);
                }
                setStoredPearls(stack, storedPearls + 1);

                if (!world.isClient) {
                    player.sendMessage(Text.translatable("item.endersteel.ender_steel_sword.pearls_stored",
                            storedPearls + 1, MAX_STORED_PEARLS).formatted(Formatting.DARK_PURPLE), true);
                }
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // I know this looks bad but it works
        if (!attacker.getWorld().isClient && target.isAlive()) {
            int storedPearls = getStoredPearls(stack);
            if (storedPearls > 0) {
                World world = attacker.getWorld();
                if (EnchantmentHelper.getLevel(EnchantmentGenerator.getVoidStrike(world), stack) > 0) {
                    if (attacker instanceof PlayerEntity) {
                        if (random.nextFloat() < 0.25f) {  // 25% chance
                            teleportEntity(target, world);
                            setStoredPearls(stack, storedPearls - 1);

                            if (!world.isClient) {
                                attacker.sendMessage(Text.translatable("item.endersteel.ender_steel_sword.void_strike_success")
                                        .formatted(Formatting.DARK_PURPLE));
                            }
                        }
                    }
                } else if (EnchantmentHelper.getLevel(EnchantmentGenerator.getEnderStreak(world), stack) > 0) {
                    int streakLevel = EnchantmentHelper.getLevel(EnchantmentGenerator.getEnderStreak(world), stack);
                    int currentStreak = getStreak(stack);

                    // Calculate chance: 50% base + 5% per streak
                    float streakChance = STREAK_BASE_CHANCE + (currentStreak * 0.05f);
                    
                    if (random.nextFloat() < streakChance) {
                        setStoredPearls(stack, storedPearls - 1);

                        // Streak cap
                        if (currentStreak >= 10) {
                            setStreak(stack, 0); // Reset streak at 10
                            world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                                    SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);
                        } else {
                            setStreak(stack, currentStreak + 1);
                            // Only spawn particles on successful streak increment
                            if (!world.isClient && attacker instanceof PlayerEntity player) {
                                Random random = (Random) world.getRandom();
                                for (int i = 0; i < 15; i++) {
                                    double offsetX = (random.nextDouble() - 0.5) * 2.0;
                                    double offsetY = random.nextDouble() * 2.0;
                                    double offsetZ = (random.nextDouble() - 0.5) * 2.0;
                                    ((ServerWorld) world).spawnParticles(
                                        ParticleTypes.PORTAL,
                                        player.getX() + offsetX,
                                        player.getY() + offsetY,
                                        player.getZ() + offsetZ,
                                        1, 0, 0, 0, 0
                                    );
                                }
                            }
                        }

                        float bonusDamage = currentStreak * 0.5f * streakLevel;

                        if (bonusDamage > 0) {
                            target.damage(target.getDamageSources().magic(), bonusDamage);
                            world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                                    SoundEvents.ENTITY_ENDERMAN_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    private static int getStoredPearls(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.STORED_PEARLS, 0);
    }

    private static void setStoredPearls(ItemStack stack, int pearls) {
        stack.set(EnderSteelDataComponents.STORED_PEARLS, Math.min(pearls, MAX_STORED_PEARLS));
    }

    private static int getStreak(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.TELEPORT_STREAK, 0);
    }

    private static void setStreak(ItemStack stack, int streak) {
        stack.set(EnderSteelDataComponents.TELEPORT_STREAK, streak);
    }

    private void teleportEntity(LivingEntity entity, World world) {
        if (world.isClient) return;
        TeleportUtil.teleportRandomly(entity, 5.0);
    }
}