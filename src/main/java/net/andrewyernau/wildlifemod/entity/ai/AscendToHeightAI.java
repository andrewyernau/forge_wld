package net.andrewyernau.wildlifemod.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static net.minecraft.util.Mth.RAD_TO_DEG;

public class AscendToHeightAI extends Goal {
    /**
     * This behavior is responsible for elevating the mob to a desired height along the Y-axis.
     *
     * <p><strong>Objective:</strong> Elevate the mob to a specific height.</p>
     *
     * <p><strong>Responsibilities:</strong></p>
     * <ul>
     *   <li>Initiate mob ascent when certain conditions are met.</li>
     *   <li>Stop ascent once the mob has reached the desired height.</li>
     *   <li>Switch to flying behavior upon completion of ascent.</li>
     * </ul>
     * <p>
     * For isAscending boolean, determines if the behavior can be used at the current time.
     * <p><strong>Responsibilities:</strong></p>
     * <ul>
     *   <li>Check if the mob has not yet completed ascent.</li>
     *   <li>Return {@code true} if the behavior can be used, and {@code false} otherwise.</li>
     * </ul>
     *
     * @return {@code true} if the behavior can be used, {@code false} otherwise.
     */
    private PathfinderMob mob;
    private double speedModifier;
    private int heightRange;
    private final RandomSource random;
    private Vec3 targetP;
    private boolean isAscending = false;


    public AscendToHeightAI(PathfinderMob mob, double speedModifier, int heightRange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.heightRange = heightRange;
        this.random = RandomSource.create();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Indicates whether the mob is currently ascending.
     *
     * <p><strong>Responsibilities:</strong></p>
     * <ul>
     *   <li>Maintain a record of the mob's ascent state.</li>
     *   <li>Update its value when the mob begins or completes ascent.</li>
     * </ul>
     */
    private double setHeightRange(int heightRange) {
        double mean;
        double standardDev;
        mean = switch (heightRange) {
            case 1 -> 7;
            case 2 -> 12;
            case 3 -> 20;
            default -> 10;
        };

        standardDev = mean * 0.1;
        double verticalSolution = mean + random.nextGaussian() * standardDev;

        return Mth.clamp(verticalSolution, -64, 320);//set in range -64, 320
    }

    private boolean checkObstacles(Vec3 pos) {
        BlockPos initialPosition = this.mob.blockPosition();
        int initialX = initialPosition.getX();
        int initialY = initialPosition.getY();
        int initialZ = initialPosition.getZ();

        int endX = Mth.floor(pos.x);
        int endY = Mth.floor(pos.y);
        int endZ = Mth.floor(pos.z);

        for (int i = initialY; i <= endY; i++) {
            for (int j = initialX; j <= endX; j++) {
                for (int k = initialZ; k <= endZ; k++) {
                    if (!this.mob.level().isEmptyBlock(new BlockPos(j, i, k))) {
                        return false; //when the loop stops returning a false state, means there is an obstacle.
                    }
                }
            }
        }
        return true; //path is clear
    }

    @Override
    public void tick() {
        Vec3 vectorAtoB = new Vec3(this.targetP.x - this.mob.getX(), this.targetP.y - this.mob.getY(), this.targetP.z - this.mob.getZ());
        double vectorialDistance = vectorAtoB.length();

        if (vectorialDistance < this.mob.getBoundingBox().getSize())
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
        else {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vectorAtoB.scale(this.speedModifier * 0.05D / vectorialDistance)));

            double d = this.targetP.y - this.mob.getY();
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, this.mob.getSpeed() * Mth.clamp(d, -1, 1) * 0.6F, 0.0D));

            Vec3 vec3 = this.mob.getDeltaMovement();
            this.mob.setYRot(-((float) Mth.atan2(vec3.z, vec3.x)) * RAD_TO_DEG);
            this.mob.yBodyRot = this.mob.getYRot();
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetP.x, this.targetP.y, this.targetP.z, this.speedModifier);
    }

    @Override
    public void stop() {
        super.stop();
        isAscending = false;

    }

    /**
     * Called when the behavior is stopped.
     *
     * <p><strong>Responsibilities:</strong></p>
     * <ul>
     *   <li>Mark ascent action as completed.</li>
     *   <li>Switch to flying behavior once ascent is finished.</li>
     * </ul>
     */

    @Override
    public boolean canUse() {
        if (!isAscending) {
            BlockPos pos = this.mob.blockPosition();

            double targetX = pos.getX() + (random.nextDouble() * 20 - 10);
            double targetZ = pos.getZ() + (random.nextDouble() * 20 - 10);
            double targetY = setHeightRange(heightRange) + pos.getY();

            this.targetP = new Vec3(targetX, targetY, targetZ);
            isAscending=true;
            return this.mob.getY() < targetP.y && checkObstacles(this.targetP);

        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getY() < this.targetP.y && !this.mob.getNavigation().isDone();
    }
}
