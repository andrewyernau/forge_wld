package net.andrewyernau.feel_the_wild.entity.base;

import com.google.common.collect.Sets;
import net.andrewyernau.feel_the_wild.entity.ai.FlyingLookRandomlyGoal;
import net.andrewyernau.feel_the_wild.entity.ai.RandomFlyingGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseFlyingAnimal extends Animal implements FlyingAnimal {
    private final HashMap<String, Integer> animationStates = new HashMap<>(1);
    public static Set<Item> BREED_FOOD;
    private int itemPositionList;

    public BaseFlyingAnimal(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new RandomFlyingGoal(this, true));
        goalSelector.addGoal(3, new FlyingLookRandomlyGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this,1f));
        goalSelector.addGoal(2,new BreedGoal(this,1));
        goalSelector.addGoal(4,new PanicGoal(this,1));
        goalSelector.addGoal(4,new TemptGoal(this,1,  Ingredient.of((ItemLike) BREED_FOOD),true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        xpReward = (int) (getAttributeValue(Attributes.MAX_HEALTH) / 25f);

        return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return getAmbient(this.level(), this.level().random);
    }

    public abstract SoundEvent getAmbient(Level pLevel, RandomSource pRandom);

    @Nullable
    protected abstract SoundEvent getDeathSound();

    @Nullable
    protected abstract SoundEvent getHurtSound(DamageSource damageSourceIn);

    @Nullable
    protected abstract SoundEvent getStepSound(BlockPos pos, BlockState blockState);

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        SoundEvent stepSound = getStepSound(pos, block);

        if (stepSound == null) {
            super.playStepSound(pos, block);
        } else {
            playSound(stepSound, 0.55f, 1.0F);
        }
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack != null && BREED_FOOD.contains(pStack.getItem());
    }

    protected boolean isBreedable() {
        return true;
    }

    @Nullable
    protected abstract Set<Item> getTemptItem();

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob partner) {
        return null;
    }

    static {
        BREED_FOOD = Sets.newHashSet(new Item[]{Items.WHEAT_SEEDS});
    }

    public boolean isFlying(){
        return this.onGround();
    }

    public boolean isWalking(){
        return this.getNavigation().getPath() != null;
    }

    public boolean isIdle(){
        return this.getNavigation().getPath() == null && !this.isFlying();
    }

    public abstract boolean isSleeping();

}
