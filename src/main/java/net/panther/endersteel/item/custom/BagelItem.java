package net.panther.endersteel.item.custom;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class BagelItem extends Item {
    public BagelItem(Settings settings) {
        super(settings.food(new FoodComponent.Builder()
                .hunger(4)         
                .saturationModifier(0.8f) 
                .build()));
    }
}
