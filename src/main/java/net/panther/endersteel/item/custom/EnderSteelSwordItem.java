package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class EnderSteelSwordItem extends SwordItem {
    private static final String PEARL_COUNT_KEY = "PearlCount";
    private static final String TELEPORT_STREAK_KEY = "TeleportStreak";
    private static final int MAX_PEARLS = 10;
    private static final float BASE_TELEPORT_CHANCE = 0.15f;
    private static final float STREAK_BONUS = 0.025f; // 2.5% increase per successful teleport

    public EnderSteelSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack swordStack = player.getStackInHand(hand);
        ItemStack otherStack = player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);

        if (otherStack.isOf(Items.ENDER_PEARL)) {
            int currentPearls = getPearlCount(swordStack);
            if (currentPearls < MAX_PEARLS) {
                setPearlCount(swordStack, currentPearls + 1);
                if (!player.getAbilities().creativeMode) {
                    otherStack.decrement(1);
                }
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.sendMessage(Text.literal("Pearl absorbed! ").formatted(Formatting.DARK_PURPLE)
                        .append(Text.literal((currentPearls + 1) + "/" + MAX_PEARLS).formatted(Formatting.GRAY)), true);
                return TypedActionResult.success(swordStack);
            }
        }
        return TypedActionResult.pass(swordStack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getWorld();
        Random random = world.getRandom();
        int pearlCount = getPearlCount(stack);
        int streak = getTeleportStreak(stack);
        float teleportChance = BASE_TELEPORT_CHANCE + (streak * STREAK_BONUS);

        if (pearlCount > 0 && random.nextFloat() < teleportChance) {
            // Deal magic damage
            target.damage(target.getDamageSources().magic(), 2.5f);
            
            // Attempt to teleport the target
            double originalX = target.getX();
            double originalY = target.getY();
            double originalZ = target.getZ();

            for(int i = 0; i < 16; ++i) {
                double targetX = target.getX() + (random.nextDouble() - 0.5D) * 16.0D;
                double targetY = MathHelper.clamp(target.getY() + (double)(random.nextInt(16) - 8),
                        world.getBottomY(), world.getBottomY() + ((world.getTopY() - world.getBottomY()) / 2));
                double targetZ = target.getZ() + (random.nextDouble() - 0.5D) * 16.0D;

                if (target.teleport(targetX, targetY, targetZ, true)) {
                    world.playSound(null, originalX, originalY, originalZ,
                            SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    
                    // Consume a pearl and increase streak
                    setPearlCount(stack, pearlCount - 1);
                    setTeleportStreak(stack, streak + 1);
                    
                    if (attacker instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) attacker;
                        float nextChance = BASE_TELEPORT_CHANCE + ((streak + 1) * STREAK_BONUS);
                        player.sendMessage(Text.literal("Enemy teleported! ").formatted(Formatting.DARK_PURPLE)
                                .append(Text.literal((pearlCount - 1) + " pearls remaining | ")
                                .append(Text.literal(String.format("%.1f%% chance", nextChance * 100)))
                                .formatted(Formatting.GRAY)), true);
                    }
                    break;
                }
            }
        } else if (pearlCount == 0) {
            // Reset streak when out of pearls
            setTeleportStreak(stack, 0);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int pearlCount = getPearlCount(stack);
        int streak = getTeleportStreak(stack);
        float currentChance = BASE_TELEPORT_CHANCE + (streak * STREAK_BONUS);
        
        tooltip.add(Text.literal("Stored Pearls: ").formatted(Formatting.DARK_PURPLE)
                .append(Text.literal(pearlCount + "/" + MAX_PEARLS).formatted(Formatting.GRAY)));
        tooltip.add(Text.literal(String.format("Teleport Chance: %.1f%%", currentChance * 100)).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Right-click with Ender Pearl to store").formatted(Formatting.BLUE));
        super.appendTooltip(stack, world, tooltip, context);
    }

    private static int getPearlCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(PEARL_COUNT_KEY);
    }

    private static void setPearlCount(ItemStack stack, int count) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(PEARL_COUNT_KEY, count);
    }

    private static int getTeleportStreak(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(TELEPORT_STREAK_KEY);
    }

    private static void setTeleportStreak(ItemStack stack, int streak) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(TELEPORT_STREAK_KEY, streak);
    }
}
