package net.andrewyernau.feel_the_wild.entity.api.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.andrewyernau.feel_the_wild.entity.api.CustomMemoryTypes;
import net.andrewyernau.feel_the_wild.entity.api.CustomSensors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.object.FixedNearestVisibleLivingEntities;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.registry.SBLSensors;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NightTimeSensor <E extends LivingEntity> extends PredicateSensor<E, E> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);

    public NightTimeSensor() {
        super((entity2, entity) -> {
            return isNightTime(entity);
        });
    }
    private static boolean isNightTime(LivingEntity entity){
        Long dayTime = entity.level().getDayTime()% 24000L;
        return dayTime>= 13000L;
    }

    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    public SensorType<? extends ExtendedSensor<?>> type() {
        return CustomSensors.NIGHTTIME.get();
    }

    protected void doTick(ServerLevel level,E entity) {
        if (predicate().test(entity, entity)) { // Use the predicate we set in the constructor to check if entity is in lava
            BrainUtils.setMemory(entity, CustomMemoryTypes.NIGHTTIME, true); // Set the memory status if we're in lava
        }
        else {
            BrainUtils.clearMemory(entity, CustomMemoryTypes.NIGHTTIME); // Clear the memory if no longer applicable.
        }
    }
}
