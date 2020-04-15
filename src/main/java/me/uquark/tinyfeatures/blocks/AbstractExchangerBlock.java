package me.uquark.tinyfeatures.blocks;

import me.uquark.tinyfeatures.TinyFeatures;
import me.uquark.tinyfeatures.items.GalleonItem;
import me.uquark.tinyfeatures.items.KnutItem;
import me.uquark.tinyfeatures.items.SickleItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractExchangerBlock extends HorizontalFacingBlock {
    public BlockItem blockItem;

    public AbstractExchangerBlock() {
        super(Settings.of(Material.METAL).strength(-1, 3600000));
        blockItem = new BlockItem(this, new Item.Settings().group(ItemGroup.MISC));
    }

    protected void packMoney(PlayerEntity player) {
        int galleons = 0, sickles = 0, knuts = 0;
        for (int i = 0; i <= 45; i++) {
            ItemStack itemStack = player.inventory.getInvStack(i);
            if (player.getMainHandStack() == itemStack)
                continue;
            if (itemStack.getItem() instanceof GalleonItem) {
                galleons += itemStack.getCount();
                itemStack.decrement(itemStack.getCount());
            }
            if (itemStack.getItem() instanceof SickleItem) {
                sickles += itemStack.getCount();
                itemStack.decrement(itemStack.getCount());
            }
            if (itemStack.getItem() instanceof KnutItem) {
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

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (exchange(player.getMainHandStack(), player))
            return ActionResult.SUCCESS;
        else
            return ActionResult.FAIL;
    }
}
