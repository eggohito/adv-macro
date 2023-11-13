package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Pair;

import java.util.Collection;

public class KilledByCrossbowCriterionMacro extends Macro {

    public static final String UNIQUE_ENTITY_TYPES_KEY = "unique_entity_types";
    public static final String VICTIMS_KEY = "victims";

    public static final Codec<KilledByCrossbowCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(UNIQUE_ENTITY_TYPES_KEY, UNIQUE_ENTITY_TYPES_KEY).forGetter(KilledByCrossbowCriterionMacro::getUniqueEntityTypesKey),
        Codec.STRING.optionalFieldOf(VICTIMS_KEY, VICTIMS_KEY).forGetter(KilledByCrossbowCriterionMacro::getVictimsKey)
    ).apply(instance, KilledByCrossbowCriterionMacro::new));

    private final String uniqueEntityTypesKey;
    private final String victimsKey;

    public KilledByCrossbowCriterionMacro(String uniqueEntityTypesKey, String victimsKey) {
        super(Criteria.KILLED_BY_CROSSBOW);
        this.uniqueEntityTypesKey = uniqueEntityTypesKey;
        this.victimsKey = victimsKey;
    }

    public String getUniqueEntityTypesKey() {
        return uniqueEntityTypesKey;
    }

    public String getVictimsKey() {
        return victimsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Integer>ifPresent(UNIQUE_ENTITY_TYPES_KEY, uniqueEntityTypes ->
            rootNbt.putInt(uniqueEntityTypesKey, uniqueEntityTypes)
        );

        context.<Collection<Entity>>ifPresent(VICTIMS_KEY, victims -> {

            NbtList victimsNbt = new NbtList();
            for (Entity piercedKilledEntity : victims) {
                victimsNbt.add(NbtString.of(piercedKilledEntity.getUuidAsString()));
            }

            rootNbt.put(victimsKey, victimsNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.KILLED_BY_CROSSBOW, () -> CODEC);
    }

}
