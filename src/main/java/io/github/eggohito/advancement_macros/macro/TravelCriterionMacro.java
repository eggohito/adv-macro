package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public abstract class TravelCriterionMacro extends Macro {

    public static final String START_LOCATION_KEY = "start_location";
    private final String startLocationKey;

    public TravelCriterionMacro(Criterion<?> baseCriterion, String startLocationKey) {
        super(baseCriterion);
        this.startLocationKey = startLocationKey;
    }

    protected static <T extends TravelCriterionMacro> Codec<T> getCodec(Function<String, T> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            strictOptionalField(START_LOCATION_KEY, START_LOCATION_KEY).forGetter(TravelCriterionMacro::getStartLocationKey)
        ).apply(instance, macroFunction));
    }

    public String getStartLocationKey() {
        return startLocationKey;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Vec3d>ifPresent(START_LOCATION_KEY, startLocation ->
            NbtUtil.writeVec3dToNbt(rootNbt, startLocationKey, startLocation)
        );
    }

}
