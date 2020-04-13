package me.uquark.tinyfeatures.mixins;

import com.google.common.collect.ImmutableMap;
import me.uquark.tinyfeatures.items.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.AbstractState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends AbstractState<Block, BlockState> implements State<BlockState> {
    protected BlockStateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries) {
        super(owner, entries);
    }

    @Shadow
    public abstract Block getBlock();

    @Overwrite
    public ActionResult onUse(World world, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockState blockState = world.getBlockState(hit.getBlockPos());
        if (player.getMainHandStack().getItem() instanceof WrenchItem)
            return ActionResult.PASS;
        return this.getBlock().onUse(blockState, world, hit.getBlockPos(), player, hand, hit);
    }
}
