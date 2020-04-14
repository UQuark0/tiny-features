package me.uquark.tinyfeatures.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Galleon extends Item {
    public static final String name = "galleon";

    public Galleon() {
        super(new Settings().group(ItemGroup.MISC).maxCount(64));
    }
}
