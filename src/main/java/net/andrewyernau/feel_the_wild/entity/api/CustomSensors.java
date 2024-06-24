package net.andrewyernau.feel_the_wild.entity.api;

import net.andrewyernau.feel_the_wild.entity.api.sensors.NightTimeSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.tslat.smartbrainlib.SBLConstants;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.function.Supplier;

public class CustomSensors {

    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, "thewild");


    public static final Supplier<SensorType<NightTimeSensor<?>>> NIGHTTIME = () -> new SensorType<>(NightTimeSensor::new);

    public static void register() {
        SENSORS.register("nighttime", NIGHTTIME);
    }
}
