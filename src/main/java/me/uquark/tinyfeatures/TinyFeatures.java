package me.uquark.tinyfeatures;

import me.uquark.tinyfeatures.items.ArmorStandWithArmsItem;
import me.uquark.tinyfeatures.items.ScrambledEggsItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TinyFeatures implements ModInitializer {
    public static final String modid = "tinyfeatures";

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(modid, ScrambledEggsItem.name), new ScrambledEggsItem());
        Registry.register(Registry.ITEM, new Identifier(modid, ArmorStandWithArmsItem.name), new ArmorStandWithArmsItem());
    }

}
