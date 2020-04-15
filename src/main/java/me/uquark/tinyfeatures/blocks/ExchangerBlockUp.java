package me.uquark.tinyfeatures.blocks;

import me.uquark.tinyfeatures.TinyFeatures;
import me.uquark.tinyfeatures.items.GalleonItem;
import me.uquark.tinyfeatures.items.KnutItem;
import me.uquark.tinyfeatures.items.SickleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ExchangerBlockUp extends AbstractExchangerBlock {
    public static final String name = "exchanger_block_up";

    @Override
    protected boolean exchange(ItemStack stack, PlayerEntity player) {
        packMoney(player);
        if (stack.getCount() < 10)
            return false;
        if (stack.getItem() instanceof KnutItem) {
            stack.decrement(10);
            player.giveItemStack(new ItemStack(TinyFeatures.SICKLE, 1));
            return true;
        }
        if (stack.getItem() instanceof SickleItem) {
            stack.decrement(10);
            player.giveItemStack(new ItemStack(TinyFeatures.GALLEON, 1));
            return true;
        }
        if (stack.getItem() instanceof GalleonItem)
            return false;
        return false;
    }
}