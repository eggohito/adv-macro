package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class ConstructBeaconCriterionMacro extends Macro {

    public static final String LEVEL_KEY = "level";

    public static Codec<ConstructBeaconCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(LEVEL_KEY, LEVEL_KEY).forGetter(ConstructBeaconCriterionMacro::getLevelKey)
    ).apply(instance, ConstructBeaconCriterionMacro::new));

    private final String levelKey;

    public ConstructBeaconCriterionMacro(String levelKey) {
        super(Criteria.CONSTRUCT_BEACON);
        this.levelKey = levelKey;
    }

    public String getLevelKey() {
        return levelKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Integer>ifPresent(LEVEL_KEY, beaconLevel ->
            rootNbt.putInt(levelKey, beaconLevel)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.CONSTRUCT_BEACON, () -> CODEC);
    }

}
