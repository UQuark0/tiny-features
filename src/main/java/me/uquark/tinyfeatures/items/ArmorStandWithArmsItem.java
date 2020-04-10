package me.uquark.tinyfeatures.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import java.util.List;

public class ArmorStandWithArmsItem extends Item {
    public static final String name = "armor_stand_with_arms";

    public ArmorStandWithArmsItem(Settings settings) {
        super(settings);
    }

    public ArmorStandWithArmsItem() {
        this(new Item.Settings().group(ItemGroup.DECORATIONS));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction direction = context.getSide();
        if (direction == Direction.DOWN) {
            return ActionResult.FAIL;
        } else {
            World world = context.getWorld();
            ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
            BlockPos blockPos = itemPlacementContext.getBlockPos();
            BlockPos blockPos2 = blockPos.up();
            if (itemPlacementContext.canPlace() && world.getBlockState(blockPos2).canReplace(itemPlacementContext)) {
                double d = (double)blockPos.getX();
                double e = (double)blockPos.getY();
                double f = (double)blockPos.getZ();
                List<Entity> list = world.getEntities((Entity)null, new Box(d, e, f, d + 1.0D, e + 2.0D, f + 1.0D));
                if (!list.isEmpty()) {
                    return ActionResult.FAIL;
                } else {
                    ItemStack itemStack = context.getStack();
                    if (!world.isClient) {
                        world.removeBlock(blockPos, false);
                        world.removeBlock(blockPos2, false);
                        ArmorStandEntity armorStandEntity = new ArmorStandEntity(world, d + 0.5D, e, f + 0.5D);
                        float g = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                        armorStandEntity.refreshPositionAndAngles(d + 0.5D, e, f + 0.5D, g, 0.0F);
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.putBoolean("ShowArms", true);
                        armorStandEntity.readCustomDataFromTag(compoundTag);
                        EntityType.loadFromEntityTag(world, context.getPlayer(), armorStandEntity, itemStack.getTag());
                        world.spawnEntity(armorStandEntity);
                        world.playSound((PlayerEntity)null, armorStandEntity.getX(), armorStandEntity.getY(), armorStandEntity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                    }

                    itemStack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            } else {
                return ActionResult.FAIL;
            }
        }
    }
}
