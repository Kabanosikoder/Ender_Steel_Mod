package net.panther.endersteel.item;

import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.panther.endersteel.EnderSteel;

import java.util.EnumMap;

public final class EndSteelArmorMaterials {
    public static final TagKey<Item> ENDER_STEEL_REPAIR_TAG =
            TagKey.of(RegistryKeys.ITEM, Identifier.of(EnderSteel.MOD_ID, "ender_steel_repair"));

    public static final ArmorMaterial ENDER_STEEL_ARMOR_MATERIAL =
            new ArmorMaterial(
                    755,
                    Util.make(new EnumMap<>(EquipmentType.class), map -> {
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
                    Identifier.of(EnderSteel.MOD_ID, "ender_steel") // equipment model id
            );

    private EndSteelArmorMaterials() {}
}

