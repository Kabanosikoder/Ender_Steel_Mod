package net.panther.endersteel.trim;

import net.panther.endersteel.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import net.panther.endersteel.EnderSteel;

public class ModTrimPatterns {
    public static final RegistryKey<ArmorTrimPattern> ENDER_STEEL = RegistryKey.of(RegistryKeys.TRIM_PATTERN,
            Identifier.of(EnderSteel.MOD_ID, "endersteel"));

    public static void bootstrap(Registerable<ArmorTrimPattern> context) {
        register(context, ModItems.ENDER_STEEL_ARMOR_SMITHING_TEMPLATE, ENDER_STEEL);
    }

    private static void register(Registerable<ArmorTrimPattern> context, Item item, RegistryKey<ArmorTrimPattern> key) {
        ArmorTrimPattern trimPattern = new ArmorTrimPattern(key.getValue(), Registries.ITEM.getEntry(item),
                Text.translatable(Util.createTranslationKey("trim_pattern", key.getValue())), false);

        context.register(key, trimPattern);
    }
}
