package me.uquark.tinyfeatures.items;

import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WrenchItem extends ToolItem {
    public static final String name = "wrench";

    public WrenchItem(Settings settings) {
        super(ToolMaterials.IRON, settings);
    }

    public WrenchItem() {
        this(new Settings().group(ItemGroup.TOOLS).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction direction = context.getSide();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (player != null) {
            if (block instanceof PistonBlock)
                if (world.getBlockState(blockPos).get(PistonBlock.EXTENDED))
                    return ActionResult.FAIL;
            if (block instanceof BedBlock)
                return ActionResult.FAIL;

            if (block instanceof FacingBlock) {
                if (!world.isClient) {
                    if (player.isSneaking())
                        world.setBlockState(blockPos, blockState.with(FacingBlock.FACING, direction.getOpposite()));
                    else
                        world.setBlockState(blockPos, blockState.with(FacingBlock.FACING, direction));
                    if (!player.isCreative())
                        stack.damage(2, player, playerEntity -> {
                            playerEntity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                        });
                }

                return ActionResult.SUCCESS;
            }

            if (block instanceof HorizontalFacingBlock) {
                if (direction == Direction.UP || direction == Direction.DOWN)
                    return ActionResult.FAIL;

                if (!world.isClient) {
                    if (player.isSneaking())
                        world.setBlockState(blockPos, blockState.with(HorizontalFacingBlock.FACING, direction.getOpposite()));
                    else
                        world.setBlockState(blockPos, blockState.with(HorizontalFacingBlock.FACING, direction));
                    if (!player.isCreative())
                        stack.damage(2, player, playerEntity -> {
                            playerEntity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                        });
                }

                return ActionResult.SUCCESS;
            }

            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }
}
