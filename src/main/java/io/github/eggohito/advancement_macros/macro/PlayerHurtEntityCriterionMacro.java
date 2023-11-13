package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class PlayerHurtEntityCriterionMacro extends Macro {

    public static final String ENTITY_KEY = "entity";
    public static final String DAMAGE_TYPE_KEY = "damage_type";
    public static final String DAMAGE_DEALT_KEY = "damage_dealt";
    public static final String DAMAGE_TAKEN_KEY = "damage_taken";
    public static final String DAMAGE_BLOCKED_KEY = "damage_blocked";

    public static final Codec<PlayerHurtEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(ENTITY_KEY, ENTITY_KEY).forGetter(PlayerHurtEntityCriterionMacro::getEntityKey),
        Codec.STRING.optionalFieldOf(DAMAGE_TYPE_KEY, DAMAGE_TYPE_KEY).forGetter(PlayerHurtEntityCriterionMacro::getDamageTypeKey),
        Codec.STRING.optionalFieldOf(DAMAGE_DEALT_KEY, DAMAGE_DEALT_KEY).forGetter(PlayerHurtEntityCriterionMacro::getDamageDealtKey),
        Codec.STRING.optionalFieldOf(DAMAGE_TAKEN_KEY, DAMAGE_TAKEN_KEY).forGetter(PlayerHurtEntityCriterionMacro::getDamageTakenKey),
        Codec.STRING.optionalFieldOf(DAMAGE_BLOCKED_KEY, DAMAGE_BLOCKED_KEY).forGetter(PlayerHurtEntityCriterionMacro::getDamageBlockedKey)
    ).apply(instance, PlayerHurtEntityCriterionMacro::new));

    private final String entityKey;
    private final String damageTypeKey;
    private final String damageDealtKey;
    private final String damageTakenKey;
    private final String damageBlockedKey;

    public PlayerHurtEntityCriterionMacro(String entityKey, String damageTypeKey, String damageDealtKey, String damageTakenKey, String damageBlockedKey) {
        super(Criteria.PLAYER_HURT_ENTITY);
        this.entityKey = entityKey;
        this.damageTypeKey = damageTypeKey;
        this.damageDealtKey = damageDealtKey;
        this.damageTakenKey = damageTakenKey;
        this.damageBlockedKey = damageBlockedKey;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public String getDamageTypeKey() {
        return damageTypeKey;
    }

    public String getDamageDealtKey() {
        return damageDealtKey;
    }

    public String getDamageTakenKey() {
        return damageTakenKey;
    }

    public String getDamageBlockedKey() {
        return damageBlockedKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Entity>ifPresent(ENTITY_KEY, targetEntity ->
            rootNbt.putString(entityKey, targetEntity.getUuidAsString())
        );

        context.<DamageSource>ifPresent(DAMAGE_TYPE_KEY, damageSource ->
            NbtUtil.writeDamageTypeToNbt(rootNbt, damageTypeKey, damageSource.getType())
        );

        context.<Float>ifPresent(DAMAGE_DEALT_KEY, damageDealt ->
            rootNbt.putFloat(damageDealtKey, damageDealt)
        );

        context.<Float>ifPresent(DAMAGE_TAKEN_KEY, damageTaken ->
            rootNbt.putFloat(damageTakenKey, damageTaken)
        );

        context.<Boolean>ifPresent(DAMAGE_BLOCKED_KEY, damageBlocked ->
            rootNbt.putBoolean(damageBlockedKey, damageBlocked)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.PLAYER_HURT_ENTITY, () -> CODEC);
    }

}
