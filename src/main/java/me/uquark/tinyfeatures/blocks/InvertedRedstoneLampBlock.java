package me.uquark.tinyfeatures.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InvertedRedstoneLampBlock extends RedstoneLampBlock {
    public static final String name = "inverted_redstone_lamp";
    public BlockItem blockItem;

    public InvertedRedstoneLampBlock() {
        super(FabricBlockSettings.of(Material.REDSTONE_LAMP).lightLevel(15).strength(0.3f, 0.3f).sounds(BlockSoundGroup.GLASS).build());
        blockItem = new BlockItem(this, new Item.Settings().group(ItemGroup.REDSTONE));
        setDefaultState(getDefaultState().with(LIT, true));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(LIT, !ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        if (!world.isClient) {
            boolean isLit = state.get(LIT);
            if (isLit == world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, state.cycle(LIT), 2);
            }
        }
    }
}
