package me.uquark.tinyfeatures.blocks;

import me.uquark.tinyfeatures.TinyFeatures;
import me.uquark.tinyfeatures.items.GalleonItem;
import me.uquark.tinyfeatures.items.KnutItem;
import me.uquark.tinyfeatures.items.SickleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ExchangerBlockDown extends AbstractExchangerBlock {
    public static final String name = "exchanger_block_down";

    protected boolean exchange(ItemStack stack, PlayerEntity player) {
        packMoney(player);
        if (stack.getItem() instanceof KnutItem)
            return false;
        if (stack.getItem() instanceof SickleItem) {
            stack.decrement(1);
            player.giveItemStack(new ItemStack(TinyFeatures.KNUT, 10));
            return true;
        }
        if (stack.getItem() instanceof GalleonItem) {
            stack.decrement(1);
            player.giveItemStack(new ItemStack(TinyFeatures.SICKLE, 10));
            return true;
        }
        return false;
    }
}
