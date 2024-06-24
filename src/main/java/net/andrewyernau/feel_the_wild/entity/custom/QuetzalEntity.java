package net.andrewyernau.feel_the_wild.entity.custom;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.andrewyernau.feel_the_wild.entity.api.CustomMemoryTypes;
import net.andrewyernau.feel_the_wild.entity.api.behaviours.SleepDuringNight;
import net.andrewyernau.feel_the_wild.entity.api.sensors.NightTimeSensor;
import net.andrewyernau.feel_the_wild.entity.base.BaseAnimal;
import net.andrewyernau.feel_the_wild.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;


import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;

import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;

import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;

import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;


import java.util.Collections;
import java.util.List;
import java.util.Set;


public class QuetzalEntity extends BaseAnimal implements SmartBrainOwner<QuetzalEntity> {

    public static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(QuetzalEntity.class, EntityDataSerializers.BOOLEAN);


    public QuetzalEntity(EntityType<? extends BaseAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public List<ExtendedSensor<QuetzalEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<QuetzalEntity>(),// This tracks nearby entities
                new NightTimeSensor<QuetzalEntity>(),//This tracks the world time
                new InWaterSensor<>()//This checks if mob is in water
//                new HurtBySensor<>()             // This tracks the last damage source and attacker
        );
    }

    @Override
    public BrainActivityGroup<QuetzalEntity> getCoreTasks() { // These are the tasks that run all the time (usually)
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),                      // Have the entity turn to face and look at its current look target
                new MoveToWalkTarget<>(),
                new SleepDuringNight<>());                 // Walk towards the current walk target
    }

    @Override
    public BrainActivityGroup<QuetzalEntity> getIdleTasks() { // These are the tasks that run when the mob isn't doing anything else (usually)
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<QuetzalEntity>(      // Run only one of the below behaviours, trying each one in order. Include the generic type because JavaC is silly
                        new TargetOrRetaliate<>(),           // Set the attack target and walk target based on nearby entities
                        new SetPlayerLookTarget<>(),          // Set the look target for the nearest player
                        new SetRandomLookTarget<>()).startCondition(entity->!isSleeping(entity)),         // Set a random look target
                new OneRandomBehaviour<>(                 // Run a random task from the below options
                        new SetRandomWalkTarget<>().startCondition(entity->!isSleeping(entity)),    // Set a random walk target to a nearby position
                        new SetRandomFlyingTarget<>().startCondition(entity->!isSleeping(entity)),// Set a random flight target to a nearby position
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 200)))); // Do nothing for 1.5->10 seconds
    }


    @Override
    public SoundEvent getAmbient(Level pLevel, RandomSource pRandom) {
        if(!isSleeping(this))return ModSounds.QUETZAL_IDLE.get();
        else return null;
    }
    private boolean isSleeping(LivingEntity entity) {
        return BrainUtils.hasMemory(entity, CustomMemoryTypes.NIGHTTIME);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PARROT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getStepSound(BlockPos pos, BlockState blockState) {
        return null;
    }

    @Override
    protected Set<Item> getTemptItem() {
        return BREED_FOOD;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    static {
        BREED_FOOD = Sets.newHashSet(new Item[]{
                Items.WHEAT_SEEDS,
                Items.MELON_SEEDS,
                Items.PUMPKIN_SEEDS,
                Items.BEETROOT_SEEDS,
                Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_POD
        });
    }
    @Override
    public boolean isFlying(){
        return this.entityData.get(FLYING);
    }
}

