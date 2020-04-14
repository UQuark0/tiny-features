package me.uquark.tinyfeatures;

import me.uquark.tinyfeatures.blocks.ExchangerBlockDown;
import me.uquark.tinyfeatures.blocks.ExchangerBlockUp;
import me.uquark.tinyfeatures.commands.PistonBlockLimitCommand;
import me.uquark.tinyfeatures.items.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TinyFeatures implements ModInitializer {
    public static final Item SCRAMBLED_EGGS = new ScrambledEggsItem();
    public static final Item ARMOR_STAND_WITH_ARMS = new ArmorStandWithArmsItem();
    public static final Item WRENCH = new WrenchItem();
    public static final Item GALLEON = new Galleon();
    public static final Item SICKLE = new Sickle();
    public static final Item KNUT = new Knut();

    public static final ExchangerBlockUp EXCHANGER_BLOCK_UP = new ExchangerBlockUp();
    public static final ExchangerBlockDown EXCHANGER_BLOCK_DOWN = new ExchangerBlockDown();

    public static final String modid = "tinyfeatures";

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(modid, ScrambledEggsItem.name), SCRAMBLED_EGGS);
        Registry.register(Registry.ITEM, new Identifier(modid, ArmorStandWithArmsItem.name), ARMOR_STAND_WITH_ARMS);
        Registry.register(Registry.ITEM, new Identifier(modid, WrenchItem.name), WRENCH);

        Registry.register(Registry.ITEM, new Identifier(modid, Galleon.name), GALLEON);
        Registry.register(Registry.ITEM, new Identifier(modid, Sickle.name), SICKLE);
        Registry.register(Registry.ITEM, new Identifier(modid, Knut.name), KNUT);

        Registry.register(Registry.BLOCK, new Identifier(modid, ExchangerBlockUp.name), EXCHANGER_BLOCK_UP);
        Registry.register(Registry.ITEM, new Identifier(modid, ExchangerBlockUp.name), EXCHANGER_BLOCK_UP.blockItem);

        Registry.register(Registry.BLOCK, new Identifier(modid, ExchangerBlockDown.name), EXCHANGER_BLOCK_DOWN);
        Registry.register(Registry.ITEM, new Identifier(modid, ExchangerBlockDown.name), EXCHANGER_BLOCK_DOWN.blockItem);

        Registry.register(Registry.SOUND_EVENT, WrenchItem.WRENCH_SOUND_ID, WrenchItem.WRENCH_SOUND_EVENT);

        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            PistonBlockLimitCommand.register(dispatcher);
        });
    }

}
