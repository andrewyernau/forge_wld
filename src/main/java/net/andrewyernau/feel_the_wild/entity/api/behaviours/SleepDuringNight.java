package net.andrewyernau.feel_the_wild.entity.api.behaviours;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.andrewyernau.feel_the_wild.entity.api.CustomMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SleepDuringNight <E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(
            CustomMemoryTypes.NIGHTTIME,
            MemoryStatus.VALUE_PRESENT
    ));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return BrainUtils.hasMemory(entity, CustomMemoryTypes.NIGHTTIME);
    }

    @Override
    protected void start(E entity) {
        PathNavigation navigation = entity.getNavigation();
        if(navigation != null && !navigation.isDone()) entity.getNavigation().stop();
    }

    @Override
    protected void tick(E entity) {
        PathNavigation navigation = entity.getNavigation();
        if(navigation != null && !navigation.isDone()) entity.getNavigation().stop();
    }

    @Override
    protected void stop(E entity) {
        super.stop(entity);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return BrainUtils.hasMemory(entity, CustomMemoryTypes.NIGHTTIME);
    }
}
