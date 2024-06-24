package net.andrewyernau.feel_the_wild.entity.base;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseAnimal extends Animal {
    private final HashMap<String, Integer> animationStates = new HashMap<>(1);
    public static Set<Item> BREED_FOOD;
    private int itemPositionList;

    public BaseAnimal(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    protected void registerGoals() {
//        goalSelector.addGoal(0, new FloatGoal(this));
//        goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
//        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
//        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
//
//        if (isBreedable()) {
//            goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
//            goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
//
//            if (getTemptItem() != null)
//                goalSelector.addGoal(3, new TemptGoal(this, 1.25D,Ingredient.of((ItemLike) getTemptItem().get(itemPositionList)) , false));
//        }
//        we are not using a goal system
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
        return false;
    };

}
