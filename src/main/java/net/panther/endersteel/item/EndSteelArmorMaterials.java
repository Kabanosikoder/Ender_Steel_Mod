package net.panther.endersteel.item;

import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.panther.endersteel.EnderSteel;

import java.util.EnumMap;

public final class EndSteelArmorMaterials {

    public static final Identifier ENDER_STEEL_EQUIPMENT_ID = Identifier.of(EnderSteel.MOD_ID, "ender_steel");

    public static final TagKey<Item> ENDER_STEEL_REPAIR_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of(EnderSteel.MOD_ID, "ender_steel_repair"));

    public static final RegistryEntry<ArmorMaterial> ENDER_STEEL_ARMOR_MATERIAL =
            register(new ArmorMaterial(755, Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 4);
                map.put(EquipmentType.LEGGINGS, 7);
                map.put(EquipmentType.CHESTPLATE, 9);
                map.put(EquipmentType.HELMET, 4);
                map.put(EquipmentType.BODY, 4);
            }),
                    20,
                    SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
                    2.25f,
                    0.35f,
                    ENDER_STEEL_REPAIR_TAG,
                    ENDER_STEEL_EQUIPMENT_ID // model id
            ));

    private static RegistryEntry<ArmorMaterial> register(ArmorMaterial material) {
        return Registry.registerReference(
                Registries.ARMOR_MATERIAL, // not working idk yet
                Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                material
        );
    }

    private EndSteelArmorMaterials() {}
}
