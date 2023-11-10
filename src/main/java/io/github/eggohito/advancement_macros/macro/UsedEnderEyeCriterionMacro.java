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

    public static final String STRONGHOLD_LOCATION_KEY_FIELD = "stronghold_location_key";

    public static final Codec<UsedEnderEyeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(STRONGHOLD_LOCATION_KEY_FIELD, "stronghold_location").forGetter(UsedEnderEyeCriterionMacro::getStrongholdLocationKey)
    ).apply(instance, UsedEnderEyeCriterionMacro::new));

    private final String strongholdLocationKey;

    public UsedEnderEyeCriterionMacro(String strongholdLocationKey) {
        super(Criteria.USED_ENDER_EYE);
        this.strongholdLocationKey = strongholdLocationKey;
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
        context.<BlockPos>ifPresent(STRONGHOLD_LOCATION_KEY_FIELD, strongholdLocation ->
            NbtUtil.writeBlockPosToNbt(rootNbt, strongholdLocationKey, strongholdLocation)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.USED_ENDER_EYE, () -> CODEC);
    }

}
