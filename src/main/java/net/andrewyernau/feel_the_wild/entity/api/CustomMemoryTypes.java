package net.andrewyernau.feel_the_wild.entity.api;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.SBLConstants;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class CustomMemoryTypes  {

    public static final MemoryModuleType<Boolean> NIGHTTIME = register("nighttime", Codec.BOOL);

    private static <T> MemoryModuleType<T> register(String id, Codec<T> codec) {
        return new MemoryModuleType<>(Optional.of(codec)); // You might need to adapt this based on your setup
    }

}
