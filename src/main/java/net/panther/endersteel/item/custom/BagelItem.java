package net.panther.endersteel.item.custom;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;

public class BagelItem extends Item { // BAGELLLLL
    public static final FoodComponent BAGEL_FOOD_COMPONENT = new FoodComponent.Builder()
		.alwaysEdible()
		.nutrition(5)
        .saturationModifier(1)
		.build();

    public BagelItem(Settings settings) {
        super(settings);
    }
}
