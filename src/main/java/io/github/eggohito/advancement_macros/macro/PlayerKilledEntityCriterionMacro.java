package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class PlayerKilledEntityCriterionMacro extends OnKilledCriterionMacro {

    public static final Codec<PlayerKilledEntityCriterionMacro> CODEC = createCodec(PlayerKilledEntityCriterionMacro::new);

    public PlayerKilledEntityCriterionMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(Criteria.PLAYER_KILLED_ENTITY, killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        super.writeToNbt(rootNbt, context);

        context.<Entity>ifPresent(KILLER_KEY, killerEntity ->
            rootNbt.putString(getKillerKey(), killerEntity.getUuidAsString())
        );

        context.<Entity>ifPresent(VICTIM_KEY, victimEntity ->
            rootNbt.putString(getVictimKey(), victimEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.PLAYER_KILLED_ENTITY, () -> CODEC);
    }

}
