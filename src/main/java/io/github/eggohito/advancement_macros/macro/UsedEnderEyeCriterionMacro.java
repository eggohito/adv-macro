package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class UsedEnderEyeCriterionMacro extends Macro {

    public static final String DISTANCE_KEY = "distance";
    public static final String LOCATION_KEY = "location";

    public static final Codec<UsedEnderEyeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(DISTANCE_KEY, DISTANCE_KEY).forGetter(UsedEnderEyeCriterionMacro::getDistanceKey),
        Codec.STRING.optionalFieldOf(LOCATION_KEY, LOCATION_KEY).forGetter(UsedEnderEyeCriterionMacro::getStrongholdLocationKey)
    ).apply(instance, UsedEnderEyeCriterionMacro::new));

    private final String distanceKey;
    private final String strongholdLocationKey;

    public UsedEnderEyeCriterionMacro(String distanceKey, String strongholdLocationKey) {
        super(Criteria.USED_ENDER_EYE);
        this.distanceKey = distanceKey;
        this.strongholdLocationKey = strongholdLocationKey;
    }

    public String getDistanceKey() {
        return distanceKey;
    }

    public String getStrongholdLocationKey() {
        return strongholdLocationKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Double>ifPresent(DISTANCE_KEY, distance ->
            rootNbt.putDouble(distanceKey, distance)
        );

        context.<BlockPos>ifPresent(LOCATION_KEY, strongholdLocation ->
            NbtUtil.writeBlockPosToNbt(rootNbt, strongholdLocationKey, strongholdLocation)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.USED_ENDER_EYE, () -> CODEC);
    }

}
