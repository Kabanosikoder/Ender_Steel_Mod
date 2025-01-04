package net.panther.endersteel.item.custom;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class BagelItem extends Item { // BAGELLLLL
    public BagelItem(Settings settings) {
        super(settings.food(new FoodComponent.Builder()
                .hunger(7)
                .saturationModifier(1.4f)
                .build()));
    }
}
