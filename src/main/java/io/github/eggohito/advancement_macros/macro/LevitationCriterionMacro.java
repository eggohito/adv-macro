package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

public class LevitationCriterionMacro extends Macro {

    public static final String START_LOCATION_KEY = "start_location";
    public static final String DURATION_KEY = "duration";

    public static final Codec<LevitationCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(START_LOCATION_KEY, START_LOCATION_KEY).forGetter(LevitationCriterionMacro::getStartLocationKey),
        Codec.STRING.optionalFieldOf(DURATION_KEY, DURATION_KEY).forGetter(LevitationCriterionMacro::getDurationKey)
    ).apply(instance, LevitationCriterionMacro::new));

    private final String startLocationKey;
    private final String durationKey;

    public LevitationCriterionMacro(String startLocationKey, String durationKey) {
        super(Criteria.LEVITATION);
        this.startLocationKey = startLocationKey;
        this.durationKey = durationKey;
    }

    public String getStartLocationKey() {
        return startLocationKey;
    }

    public String getDurationKey() {
        return durationKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Vec3d>ifPresent(START_LOCATION_KEY, startLocation ->
            NbtUtil.writeVec3dToNbt(rootNbt, startLocationKey, startLocation)
        );

        context.<Integer>ifPresent(DURATION_KEY, duration ->
            rootNbt.putInt(durationKey, duration)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.LEVITATION, () -> CODEC);
    }

}
