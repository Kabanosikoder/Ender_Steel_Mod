package net.panther.endersteel.enchantment;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.datagen.ModItemTagProvider;
import net.panther.endersteel.enchantment.effect.EnderStreakEffect;
import net.panther.endersteel.enchantment.effect.GazingVoidEffect;
import net.panther.endersteel.enchantment.effect.GravitideEffect;
import net.panther.endersteel.enchantment.effect.PhantomHarvestEffect;
import net.panther.endersteel.enchantment.effect.RepulsiveShriekEffect;
import net.panther.endersteel.enchantment.effect.VoidStrikeEffect;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> GAZING_VOID =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "gazing_void"));
    public static final RegistryKey<Enchantment> VOID_STRIKE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "void_strike"));
    public static final RegistryKey<Enchantment> ENDER_STREAK =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "ender_streak"));
    public static final RegistryKey<Enchantment> PHANTOM_HARVEST =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "phantom_harvest"));
    public static final RegistryKey<Enchantment> REPULSIVE_SHRIEK =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "repulsive_shriek"));
    public static final RegistryKey<Enchantment> GRAVITIDE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "gravitide"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        // Gazing Void - Scythe only
        register(registerable, GAZING_VOID, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ModItemTagProvider.SCYTHE_ENCHANTABLE),
                4,
                1,
                Enchantment.leveledCost(10,11),
                Enchantment.leveledCost(30,14),
                1,
                AttributeModifierSlot.MAINHAND))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new GazingVoidEffect())
        );

        // Void Strike - Ender Steel Sword only
        register(registerable, VOID_STRIKE, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ModItemTagProvider.ENDER_STEEL_SWORD_ENCHANTABLE),
            4,
            1,
            Enchantment.leveledCost(10,11),
            Enchantment.leveledCost(30,14),
            1,
            AttributeModifierSlot.MAINHAND))
            .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
            .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                    EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                    new VoidStrikeEffect()));

        // Ender Streak - for Ender Steel Sword only
        register(registerable, ENDER_STREAK, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ModItemTagProvider.ENDER_STEEL_SWORD_ENCHANTABLE),
            3,
            1,
            Enchantment.leveledCost(15,20),
            Enchantment.leveledCost(25,30),
            1,
            AttributeModifierSlot.MAINHAND))
            .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
            .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                    EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                    new EnderStreakEffect()));

        // Phantom Harvest - Scythe only
        register(registerable, PHANTOM_HARVEST, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ModItemTagProvider.SCYTHE_ENCHANTABLE),
                2,
                1,
                Enchantment.leveledCost(15,18),
                Enchantment.leveledCost(35,20),
                1,
                AttributeModifierSlot.MAINHAND))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new PhantomHarvestEffect()));

        // Gravitide - Scythe only
        register(registerable, GRAVITIDE, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ModItemTagProvider.SCYTHE_ENCHANTABLE),
                10,
                2,
                Enchantment.leveledCost(15, 10),
                Enchantment.leveledCost(30, 10),
                2,
                AttributeModifierSlot.MAINHAND))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new GravitideEffect())
        );

        // Repulsive Shriek - Chestplate only
        register(registerable, REPULSIVE_SHRIEK, Enchantment.builder(Enchantment.definition(
                         items.getOrThrow(ModItemTagProvider.REPULSIVE_SHRIEK_ENCHANTABLE),
                         2,
                         1,
                         Enchantment.leveledCost(10,12),
                         Enchantment.leveledCost(25,15),
                         1,
                         AttributeModifierSlot.CHEST))
                         .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new RepulsiveShriekEffect()));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));

    }
}