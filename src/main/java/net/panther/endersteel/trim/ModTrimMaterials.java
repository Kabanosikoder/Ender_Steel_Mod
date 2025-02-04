package net.panther.endersteel.trim;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.ModItems;

import java.util.Map;

public class ModTrimMaterials {
    public static final RegistryKey<ArmorTrimMaterial> ENDER_STEEL = RegistryKey.of(RegistryKeys.TRIM_MATERIAL,
            Identifier.of(EnderSteel.MOD_ID, "ender_steel"));

    public static void bootstrap(Registerable<ArmorTrimMaterial> registerable){
        register(registerable, ENDER_STEEL, Registries.ITEM.getEntry(ModItems.ENDER_STEEL_INGOT),
                Style.EMPTY.withColor(TextColor.parse("#312d75").getOrThrow()), 1.0f);

    }

    private static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> armorTrimKey,
                                 RegistryEntry<Item> item, Style style, float itemModelIndex){
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(armorTrimKey.getValue().getPath(), item, itemModelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", armorTrimKey.getValue())).fillStyle(style));

        registerable.register(armorTrimKey, trimMaterial);
    }
}
