package net.panther.endersteel.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.datagen.ModItemTagProvider;
import net.panther.endersteel.enchantment.effect.GazingVoidEffect;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> VOID_STRIKE = of("void_strike");
    public static final RegistryKey<Enchantment> ENDER_STREAK = of("ender_streak");
    public static final RegistryKey<Enchantment> PHANTOM_HARVEST = of("phantom_harvest");
    public static final RegistryKey<Enchantment> REPULSIVE_SHRIEK = of("repulsive_shriek");

    public static final RegistryKey<Enchantment> GAZING_VOID =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, "gazing_void"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, GAZING_VOID, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
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
                        new GazingVoidEffect(new EnchantmentLevelBasedValue() {

                            @Override
                            public float getValue(int level) {
                                return 0;
                            }

                            @Override
                            public MapCodec<? extends EnchantmentLevelBasedValue> getCodec() {
                                return null;
                            }
                        }))
        );


    }

    private static RegistryKey<Enchantment> of(String path) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnderSteel.MOD_ID, path));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));

    }
}