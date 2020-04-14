package me.uquark.tinyfeatures.blocks;

import me.uquark.tinyfeatures.TinyFeatures;
import me.uquark.tinyfeatures.items.Galleon;
import me.uquark.tinyfeatures.items.Knut;
import me.uquark.tinyfeatures.items.Sickle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;

public abstract class AbstractExchangerBlock extends HorizontalFacingBlock {
    public BlockItem blockItem;

    public AbstractExchangerBlock() {
        super(Settings.of(Material.METAL));
        blockItem = new BlockItem(this, new Item.Settings().group(ItemGroup.MISC));
    }

    protected void packMoney(PlayerEntity player) {
        int galleons = 0, sickles = 0, knuts = 0;
        for (int i = 0; i <= 45; i++) {
            ItemStack itemStack = player.inventory.getInvStack(i);
            if (player.getMainHandStack() == itemStack)
                continue;
            if (itemStack.getItem() instanceof Galleon) {
                galleons += itemStack.getCount();
                itemStack.decrement(itemStack.getCount());
            }
            if (itemStack.getItem() instanceof Sickle) {
                sickles += itemStack.getCount();
                itemStack.decrement(itemStack.getCount());
            }
            if (itemStack.getItem() instanceof Knut) {
                knuts += itemStack.getCount();
                itemStack.decrement(itemStack.getCount());
            }
        }
        player.giveItemStack(new ItemStack(TinyFeatures.GALLEON, galleons));
        player.giveItemStack(new ItemStack(TinyFeatures.SICKLE, sickles));
        player.giveItemStack(new ItemStack(TinyFeatures.KNUT, knuts));
    }

    protected abstract boolean exchange(ItemStack stack, PlayerEntity player);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
}
