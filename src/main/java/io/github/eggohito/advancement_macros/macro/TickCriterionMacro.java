package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class TickCriterionMacro extends Macro {

    public static final Codec<TickCriterionMacro> CODEC = createEmptyCodec(TickCriterionMacro::new);

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.TICK, () -> CODEC);
    }

}
