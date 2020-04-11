package me.uquark.tinyfeatures.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Npc;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.Trader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractTraderEntity.class)
public abstract class AbstractTraderEntityMixin extends PassiveEntity implements Npc, Trader {
    protected AbstractTraderEntityMixin(EntityType<? extends PassiveEntity> type, World world) {
        super(type, world);
    }

    @Overwrite
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }
}
