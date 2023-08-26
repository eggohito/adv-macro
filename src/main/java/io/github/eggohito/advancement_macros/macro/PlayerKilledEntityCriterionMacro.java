package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlayerKilledEntityCriterionMacro extends OnKilledCriterionMacro {

    public static final Codec<OnKilledCriterionMacro> CODEC = getCodec(PlayerKilledEntityCriterionMacro::new);

    public PlayerKilledEntityCriterionMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(Criteria.PLAYER_KILLED_ENTITY.getId(), killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        super.writeToNbt(rootNbt, context);

        context.<Entity>ifPresent(KILLER_KEY_FIELD, killerEntity ->
            rootNbt.putString(getKillerKey(), killerEntity.getUuidAsString())
        );

        context.<Entity>ifPresent(VICTIM_KEY_FIELD, victimEntity ->
            rootNbt.putString(getVictimKey(), victimEntity.getUuidAsString())
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.PLAYER_KILLED_ENTITY.getId(), () -> CODEC);
    }

}
