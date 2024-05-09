package net.andrewyernau.wildlifemod.entity.custom;

import net.andrewyernau.wildlifemod.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class QuetzalEntity extends Animal {

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
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,5D)
                .add(Attributes.FOLLOW_RANGE,24D)
                .add(Attributes.MOVEMENT_SPEED,0.20)
                .add(Attributes.FLYING_SPEED,0.5);

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) { //Breeding mechanic
        return ModEntities.QUETZAL.get().create(serverLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Tags.Items.SEEDS_BEETROOT); //Implement other food to breed
    }


    //AI PARAMS


    //ALL GOALS SET UP

}

