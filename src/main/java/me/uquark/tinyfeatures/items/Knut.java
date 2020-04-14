package me.uquark.tinyfeatures.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Knut extends Item {
    public static final String name = "knut";

    public Knut() {
        super(new Settings().group(ItemGroup.MISC).maxCount(64));
    }
}
