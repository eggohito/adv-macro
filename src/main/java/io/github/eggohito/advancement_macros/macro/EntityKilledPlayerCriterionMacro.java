package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class EntityKilledPlayerCriterionMacro extends OnKilledCriterionMacro {

    public static final Codec<EntityKilledPlayerCriterionMacro> CODEC = createCodec(EntityKilledPlayerCriterionMacro::new);

    public EntityKilledPlayerCriterionMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        super.writeToNbt(rootNbt, context);

        //  Context of killer and victim is swapped here
        context.<Entity>ifPresent(KILLER_KEY, victimEntity ->
            rootNbt.putString(getVictimKey(), victimEntity.getUuidAsString())
        );

        context.<Entity>ifPresent(VICTIM_KEY, killerEntity ->
            rootNbt.putString(getKillerKey(), killerEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.ENTITY_KILLED_PLAYER, () -> CODEC);
    }

}
