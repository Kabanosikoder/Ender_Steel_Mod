package net.panther.endersteel.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.panther.endersteel.datagen.EnchantmentGenerator;
import net.panther.endersteel.effect.ModEffects;
import net.panther.endersteel.item.EndSteelToolMaterial;
import net.panther.endersteel.component.EnderSteelDataComponents;
import net.panther.endersteel.config.ModConfig;

import java.util.List;

public class EnderSteelScytheItem extends SwordItem {
    private static final int ABILITY_COOLDOWN = 300;
    private Entity lastKilledEntity = null;

    public EnderSteelScytheItem(EndSteelToolMaterial enderSteel, Settings settings) {
        super(EndSteelToolMaterial.ENDER_STEEL, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world == null) {
            return TypedActionResult.pass(stack);
        }

        RegistryEntry<Enchantment> gazingVoid = EnchantmentGenerator.getGazingVoid(world);
        if (gazingVoid == null || EnchantmentHelper.getLevel(gazingVoid, stack) <= 0) {
            return TypedActionResult.pass(stack);
        }

        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            setVoidGazeActive(stack, true);

            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);

            player.sendMessage(Text.literal("Void Gaze activated!").formatted(Formatting.DARK_PURPLE), true);
            player.getItemCooldownManager().set(this, ABILITY_COOLDOWN);

            return TypedActionResult.success(stack);
        } else {
            player.sendMessage(Text.literal("Ability is on cooldown!").formatted(Formatting.RED), true);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        World world = target.getWorld();
        if (world != null && EnchantmentHelper.getLevel(EnchantmentGenerator.getPhantomHarvest(world), stack) > 0 &&
                !target.isAlive() && attacker instanceof PlayerEntity player) {

            if (target == lastKilledEntity) {
                return super.postHit(stack, target, attacker);
            }
            lastKilledEntity = target;

            boolean hasFullSet = true;
            for (ItemStack armorPiece : player.getArmorItems()) {
                if (!(armorPiece.getItem() instanceof EnderSteelArmorItem)) {
                    hasFullSet = false;
                    break;
                }
            }

            if (hasFullSet) {
                // Add 1 charge on kill
                ItemStack chestplate = player.getInventory().getArmorStack(2);
                if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
                    int currentCharges = chestplate.getOrDefault(EnderSteelDataComponents.EVASION_CHARGES, 0);
                    if (currentCharges < ModConfig.MAX_EVASION_CHARGES) {
                        chestplate.set(EnderSteelDataComponents.EVASION_CHARGES, currentCharges + 1);

                        player.getWorld().playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                            SoundCategory.PLAYERS,
                            0.5f,
                            1.2f
                        );

                        if (!player.getWorld().isClient()) {
                            ServerWorld serverWorld = (ServerWorld) player.getWorld();
                            Random random = Random.create();
                            for (int i = 0; i < 15; i++) {
                                double offsetX = (random.nextDouble() - 0.5) * 2.0;
                                double offsetY = random.nextDouble() * 2.0;
                                double offsetZ = (random.nextDouble() - 0.5) * 2.0;
                                serverWorld.spawnParticles(
                                    ParticleTypes.PORTAL,
                                    player.getX() + offsetX,
                                    player.getY() + offsetY,
                                    player.getZ() + offsetZ,
                                    1, 0, 0, 0, 0
                                );
                            }
                        }
                    }
                }
            } else {

                player.heal(4.0f);

                player.getWorld().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ITEM_HONEY_BOTTLE_DRINK,
                    SoundCategory.PLAYERS,
                    0.5f,
                    1.0f
                );
            }
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.getHolder() instanceof Entity entity) {
            World world = entity.getWorld();
            if (EnchantmentHelper.getLevel(EnchantmentGenerator.getGazingVoid(world), stack) > 0) {
                tooltip.add(Text.translatable("tooltip.ender_steel.void_gaze_activate")
                        .formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("tooltip.ender_steel.void_gaze_effect")
                        .formatted(Formatting.GRAY));
            }
        }
    }

    public static boolean isVoidGazeActive(ItemStack stack) {
        return stack.getOrDefault(EnderSteelDataComponents.VOID_GAZE_ACTIVE, false);
    }

    public static void setVoidGazeActive(ItemStack stack, boolean active) {
        stack.set(EnderSteelDataComponents.VOID_GAZE_ACTIVE, active);
    }
}
