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
import net.minecraft.nbt.NbtCompound;
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

import java.util.List;

public class EnderSteelScytheItem extends SwordItem {
    private static final String VOID_GAZE_ACTIVE_KEY = "VoidGazeActive";
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
        // Handle Void Gaze effect
        if (isVoidGazeActive(stack)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4));
            target.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, 100, 0));

            target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.PLAYERS, 1.0F, 0.5F);
            setVoidGazeActive(stack, false);
        }

        // Handle Phantom Harvest effect
        World world = target.getWorld();
        if (world != null && EnchantmentHelper.getLevel(EnchantmentGenerator.getPhantomHarvest(world), stack) > 0 &&
                !target.isAlive() && attacker instanceof PlayerEntity player) {

            // Prevent duplicate processing
            if (target == lastKilledEntity) {
                return super.postHit(stack, target, attacker);
            }
            lastKilledEntity = target;

            // Check if wearing full Ender Steel armor
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
                    NbtCompound nbt = (NbtCompound) chestplate.getComponents();
                    int currentCharges = nbt.getInt("evasion_charges");
                    if (currentCharges < 5) {
                        nbt.putInt("evasion_charges", currentCharges + 1);
                        // Play recharge sound
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
                // Heal player by 1 heart on kill
                player.heal(1.0f);

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

    private static boolean isVoidGazeActive(ItemStack stack) {
        NbtCompound nbt = (NbtCompound) stack.getComponents();
        return nbt.getBoolean(VOID_GAZE_ACTIVE_KEY);
    }

    private static void setVoidGazeActive(ItemStack stack, boolean active) {
        NbtCompound nbt = (NbtCompound) stack.getComponents();
        nbt.putBoolean(VOID_GAZE_ACTIVE_KEY, active);
    }
}
