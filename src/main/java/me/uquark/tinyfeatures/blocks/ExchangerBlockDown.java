package me.uquark.tinyfeatures.blocks;

import me.uquark.tinyfeatures.TinyFeatures;
import me.uquark.tinyfeatures.items.Galleon;
import me.uquark.tinyfeatures.items.Knut;
import me.uquark.tinyfeatures.items.Sickle;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExchangerBlockDown extends AbstractExchangerBlock {
    public static final String name = "exchanger_block_down";

    protected boolean exchange(ItemStack stack, PlayerEntity player) {
        packMoney(player);
        if (stack.getItem() instanceof Knut)
            return false;
        if (stack.getItem() instanceof Sickle) {
            stack.decrement(1);
            player.giveItemStack(new ItemStack(TinyFeatures.KNUT, 10));
            return true;
        }
        if (stack.getItem() instanceof Galleon) {
            stack.decrement(1);
            player.giveItemStack(new ItemStack(TinyFeatures.SICKLE, 10));
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (exchange(player.getMainHandStack(), player))
            return ActionResult.SUCCESS;
        else
            return ActionResult.FAIL;
    }
}
