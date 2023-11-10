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

    public static final String PIERCED_KILLED_ENTITIES_KEY_FIELD = "pierced_killed_entities_key";

    public static final Codec<KilledByCrossbowCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(PIERCED_KILLED_ENTITIES_KEY_FIELD, "pierced_killed_entities").forGetter(KilledByCrossbowCriterionMacro::getPiercedKilledEntitiesKey)
    ).apply(instance, KilledByCrossbowCriterionMacro::new));

    private final String piercedKilledEntitiesKey;

    public KilledByCrossbowCriterionMacro(String piercedKilledEntitiesKey) {
        super(Criteria.KILLED_BY_CROSSBOW);
        this.piercedKilledEntitiesKey = piercedKilledEntitiesKey;
    }

    public String getPiercedKilledEntitiesKey() {
        return piercedKilledEntitiesKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Collection<Entity>>ifPresent(PIERCED_KILLED_ENTITIES_KEY_FIELD, piercedKilledEntities -> {

            NbtList piercedKilledEntitiesNbt = new NbtList();
            for (Entity piercedKilledEntity : piercedKilledEntities) {
                piercedKilledEntitiesNbt.add(NbtString.of(piercedKilledEntity.getUuidAsString()));
            }

            rootNbt.put(piercedKilledEntitiesKey, piercedKilledEntitiesNbt);

        });
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.KILLED_BY_CROSSBOW, () -> CODEC);
    }

}
