package me.uquark.tinyfeatures.mixins;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    private BlockPos spawnPosition;
    @Shadow
    private boolean spawnForced;
    @Shadow
    private int sleepTimer;

    public void setPlayerSpawn(BlockPos blockPos, boolean bl, boolean bl2) {
        if (blockPos != null) {
            if (bl2 && !blockPos.equals(this.spawnPosition)) {
                this.sendMessage(new TranslatableText("block.minecraft.bed.set_spawn"));
            }

            this.spawnPosition = blockPos;
            this.spawnForced = bl;
        } else {
            this.spawnPosition = null;
            this.spawnForced = false;
        }

    }

    private boolean isWithinSleepingRange(BlockPos sleepPos, Direction direction) {
        return this.method_24278(sleepPos) || this.method_24278(sleepPos.offset(direction.getOpposite()));
    }

    private boolean method_24278(BlockPos blockPos) {
        Vec3d vec3d = new Vec3d((double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D);
        return Math.abs(this.getX() - vec3d.getX()) <= 3.0D && Math.abs(this.getY() - vec3d.getY()) <= 2.0D && Math.abs(this.getZ() - vec3d.getZ()) <= 3.0D;
    }

    private boolean isBedObstructed(BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.up();
        return !this.doesNotSuffocate(blockPos) || !this.doesNotSuffocate(blockPos.offset(direction.getOpposite()));
    }

    public boolean doesNotSuffocate(BlockPos pos) {
        return !this.world.getBlockState(pos).canSuffocate(this.world, pos);
    }

    public abstract boolean isCreative();

    public Either<PlayerEntity.SleepFailureReason, Unit> trySleep(BlockPos pos) {
        Direction direction = (Direction)this.world.getBlockState(pos).get(HorizontalFacingBlock.FACING);
        if (!this.world.isClient) {
            if (this.isSleeping() || !this.isAlive()) {
                return Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM);
            }

            if (!this.world.dimension.hasVisibleSky()) {
                return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE);
            }

            if (this.world.isDay()) {
                if (isInSneakingPose())
                    this.setPlayerSpawn(pos, false, true);
                return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW);
            }

            if (!this.isWithinSleepingRange(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.TOO_FAR_AWAY);
            }

            if (this.isBedObstructed(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.OBSTRUCTED);
            }

            if (!this.isCreative()) {
                double d = 8.0D;
                double e = 5.0D;
                Vec3d vec3d = new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
                List<HostileEntity> list = this.world.getEntities(HostileEntity.class, new Box(vec3d.getX() - 8.0D, vec3d.getY() - 5.0D, vec3d.getZ() - 8.0D, vec3d.getX() + 8.0D, vec3d.getY() + 5.0D, vec3d.getZ() + 8.0D), (hostileEntity) -> {
                    return true;
                });
                if (!list.isEmpty()) {
                    return Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE);
                }
            }
        }

        this.sleep(pos);
        this.sleepTimer = 0;
        if (this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).updatePlayersSleeping();
        }

        return Either.right(Unit.INSTANCE);
    }

    public void sleep(BlockPos pos) {
        this.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
        if (isInSneakingPose())
            this.setPlayerSpawn(pos, false, true);
        super.sleep(pos);
    }

    public void resetStat(Stat<?> stat) {}

    public boolean isInSneakingPose() {
        return getPose() == EntityPose.CROUCHING;
    }
}
