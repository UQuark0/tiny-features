package me.uquark.tinyfeatures.items;

import me.uquark.tinyfeatures.TinyFeatures;
import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WrenchItem extends ToolItem {
    public static final String name = "wrench";
    public static final Identifier WRENCH_SOUND_ID = new Identifier(TinyFeatures.modid, "wrench_sound");
    public static final SoundEvent WRENCH_SOUND_EVENT = new SoundEvent(WRENCH_SOUND_ID);

    public WrenchItem(Settings settings) {
        super(ToolMaterials.IRON, settings);
    }

    public WrenchItem() {
        this(new Settings().group(ItemGroup.TOOLS).maxCount(1));
    }

    private static void success(PlayerEntity player, ItemStack stack, BlockPos pos, World world) {
        if (world.isClient)
            return;

        if (!player.isCreative())
            stack.damage(2, player, playerEntity -> {
                playerEntity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });

        world.playSound(
                null,
                pos,
                WRENCH_SOUND_EVENT,
                SoundCategory.BLOCKS,
                1,
                world.getRandom().nextFloat() + 0.5f
        );
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
                }
                success(player, stack, blockPos, world);
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
                    success(player, stack, blockPos, world);
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
