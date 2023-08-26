package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriFunction;

public abstract class OnKilledCriterionMacro extends Macro {

    public static final String KILLER_KEY_FIELD = "killer_key";
    public static final String VICTIM_KEY_FIELD = "victim_key";
    public static final String KILLING_BLOW_KEY_FIELD = "killing_blow_key";

    private final String killerKey;
    private final String victimKey;
    private final String killingBlowKey;

    public OnKilledCriterionMacro(Identifier id, String killerKey, String victimKey, String killingBlowKey) {
        super(id);
        this.killerKey = killerKey;
        this.victimKey = victimKey;
        this.killingBlowKey = killingBlowKey;
    }

    protected static Codec<OnKilledCriterionMacro> getCodec(TriFunction<String, String, String, OnKilledCriterionMacro> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf(KILLER_KEY_FIELD, "killer").forGetter(OnKilledCriterionMacro::getKillerKey),
            Codec.STRING.optionalFieldOf(VICTIM_KEY_FIELD, "victim").forGetter(OnKilledCriterionMacro::getVictimKey),
            Codec.STRING.optionalFieldOf(KILLING_BLOW_KEY_FIELD, "killing_blow").forGetter(OnKilledCriterionMacro::getKillingBlowKey)
        ).apply(instance, macroFunction::apply));
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<DamageSource>ifPresent(KILLING_BLOW_KEY_FIELD, killingBlowDmgSource ->
            NbtUtil.writeDamageSourceToNbt(rootNbt, killingBlowKey, killingBlowDmgSource)
        );
    }

    public String getKillerKey() {
        return killerKey;
    }

    public String getVictimKey() {
        return victimKey;
    }

    public String getKillingBlowKey() {
        return killingBlowKey;
    }

}
