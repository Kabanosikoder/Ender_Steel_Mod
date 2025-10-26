package net.panther.endersteel.item;

import net.minecraft.item.ToolMaterial;

import static net.panther.endersteel.datagen.ModBlockTagProvider.INCORRECT_FOR_ENDER_STEEL_TOOL;
import static net.panther.endersteel.util.ModTags.Items.ENDER_STEEL_REPAIR;


public class EndSteelToolMaterial {

    public static ToolMaterial ENDER_STEEL = new ToolMaterial(INCORRECT_FOR_ENDER_STEEL_TOOL,
            2532, 10.45F, 4.5F, 17, ENDER_STEEL_REPAIR);
}
