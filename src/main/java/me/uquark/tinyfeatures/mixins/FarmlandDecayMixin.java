package me.uquark.tinyfeatures.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Mixin(FarmlandBlock.class)
public class FarmlandDecayMixin {
    private static boolean isWaterNearby(WorldView worldView, BlockPos pos) {
        Iterator var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = (BlockPos)var2.next();
        } while(!worldView.getFluidState(blockPos).matches(FluidTags.WATER));

        return true;
    }

    @Inject(at = @At("HEAD"), method = "onLandedUpon", cancellable = true)
    private void onLandedUpon(World world, BlockPos pos, Entity entity, float distance, CallbackInfo info) {
        info.cancel();
        if (!world.isClient && !isWaterNearby(world, pos))
            if (world.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F)
                FarmlandBlock.setToDirt(world.getBlockState(pos), world, pos);
        entity.handleFallDamage(distance, 1.0F);
    }
}
