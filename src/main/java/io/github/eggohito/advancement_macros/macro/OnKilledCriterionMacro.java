package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriFunction;

public abstract class OnKilledCriterionMacro extends Macro {

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
            Codec.STRING.optionalFieldOf("killer_key", "killer").forGetter(OnKilledCriterionMacro::getKillerKey),
            Codec.STRING.optionalFieldOf("victim_key", "victim").forGetter(OnKilledCriterionMacro::getVictimKey),
            Codec.STRING.optionalFieldOf("killing_blow_key", "killing_blow").forGetter(OnKilledCriterionMacro::getKillingBlowKey)
        ).apply(instance, macroFunction::apply));
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
