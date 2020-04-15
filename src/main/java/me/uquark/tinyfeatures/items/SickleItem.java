package me.uquark.tinyfeatures.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class SickleItem extends Item {
    public static final String name = "sickle";

    public SickleItem() {
        super(new Settings().group(ItemGroup.MISC).maxCount(64));
    }
}
