package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ConstructBeaconCriterionMacro extends Macro {

    public static final String BEACON_LEVEL_KEY_FIELD = "beacon_level_key";
    public static Codec<ConstructBeaconCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(BEACON_LEVEL_KEY_FIELD, "beacon_level").forGetter(ConstructBeaconCriterionMacro::getBeaconLevelKey)
    ).apply(instance, ConstructBeaconCriterionMacro::new));

    private final String beaconLevelKey;
    public ConstructBeaconCriterionMacro(String beaconLevelKey) {
        super(Criteria.CONSTRUCT_BEACON.getId());
        this.beaconLevelKey = beaconLevelKey;
    }

    public String getBeaconLevelKey() {
        return beaconLevelKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Integer>ifPresent(BEACON_LEVEL_KEY_FIELD, beaconLevel ->
            rootNbt.putInt(beaconLevelKey, beaconLevel)
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.CONSTRUCT_BEACON.getId(), () -> CODEC);
    }

}
