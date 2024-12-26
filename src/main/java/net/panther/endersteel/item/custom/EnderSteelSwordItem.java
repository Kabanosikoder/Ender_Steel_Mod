package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.util.TeleportUtil;

import java.util.List;
import java.util.Random;

public class EnderSteelSwordItem extends SwordItem {
    private static final int MAX_STORED_PEARLS = 10;
    private static final String STREAK_KEY = "teleport_streak";
    private static final Random random = new Random();
    private static final float STREAK_BASE_CHANCE = 0.15f; // 15% base chance

    public EnderSteelSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int storedPearls = getStoredPearls(stack);
        int currentStreak = getStreak(stack);

        // Always show current streak if it exists
        if (currentStreak > 0) {
            tooltip.add(Text.literal("Current Streak: " + currentStreak).formatted(Formatting.LIGHT_PURPLE));
        }

        // Show bonus damage if we have a streak
        if (currentStreak > 0) {
            float bonusDamage = currentStreak * 0.5f;
            tooltip.add(Text.literal("Bonus Damage: +" + String.format("%.1f", bonusDamage))
                    .formatted(Formatting.LIGHT_PURPLE));
        }

        // Show stored pearls
        tooltip.add(Text.translatable("item.endersteel.ender_steel_sword.pearl_stored", storedPearls, MAX_STORED_PEARLS)
                .formatted(Formatting.LIGHT_PURPLE));
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
        if (!attacker.getWorld().isClient && target.isAlive()) {
            int storedPearls = getStoredPearls(stack);
            if (storedPearls > 0) {
                // Check for Ender Strike enchantment
                if (EnchantmentHelper.getLevel(ModEnchantments.ENDER_STRIKE, stack) > 0) {
                    if (random.nextFloat() < 0.5f) {  // 50% chance
                        if (teleportEntity(target, target.getWorld())) {
                            setStoredPearls(stack, storedPearls - 1);
                        }
                    }
                } else if (EnchantmentHelper.getLevel(ModEnchantments.ENDER_STREAK, stack) > 0) {
                    // Ender Streak enchantment logic
                    int streakLevel = EnchantmentHelper.getLevel(ModEnchantments.ENDER_STREAK, stack);
                    int currentStreak = getStreak(stack);
                    
                    // 15% chance to trigger streak bonus damage
                    if (random.nextFloat() < STREAK_BASE_CHANCE) {
                        setStoredPearls(stack, storedPearls - 1);
                        setStreak(stack, currentStreak + 1);
                        
                        // Bonus damage based on streak, enhanced by enchantment level
                        float bonusDamage = currentStreak * 0.5f * streakLevel;
                        if (bonusDamage > 0) {
                            target.damage(target.getDamageSources().magic(), bonusDamage);
                        }
                    }
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    private int getStoredPearls(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("stored_pearls");
    }

    private void setStoredPearls(ItemStack stack, int count) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("stored_pearls", count);
    }

    private int getStreak(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt(STREAK_KEY);
    }

    private void setStreak(ItemStack stack, int streak) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(STREAK_KEY, streak);
    }

    private boolean teleportEntity(LivingEntity entity, World world) {
        if (world.isClient) return false;
        return TeleportUtil.teleportRandomly(entity, 5.0);
    }

    private float getAttackSpeed() {
        return 4.0f + this.getAttackSpeedModifier();
    }

    private float getAttackSpeedModifier() {
        return -2.4f;
    }
}
