package net.andrewyernau.wildlifemod.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;


import java.util.EnumSet;
/**
 * This class controls the flying behavior of a mob, making it move in a
 * specified direction and adjust if it encounters obstacles.
 */
public class FlyingInAirAI  extends Goal{
    private PathfinderMob mob;
    private double speedModifier;
    private final RandomSource random;
    private Vec3 targetP;
    private boolean isFlying;

    public FlyingInAirAI(PathfinderMob mob,double speedModifier){
        this.mob=mob;
        this.speedModifier=speedModifier;
        this.random = RandomSource.create();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.targetP =newRandomThetaDirection();

    }
    private Vec3 newRandomThetaDirection(){
        Vec3 vectorOld= this.mob.getDeltaMovement();

        double lastUX=vectorOld.x;
        double lastUZ=vectorOld.z;

        int theta=20;

        double alpha=Mth.atan2(lastUZ,lastUX);
        double r=Mth.clamp(random.nextGaussian(),-1,1);
        float beta=(float) (alpha + (r * theta * Mth.DEG_TO_RAD));

        return new Vec3(Mth.cos(beta), vectorOld.y,Mth.sin(beta));
    }
    private Vec3 pos(){
        double x=this.mob.getX();
        double y=this.mob.getY();
        double z=this.mob.getZ();

        Vec3 direction=newRandomThetaDirection();
        double distanceH= 10+random.nextGaussian()*3;

        double normalizedVx=direction.normalize().x;
        double normalizedVz=direction.normalize().z;

        double x2=x+normalizedVx*distanceH;
        double z2=z+normalizedVz*distanceH;
        return new Vec3(x2, y,z2);
    }

    private boolean checkObstacles(Vec3 direction) {
        BlockPos thisPos = this.mob.blockPosition();
        for (int i = 1; i <= 6; i++) {
            BlockPos nextPos=thisPos.offset((int)direction.scale(i).x,(int)direction.scale(i).y,(int)direction.scale(i).z);
            if (!this.mob.level().isEmptyBlock(nextPos)) {
                return false; //Returning false state meaning there is an obstacle on the path.
            }
        }
        return true; //path is clear
    }

    @Override
    public void tick() {
        double vectorialDistance = targetP.length();

        if (vectorialDistance < this.mob.getBoundingBox().getSize())
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
        else {
            Vec3 directionToTarget = targetP.subtract(this.mob.position()).normalize();
            Vec3 adjustedMovement = this.mob.getDeltaMovement().add(directionToTarget.scale(this.speedModifier * 0.05D));

            this.mob.setDeltaMovement(adjustedMovement);
            Vec3 vec3 = this.mob.getDeltaMovement();
            this.mob.setYRot(-((float) Mth.atan2(vec3.z, vec3.x)) * Mth.RAD_TO_DEG);
            this.mob.yBodyRot = this.mob.getYRot();
        }

    }

    @Override
    public boolean canUse() {
        if (!isFlying) {

            this.targetP = pos();

            return this.mob.getY() < targetP.y && checkObstacles(this.targetP);
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getY() < this.targetP.y && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetP.x, this.targetP.y, this.targetP.z, this.speedModifier);
    }

    @Override
    public void stop() {
        super.stop();
        isFlying = false;
    }
}
