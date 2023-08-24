package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class EntityKilledPlayerMacro extends OnKilledCriterionMacro {

    public static final Codec<EntityKilledPlayerMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("killer_key", "killer").forGetter(EntityKilledPlayerMacro::getKillerKey),
        Codec.STRING.optionalFieldOf("victim_key", "victim").forGetter(EntityKilledPlayerMacro::getVictimKey),
        Codec.STRING.optionalFieldOf("killing_blow_key", "killing_blow").forGetter(EntityKilledPlayerMacro::getKillingBlowKey)
    ).apply(instance, EntityKilledPlayerMacro::new));

    public EntityKilledPlayerMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(Criteria.ENTITY_KILLED_PLAYER.getId(), killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, Object object) {

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.ENTITY_KILLED_PLAYER.getId(), () -> CODEC);
    }

}
