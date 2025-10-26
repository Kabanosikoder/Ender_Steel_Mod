package net.panther.endersteel.util;

import net.panther.endersteel.EnderSteel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(EnderSteel.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ENDER_STEEL_REPAIR = createTag();

        private static TagKey<Item> createTag() {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(EnderSteel.MOD_ID, "ender_steel_repair"));
        }
    }
}