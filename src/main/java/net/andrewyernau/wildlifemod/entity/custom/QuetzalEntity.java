package net.andrewyernau.wildlifemod.entity.custom;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.andrewyernau.wildlifemod.entity.ModEntities;
import net.andrewyernau.wildlifemod.entity.ai.AscendToHeightAI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import java.util.Set;
import java.util.function.IntFunction;

public class QuetzalEntity extends Animal {

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    public static final TagKey<Block> QUETZAL_SPAWNABLE_ON = create("quetzal_spawnable_on");
    private static TagKey<Block> create(String pName) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(pName));
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID;
    private static final Item POISONOUS_FOOD;
    private static final Set<Item> BREED_FOOD;



    public QuetzalEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this,1.15D));
        this.goalSelector.addGoal(2, new TemptGoal(this,1.2D, Ingredient.of(Tags.Items.SEEDS_BEETROOT),false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this,1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this,1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class,6f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2,new AscendToHeightAI(this,1.0D,1));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,5D)
                .add(Attributes.FOLLOW_RANGE,24D)
                .add(Attributes.MOVEMENT_SPEED,0.20D)
                .add(Attributes.FLYING_SPEED,0.5D);

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) { //Breeding mechanic
        return ModEntities.QUETZAL.get().create(serverLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        //Check all breeding possibilities
        return BREED_FOOD.contains(pStack.getItem());
    }


    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.6F;
    }

    public static boolean checkQuetzalSpawnRules(EntityType<QuetzalEntity> pQuetzal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(QUETZAL_SPAWNABLE_ON) && isBrightEnoughToSpawn(pLevel, pPos);
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    public boolean isPushable() {
        return true;
    }

    protected void doPush(Entity pEntity) {
        if (!(pEntity instanceof Player)) {
            super.doPush(pEntity);
        }

    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    public QuetzalEntity.Variant getVariant() {
        return QuetzalEntity.Variant.byId((Integer) this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariant(QuetzalEntity.Variant pVariant) {
        this.entityData.set(DATA_VARIANT_ID, pVariant.id);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, 0);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant().id);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(QuetzalEntity.Variant.byId(pCompound.getInt("Variant")));
    }

    /**
     * Method to configure the flying sound of the Quetzal.
     * This method sets the sound that will play when the Quetzal is flying.
     * @return The flying sound of the Quetzal.
     */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    public static SoundEvent getAmbient(Level pLevel, RandomSource pRandom) {

            return SoundEvents.PARROT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return super.getHurtSound(pDamageSource);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }
    protected void playFlySound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    public float getVoicePitch() {
        return getPitch(this.random);
    }

    public static float getPitch(RandomSource pRandom) {
        return (pRandom.nextFloat() - pRandom.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    /**
     * Methods to register the AI behaviors of the Quetzal.
     * This method registers the artificial intelligence (AI) behaviors that the Quetzal will follow during the game,
     * such as searching for food and avoiding obstacles.
     */
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }

        this.flap += this.flapping * 2.0F;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double)(0.5F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    protected Vector3f getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pScale) {
        return new Vector3f(0.0F, pDimensions.height - 0.4375F * pScale, 0.0F);
    }

    static {
        DATA_VARIANT_ID = SynchedEntityData.defineId(QuetzalEntity.class, EntityDataSerializers.INT);
        POISONOUS_FOOD = Items.COOKIE;
        BREED_FOOD = Sets.newHashSet(new Item[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});
    }

    public static enum Variant implements StringRepresentable {
        RED_BLUE(0, "red_blue"),
        BLUE(1, "blue"),
        GREEN(2, "green"),
        YELLOW_BLUE(3, "yellow_blue"),
        GRAY(4, "gray");

        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(QuetzalEntity.Variant::values);
        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(QuetzalEntity.Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
        final int id;
        private final String name;

        private Variant(int pId, String pName) {
            this.id = pId;
            this.name = pName;
        }

        public int getId() {
            return this.id;
        }

        public static QuetzalEntity.Variant byId(int pId) {
            return (Variant)BY_ID.apply(pId);
        }

        public String getSerializedName() {
            return this.name;
        }
    }

}

