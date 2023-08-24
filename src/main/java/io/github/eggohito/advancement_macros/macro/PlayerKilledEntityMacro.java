package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlayerKilledEntityMacro extends OnKilledCriterionMacro {

    public static final Codec<PlayerKilledEntityMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("killer_key", "killer").forGetter(PlayerKilledEntityMacro::getKillerKey),
        Codec.STRING.optionalFieldOf("victim_key", "victim").forGetter(PlayerKilledEntityMacro::getVictimKey),
        Codec.STRING.optionalFieldOf("killing_blow_key", "killing_blow").forGetter(PlayerKilledEntityMacro::getKillingBlowKey)
    ).apply(instance, PlayerKilledEntityMacro::new));

    public PlayerKilledEntityMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(Criteria.PLAYER_KILLED_ENTITY.getId(), killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, Object object) {

        if (object instanceof ServerPlayerEntity player) {
             rootNbt.putString(getKillerKey(), player.getUuidAsString());
        } else if (object instanceof Entity entity) {
            rootNbt.putString(getVictimKey(), entity.getUuidAsString());
        }

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.PLAYER_KILLED_ENTITY.getId(), () -> CODEC);
    }

}
