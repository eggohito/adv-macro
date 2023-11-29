package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.function.TriFunction;

public abstract class OnKilledCriterionMacro extends Macro {

    public static final String KILLER_KEY = "killer";
    public static final String VICTIM_KEY = "victim";
    public static final String KILLING_BLOW_KEY = "killing_blow";

    private final String killerKey;
    private final String victimKey;
    private final String killingBlowKey;

    public OnKilledCriterionMacro(String killerKey, String victimKey, String killingBlowKey) {
        this.killerKey = killerKey;
        this.victimKey = victimKey;
        this.killingBlowKey = killingBlowKey;
    }

    protected static <T extends OnKilledCriterionMacro> Codec<T> createCodec(TriFunction<String, String, String, T> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            strictOptionalField(KILLER_KEY, KILLER_KEY).forGetter(OnKilledCriterionMacro::getKillerKey),
            strictOptionalField(VICTIM_KEY, VICTIM_KEY).forGetter(OnKilledCriterionMacro::getVictimKey),
            strictOptionalField(KILLING_BLOW_KEY, KILLING_BLOW_KEY).forGetter(OnKilledCriterionMacro::getKillingBlowKey)
        ).apply(instance, macroFunction::apply));
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<DamageSource>ifPresent(KILLING_BLOW_KEY, killingBlowDmgSource ->
            NbtUtil.writeDamageTypeToNbt(rootNbt, killingBlowKey, killingBlowDmgSource.getType())
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
