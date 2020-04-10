package me.uquark.tinyfeatures.mixins;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public class CauldronMixin {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            info.setReturnValue(ActionResult.PASS);
        }
        int i = (Integer)state.get(Properties.LEVEL_3);
        Item item = itemStack.getItem();
        if (i > 0 && item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block.getName().asString().contains("Wool") && !block.getName().asString().equals("White Wool") && !world.isClient()) {
                ItemStack itemStack5 = new ItemStack(Blocks.WHITE_WOOL, 1);
                if (itemStack.hasTag()) {
                    assert itemStack.getTag() != null;
                    itemStack5.setTag(itemStack.getTag().copy());
                }
                player.giveItemStack(itemStack5);
                itemStack.decrement(1);
                ((CauldronBlock) state.getBlock()).setLevel(world, pos, state, i - 1);
                info.setReturnValue(ActionResult.SUCCESS);
            } else if (block.getName().asString().contains("Carpet") && !block.getName().asString().equals("White Carpet") && !world.isClient()) {
                ItemStack itemStack5 = new ItemStack(Blocks.WHITE_CARPET, 1);
                if (itemStack.hasTag()) {
                    assert itemStack.getTag() != null;
                    itemStack5.setTag(itemStack.getTag().copy());
                }
                player.giveItemStack(itemStack5);
                itemStack.decrement(1);
                ((CauldronBlock) state.getBlock()).setLevel(world, pos, state, i - 1);
                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
