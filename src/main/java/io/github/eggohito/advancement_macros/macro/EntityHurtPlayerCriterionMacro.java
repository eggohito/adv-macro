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

public class EntityHurtPlayerCriterionMacro extends Macro {

    public static final String DAMAGE_SOURCE_ENTITY_KEY = "damage_source_entity";
    public static final String DAMAGE_TYPE_KEY = "damage_type";
    public static final String DAMAGE_DEALT_KEY = "damage_dealt";
    public static final String DAMAGE_TAKEN_KEY = "damage_taken";
    public static final String DAMAGE_BLOCKED_KEY = "damage_blocked";

    public static final Codec<EntityHurtPlayerCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(DAMAGE_SOURCE_ENTITY_KEY, DAMAGE_SOURCE_ENTITY_KEY).forGetter(EntityHurtPlayerCriterionMacro::getDamageSourceEntityKey),
        strictOptionalField(DAMAGE_TYPE_KEY, DAMAGE_TYPE_KEY).forGetter(EntityHurtPlayerCriterionMacro::getDamageTypeKey),
        strictOptionalField(DAMAGE_DEALT_KEY, DAMAGE_DEALT_KEY).forGetter(EntityHurtPlayerCriterionMacro::getDamageDealtKey),
        strictOptionalField(DAMAGE_TAKEN_KEY, DAMAGE_TAKEN_KEY).forGetter(EntityHurtPlayerCriterionMacro::getDamageTakenKey),
        strictOptionalField(DAMAGE_BLOCKED_KEY, DAMAGE_BLOCKED_KEY).forGetter(EntityHurtPlayerCriterionMacro::getDamageBlockedKey)
    ).apply(instance, EntityHurtPlayerCriterionMacro::new));

    private final String damageSourceEntityKey;
    private final String damageTypeKey;
    private final String damageDealtKey;
    private final String damageTakenKey;
    private final String damageBlockedKey;

    public EntityHurtPlayerCriterionMacro(String damageSourceEntityKey, String damageTypeKey, String damageDealtKey, String damageTakenKey, String damageBlockedKey) {
        this.damageSourceEntityKey = damageSourceEntityKey;
        this.damageTypeKey = damageTypeKey;
        this.damageDealtKey = damageDealtKey;
        this.damageTakenKey = damageTakenKey;
        this.damageBlockedKey = damageBlockedKey;
    }

    public String getDamageSourceEntityKey() {
        return damageSourceEntityKey;
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

        context.<Entity>ifPresent(DAMAGE_SOURCE_ENTITY_KEY, attackerEntity ->
            rootNbt.putString(damageSourceEntityKey, attackerEntity.getUuidAsString())
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
        return () -> new Pair<>(Criteria.ENTITY_HURT_PLAYER, () -> CODEC);
    }

}
