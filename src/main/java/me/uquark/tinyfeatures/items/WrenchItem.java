package me.uquark.tinyfeatures.items;

import me.uquark.tinyfeatures.TinyFeatures;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
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

    private static boolean isLegalPropertyValue(DirectionProperty property, Direction value) {
        if (property == Properties.FACING)
            return true;
        if (property == Properties.HORIZONTAL_FACING) {
            return value != Direction.UP && value != Direction.DOWN;
        }
        if (property == Properties.HOPPER_FACING) {
            return value != Direction.UP;
        }
        return false;
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

            DirectionProperty directionProperty = null;

            for (Property<?> property : blockState.getProperties())
                if (property instanceof DirectionProperty) {
                    directionProperty = (DirectionProperty) property;
                    break;
                }

            if (directionProperty == null)
                return ActionResult.FAIL;

            Direction newBlockDirection;
            if (player.isSneaking())
                newBlockDirection = direction.getOpposite();
            else
                newBlockDirection = direction;

            if (isLegalPropertyValue(directionProperty, newBlockDirection)) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState.with(directionProperty, newBlockDirection));
                    success(player, stack, blockPos, world);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }
}
