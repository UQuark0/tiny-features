package me.uquark.tinyfeatures.mixins;

import net.minecraft.block.*;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block {
    public CauldronBlockMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    public abstract void setLevel(World world, BlockPos pos, BlockState state, int level);

    @SuppressWarnings("deprecation")
    @Overwrite
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return ActionResult.PASS;
        } else {
            int i = (Integer)state.get(CauldronBlock.LEVEL);
            Item item = itemStack.getItem();
            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !world.isClient) {
                    if (!player.abilities.creativeMode) {
                        player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                    }

                    player.incrementStat(Stats.FILL_CAULDRON);
                    this.setLevel(world, pos, state, 3);
                    world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResult.SUCCESS;
            } else if (item == Items.BUCKET) {
                if (i == 3 && !world.isClient) {
                    if (!player.abilities.creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.insertStack(new ItemStack(Items.WATER_BUCKET))) {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    player.incrementStat(Stats.USE_CAULDRON);
                    this.setLevel(world, pos, state, 0);
                    world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResult.SUCCESS;
            } else {
                ItemStack itemStack4;
                if (item == Items.GLASS_BOTTLE) {
                    if (i > 0 && !world.isClient) {
                        if (!player.abilities.creativeMode) {
                            itemStack4 = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                            player.incrementStat(Stats.USE_CAULDRON);
                            itemStack.decrement(1);
                            if (itemStack.isEmpty()) {
                                player.setStackInHand(hand, itemStack4);
                            } else if (!player.inventory.insertStack(itemStack4)) {
                                player.dropItem(itemStack4, false);
                            } else if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)player).openContainer(player.playerContainer);
                            }
                        }

                        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        this.setLevel(world, pos, state, i - 1);
                    }

                    return ActionResult.SUCCESS;
                } else if (item == Items.POTION && PotionUtil.getPotion(itemStack) == Potions.WATER) {
                    if (i < 3 && !world.isClient) {
                        if (!player.abilities.creativeMode) {
                            itemStack4 = new ItemStack(Items.GLASS_BOTTLE);
                            player.incrementStat(Stats.USE_CAULDRON);
                            player.setStackInHand(hand, itemStack4);
                            if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)player).openContainer(player.playerContainer);
                            }
                        }

                        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        this.setLevel(world, pos, state, i + 1);
                    }

                    return ActionResult.SUCCESS;
                } else {
                    if (i > 0 && item instanceof DyeableItem) {
                        DyeableItem dyeableItem = (DyeableItem)item;
                        if (dyeableItem.hasColor(itemStack) && !world.isClient) {
                            dyeableItem.removeColor(itemStack);
                            this.setLevel(world, pos, state, i - 1);
                            player.incrementStat(Stats.CLEAN_ARMOR);
                            return ActionResult.SUCCESS;
                        }
                    }

                    if (i > 0 && item instanceof BannerItem) {
                        if (BannerBlockEntity.getPatternCount(itemStack) > 0 && !world.isClient) {
                            itemStack4 = itemStack.copy();
                            itemStack4.setCount(1);
                            BannerBlockEntity.loadFromItemStack(itemStack4);
                            player.incrementStat(Stats.CLEAN_BANNER);
                            if (!player.abilities.creativeMode) {
                                itemStack.decrement(1);
                                this.setLevel(world, pos, state, i - 1);
                            }

                            if (itemStack.isEmpty()) {
                                player.setStackInHand(hand, itemStack4);
                            } else if (!player.inventory.insertStack(itemStack4)) {
                                player.dropItem(itemStack4, false);
                            } else if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)player).openContainer(player.playerContainer);
                            }
                        }

                        return ActionResult.SUCCESS;
                    } else if (i > 0 && item instanceof BlockItem) {
                        Block block = ((BlockItem)item).getBlock();
                        if (block instanceof ShulkerBoxBlock && !world.isClient()) {
                            ItemStack itemStack5 = new ItemStack(Blocks.SHULKER_BOX, 1);
                            if (itemStack.hasTag()) {
                                assert itemStack.getTag() != null;
                                itemStack5.setTag(itemStack.getTag().copy());
                            }

                            player.setStackInHand(hand, itemStack5);
                            this.setLevel(world, pos, state, i - 1);
                            player.incrementStat(Stats.CLEAN_SHULKER_BOX);
                            return ActionResult.SUCCESS;
                        }
                        // MY CODE
                        else if (block.asItem().getTranslationKey().contains("wool") && !block.asItem().getTranslationKey().equals("white_wool")) {
                            ItemStack newWoolStack = new ItemStack(Blocks.WHITE_WOOL, 1);
                            if (itemStack.hasTag()) {
                                assert itemStack.getTag() != null;
                                newWoolStack.setTag(itemStack.getTag().copy());
                            }

                            player.giveItemStack(newWoolStack);
                            itemStack.decrement(1);
                            this.setLevel(world, pos, state, i - 1);
                            return ActionResult.SUCCESS;
                        }
                        else if (block.asItem().getTranslationKey().contains("carpet") && !block.asItem().getTranslationKey().equals("white_carpet")) {
                            ItemStack newCarpetStack = new ItemStack(Blocks.WHITE_CARPET, 1);
                            if (itemStack.hasTag()) {
                                assert itemStack.getTag() != null;
                                newCarpetStack.setTag(itemStack.getTag().copy());
                            }

                            player.giveItemStack(newCarpetStack);
                            itemStack.decrement(1);
                            this.setLevel(world, pos, state, i - 1);
                            return ActionResult.SUCCESS;
                        }
                        // END OF MY CODE
                        else {
                            return ActionResult.CONSUME;
                        }
                    } else {
                        return ActionResult.PASS;
                    }
                }
            }
        }
    }
}
