package me.uquark.tinyfeatures.items;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ScrambledEggsItem extends Item {
    public static final String name = "scrambled_eggs";

    public ScrambledEggsItem() {
        super(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(6).build()));
    }
}
