package me.uquark.tinyfeatures.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {
    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }

    private static boolean isWaterNearby(WorldView worldView, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = (BlockPos)var2.next();
        } while(!worldView.getFluidState(blockPos).matches(FluidTags.WATER));

        return true;
    }

    @Overwrite
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {
        if (!world.isClient && world.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
            // MY CODE
            if (!isWaterNearby(world, pos))
            // END OF MY CODE
                FarmlandBlock.setToDirt(world.getBlockState(pos), world, pos);
        }

        super.onLandedUpon(world, pos, entity, distance);
    }
}
