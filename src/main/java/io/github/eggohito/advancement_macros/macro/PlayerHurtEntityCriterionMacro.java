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
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlayerHurtEntityCriterionMacro extends Macro {

    public static final String TARGET_KEY_FIELD = "target_key";
    public static final String DAMAGE_SOURCE_KEY_FIELD = "damage_source_key";
    public static final String DAMAGE_DEALT_AMOUNT_KEY_FIELD = "damage_dealt_amount_key";
    public static final String DAMAGE_ABSORBED_AMOUNT_KEY_FIELD = "damage_absorbed_amount_key";
    public static final String DAMAGE_BLOCKED_KEY_FIELD = "damage_blocked_key";

    public static final Codec<PlayerHurtEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(TARGET_KEY_FIELD, "target").forGetter(PlayerHurtEntityCriterionMacro::getHurtEntityKey),
        Codec.STRING.optionalFieldOf(DAMAGE_SOURCE_KEY_FIELD, "damage_source").forGetter(PlayerHurtEntityCriterionMacro::getDamageSourceKey),
        Codec.STRING.optionalFieldOf(DAMAGE_DEALT_AMOUNT_KEY_FIELD, "damage_dealt_amount").forGetter(PlayerHurtEntityCriterionMacro::getDamageDealtAmountKey),
        Codec.STRING.optionalFieldOf(DAMAGE_ABSORBED_AMOUNT_KEY_FIELD, "damage_absorbed_amount").forGetter(PlayerHurtEntityCriterionMacro::getDamageTakenAmountKey),
        Codec.STRING.optionalFieldOf(DAMAGE_BLOCKED_KEY_FIELD, "damage_blocked").forGetter(PlayerHurtEntityCriterionMacro::getDamageBlockedKey)
    ).apply(instance, PlayerHurtEntityCriterionMacro::new));

    private final String hurtEntityKey;
    private final String damageSourceKey;
    private final String damageDealtAmountKey;
    private final String damageTakenAmountKey;
    private final String damageBlockedKey;

    public PlayerHurtEntityCriterionMacro(String hurtEntityKey, String damageSourceKey, String damageDealtAmountKey, String damageTakenAmountKey, String damageBlockedKey) {
        super(Criteria.PLAYER_HURT_ENTITY.getId());
        this.hurtEntityKey = hurtEntityKey;
        this.damageSourceKey = damageSourceKey;
        this.damageDealtAmountKey = damageDealtAmountKey;
        this.damageTakenAmountKey = damageTakenAmountKey;
        this.damageBlockedKey = damageBlockedKey;
    }

    public String getHurtEntityKey() {
        return hurtEntityKey;
    }

    public String getDamageSourceKey() {
        return damageSourceKey;
    }

    public String getDamageDealtAmountKey() {
        return damageDealtAmountKey;
    }

    public String getDamageTakenAmountKey() {
        return damageTakenAmountKey;
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

        context.<Entity>ifPresent(TARGET_KEY_FIELD, targetEntity ->
            rootNbt.putString(hurtEntityKey, targetEntity.getUuidAsString())
        );

        context.<DamageSource>ifPresent(DAMAGE_SOURCE_KEY_FIELD, damageSource ->
            NbtUtil.writeDamageSourceToNbt(rootNbt, damageSourceKey, damageSource)
        );

        context.<Float>ifPresent(DAMAGE_DEALT_AMOUNT_KEY_FIELD, damageDealtAmount ->
            rootNbt.putFloat(damageDealtAmountKey, damageDealtAmount)
        );

        context.<Float>ifPresent(DAMAGE_ABSORBED_AMOUNT_KEY_FIELD, damageTakenAmount ->
            rootNbt.putFloat(damageTakenAmountKey, damageTakenAmount)
        );

        context.<Boolean>ifPresent(DAMAGE_BLOCKED_KEY_FIELD, damageBlocked ->
            rootNbt.putBoolean(damageBlockedKey, damageBlocked)
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.PLAYER_HURT_ENTITY.getId(), () -> CODEC);
    }

}
