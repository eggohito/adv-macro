package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

public class TargetHitCriterionMacro extends Macro {

    public static final String PROJECTILE_KEY_FIELD = "projectile_key";
    public static final String HIT_LOCATION_KEY_FIELD = "hit_location_key";
    public static final String SIGNAL_STRENGTH_KEY_FIELD = "signal_strength_key";

    public static final Codec<TargetHitCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(PROJECTILE_KEY_FIELD, "projectile").forGetter(TargetHitCriterionMacro::getProjectileKey),
        Codec.STRING.optionalFieldOf(HIT_LOCATION_KEY_FIELD, "hit_location").forGetter(TargetHitCriterionMacro::getHitLocationKey),
        Codec.STRING.optionalFieldOf(SIGNAL_STRENGTH_KEY_FIELD, "signal_strength").forGetter(TargetHitCriterionMacro::getSignalStrengthKey)
    ).apply(instance, TargetHitCriterionMacro::new));

    private final String projectileKey;
    private final String hitLocationKey;
    private final String signalStrengthKey;

    public TargetHitCriterionMacro(String projectileKey, String hitLocationKey, String signalStrengthKey) {
        super(Criteria.TARGET_HIT.getId());
        this.projectileKey = projectileKey;
        this.hitLocationKey = hitLocationKey;
        this.signalStrengthKey = signalStrengthKey;
    }

    public String getProjectileKey() {
        return projectileKey;
    }

    public String getHitLocationKey() {
        return hitLocationKey;
    }

    public String getSignalStrengthKey() {
        return signalStrengthKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Entity>ifPresent(PROJECTILE_KEY_FIELD, projectileEntity ->
            rootNbt.putString(projectileKey, projectileEntity.getUuidAsString())
        );

        context.<Vec3d>ifPresent(HIT_LOCATION_KEY_FIELD, hitLocation ->
            NbtUtil.writeVec3dToNbt(rootNbt, hitLocationKey, hitLocation)
        );

        context.<Integer>ifPresent(SIGNAL_STRENGTH_KEY_FIELD, signalStrength ->
            rootNbt.putInt(signalStrengthKey, signalStrength)
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.TARGET_HIT.getId(), () -> CODEC);
    }

}
