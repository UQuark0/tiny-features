package me.uquark.tinyfeatures;

import me.uquark.tinyfeatures.commands.PistonBlockLimitCommand;
import me.uquark.tinyfeatures.items.ArmorStandWithArmsItem;
import me.uquark.tinyfeatures.items.ScrambledEggsItem;
import me.uquark.tinyfeatures.items.WrenchItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TinyFeatures implements ModInitializer {
    public static final String modid = "tinyfeatures";

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(modid, ScrambledEggsItem.name), new ScrambledEggsItem());
        Registry.register(Registry.ITEM, new Identifier(modid, ArmorStandWithArmsItem.name), new ArmorStandWithArmsItem());
        Registry.register(Registry.ITEM, new Identifier(modid, WrenchItem.name), new WrenchItem());

        Registry.register(Registry.SOUND_EVENT, WrenchItem.WRENCH_SOUND_ID, WrenchItem.WRENCH_SOUND_EVENT);

        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            PistonBlockLimitCommand.register(dispatcher);
        });
    }

}
