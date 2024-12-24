package net.panther.endersteel.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class EnderSteelArmorItem extends ArmorItem {
    public EnderSteelArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.endersteel.armor.set_bonus").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("item.endersteel.armor.teleport_ability").formatted(Formatting.BLUE));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
