package me.uquark.tinyfeatures.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class GalleonItem extends Item {
    public static final String name = "galleon";

    public GalleonItem() {
        super(new Settings().group(ItemGroup.MISC).maxCount(64));
    }
}
