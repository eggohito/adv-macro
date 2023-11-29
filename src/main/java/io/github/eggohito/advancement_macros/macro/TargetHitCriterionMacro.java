package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class TargetHitCriterionMacro extends Macro {

    public static final String PROJECTILE_KEY = "projectile";
    public static final String HIT_LOCATION_KEY = "hit_location";
    public static final String SIGNAL_STRENGTH_KEY = "signal_strength";

    public static final Codec<TargetHitCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(PROJECTILE_KEY, PROJECTILE_KEY).forGetter(TargetHitCriterionMacro::getProjectileKey),
        strictOptionalField(HIT_LOCATION_KEY, HIT_LOCATION_KEY).forGetter(TargetHitCriterionMacro::getHitLocationKey),
        strictOptionalField(SIGNAL_STRENGTH_KEY, SIGNAL_STRENGTH_KEY).forGetter(TargetHitCriterionMacro::getSignalStrengthKey)
    ).apply(instance, TargetHitCriterionMacro::new));

    private final String projectileKey;
    private final String hitLocationKey;
    private final String signalStrengthKey;

    public TargetHitCriterionMacro(String projectileKey, String hitLocationKey, String signalStrengthKey) {
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

        context.<Entity>ifPresent(PROJECTILE_KEY, projectileEntity ->
            rootNbt.putString(projectileKey, projectileEntity.getUuidAsString())
        );

        context.<Vec3d>ifPresent(HIT_LOCATION_KEY, hitLocation ->
            NbtUtil.writeVec3dToNbt(rootNbt, hitLocationKey, hitLocation)
        );

        context.<Integer>ifPresent(SIGNAL_STRENGTH_KEY, signalStrength ->
            rootNbt.putInt(signalStrengthKey, signalStrength)
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.TARGET_HIT, () -> CODEC);
    }

}
