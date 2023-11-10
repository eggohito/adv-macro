package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class ImpossibleCriterionMacro extends Macro {

    public static final Codec<ImpossibleCriterionMacro> CODEC = createEmptyCodec(ImpossibleCriterionMacro::new);

    public ImpossibleCriterionMacro() {
        super(Criteria.IMPOSSIBLE);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.IMPOSSIBLE, () -> CODEC);
    }

}
