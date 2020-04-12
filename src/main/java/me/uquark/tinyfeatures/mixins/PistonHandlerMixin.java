package me.uquark.tinyfeatures.mixins;

import me.uquark.tinyfeatures.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PistonHandler.class)
public abstract class PistonHandlerMixin {
    @Shadow
    private World world;
    @Shadow
    private BlockPos posFrom;
    @Shadow
    private Direction motionDirection;
    @Shadow
    private List<BlockPos> movedBlocks;
    @Shadow
    private List<BlockPos> brokenBlocks;

    private static boolean isBlockSticky(Block block) {
        return block == Blocks.SLIME_BLOCK || block == Blocks.HONEY_BLOCK;
    }

    private static boolean isAdjacentBlockStuck(Block block, Block block2) {
        if (block == Blocks.HONEY_BLOCK && block2 == Blocks.SLIME_BLOCK) {
            return false;
        } else if (block == Blocks.SLIME_BLOCK && block2 == Blocks.HONEY_BLOCK) {
            return false;
        } else {
            return isBlockSticky(block) || isBlockSticky(block2);
        }
    }

    @Shadow
    public abstract void setMovedBlocks(int from, int to);
    @Shadow
    public abstract boolean canMoveAdjacentBlock(BlockPos pos);

    @Overwrite
    private boolean tryMove(BlockPos pos, Direction dir) {
        BlockState blockState = this.world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (blockState.isAir()) {
            return true;
        } else if (!PistonBlock.isMovable(blockState, this.world, pos, this.motionDirection, false, dir)) {
            return true;
        } else if (pos.equals(this.posFrom)) {
            return true;
        } else if (this.movedBlocks.contains(pos)) {
            return true;
        } else {
            int i = 1;
            if (i + this.movedBlocks.size() > Config.PISTON_BLOCK_LIMIT) {
                return false;
            } else {
                while(isBlockSticky(block)) {
                    BlockPos blockPos = pos.offset(this.motionDirection.getOpposite(), i);
                    Block block2 = block;
                    blockState = this.world.getBlockState(blockPos);
                    block = blockState.getBlock();
                    if (blockState.isAir() || !isAdjacentBlockStuck(block2, block) || !PistonBlock.isMovable(blockState, this.world, blockPos, this.motionDirection, false, this.motionDirection.getOpposite()) || blockPos.equals(this.posFrom)) {
                        break;
                    }

                    ++i;
                    if (i + this.movedBlocks.size() > Config.PISTON_BLOCK_LIMIT) {
                        return false;
                    }
                }

                int j = 0;

                int l;
                for(l = i - 1; l >= 0; --l) {
                    this.movedBlocks.add(pos.offset(this.motionDirection.getOpposite(), l));
                    ++j;
                }

                l = 1;

                while(true) {
                    BlockPos blockPos2 = pos.offset(this.motionDirection, l);
                    int m = this.movedBlocks.indexOf(blockPos2);
                    if (m > -1) {
                        this.setMovedBlocks(j, m);

                        for(int n = 0; n <= m + j; ++n) {
                            BlockPos blockPos3 = (BlockPos)this.movedBlocks.get(n);
                            if (isBlockSticky(this.world.getBlockState(blockPos3).getBlock()) && !this.canMoveAdjacentBlock(blockPos3)) {
                                return false;
                            }
                        }

                        return true;
                    }

                    blockState = this.world.getBlockState(blockPos2);
                    if (blockState.isAir()) {
                        return true;
                    }

                    if (!PistonBlock.isMovable(blockState, this.world, blockPos2, this.motionDirection, true, this.motionDirection) || blockPos2.equals(this.posFrom)) {
                        return false;
                    }

                    if (blockState.getPistonBehavior() == PistonBehavior.DESTROY) {
                        this.brokenBlocks.add(blockPos2);
                        return true;
                    }

                    if (this.movedBlocks.size() >= Config.PISTON_BLOCK_LIMIT) {
                        return false;
                    }

                    this.movedBlocks.add(blockPos2);
                    ++j;
                    ++l;
                }
            }
        }
    }
}
