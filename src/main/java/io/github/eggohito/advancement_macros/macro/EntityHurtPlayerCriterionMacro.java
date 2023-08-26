package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class EntityHurtPlayerCriterionMacro extends Macro {

    public static final String DAMAGE_SOURCE_KEY_FIELD = "damage_source_key";
    public static final String DAMAGE_DEALT_AMOUNT_KEY_FIELD = "damage_dealt_amount_key";
    public static final String DAMAGE_TAKEN_AMOUNT_KEY_FIELD = "damage_taken_amount_key";
    public static final String DAMAGE_BLOCKED_KEY_FIELD = "damage_blocked_key";

    public static final Codec<EntityHurtPlayerCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(DAMAGE_SOURCE_KEY_FIELD, "damage_source").forGetter(EntityHurtPlayerCriterionMacro::getDamageSourceKey),
        Codec.STRING.optionalFieldOf(DAMAGE_DEALT_AMOUNT_KEY_FIELD, "damage_dealt_amount").forGetter(EntityHurtPlayerCriterionMacro::getDamageDealtAmountKey),
        Codec.STRING.optionalFieldOf(DAMAGE_TAKEN_AMOUNT_KEY_FIELD, "damage_taken_amount").forGetter(EntityHurtPlayerCriterionMacro::getDamageTakenAmountKey),
        Codec.STRING.optionalFieldOf(DAMAGE_BLOCKED_KEY_FIELD, "damage_blocked").forGetter(EntityHurtPlayerCriterionMacro::getDamageBlockedKey)
    ).apply(instance, EntityHurtPlayerCriterionMacro::new));

    private final String damageSourceKey;
    private final String damageDealtAmountKey;
    private final String damageTakenAmountKey;
    private final String damageBlockedKey;

    public EntityHurtPlayerCriterionMacro(String damageSourceKey, String damageDealtAmountKey, String damageTakenAmountKey, String damageBlockedKey) {
        super(Criteria.ENTITY_HURT_PLAYER.getId());
        this.damageSourceKey = damageSourceKey;
        this.damageDealtAmountKey = damageDealtAmountKey;
        this.damageTakenAmountKey = damageTakenAmountKey;
        this.damageBlockedKey = damageBlockedKey;
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

        context.<DamageSource>ifPresent(DAMAGE_SOURCE_KEY_FIELD, damageSource ->
            NbtUtil.writeDamageSourceToNbt(rootNbt, damageSourceKey, damageSource)
        );

        context.<Float>ifPresent(DAMAGE_DEALT_AMOUNT_KEY_FIELD, damageDealtAmount ->
            rootNbt.putFloat(damageDealtAmountKey, damageDealtAmount)
        );

        context.<Float>ifPresent(DAMAGE_TAKEN_AMOUNT_KEY_FIELD, damageTakenAmount ->
            rootNbt.putFloat(damageTakenAmountKey, damageTakenAmount)
        );

        context.<Boolean>ifPresent(DAMAGE_BLOCKED_KEY_FIELD, damageBlocked ->
            rootNbt.putBoolean(damageBlockedKey, damageBlocked)
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.ENTITY_HURT_PLAYER.getId(), () -> CODEC);
    }

}
