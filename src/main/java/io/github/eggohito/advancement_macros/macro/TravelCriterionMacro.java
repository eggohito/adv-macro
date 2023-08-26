package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public abstract class TravelCriterionMacro extends Macro {

    public static final String START_LOCATION_KEY_FIELD = "start_location_key";
    private final String startLocationKey;

    public TravelCriterionMacro(Identifier id, String startLocationKey) {
        super(id);
        this.startLocationKey = startLocationKey;
    }

    protected static Codec<TravelCriterionMacro> getCodec(Function<String, TravelCriterionMacro> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf(START_LOCATION_KEY_FIELD, "start_location").forGetter(TravelCriterionMacro::getStartLocationKey)
        ).apply(instance, macroFunction));
    }

    public String getStartLocationKey() {
        return startLocationKey;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Vec3d>ifPresent(START_LOCATION_KEY_FIELD, startLocation ->
            NbtUtil.writeVec3dToNbt(rootNbt, startLocationKey, startLocation)
        );
    }

}
