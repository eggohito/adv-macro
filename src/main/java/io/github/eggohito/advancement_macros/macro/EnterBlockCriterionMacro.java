package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class EnterBlockCriterionMacro extends Macro {

    public static final String ENTERED_BLOCK_KEY_FIELD = "entered_block_key";

    public static final Codec<EnterBlockCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(ENTERED_BLOCK_KEY_FIELD, "entered_block").forGetter(EnterBlockCriterionMacro::getEnteredBlockKey)
    ).apply(instance, EnterBlockCriterionMacro::new));

    private final String enteredBlockKey;

    public EnterBlockCriterionMacro(String enteredBlockKey) {
        super(Criteria.ENTER_BLOCK);
        this.enteredBlockKey = enteredBlockKey;
    }

    public String getEnteredBlockKey() {
        return enteredBlockKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<BlockState>ifPresent(ENTERED_BLOCK_KEY_FIELD, enteredBlockState ->
            NbtUtil.writeBlockStateToNbt(rootNbt, enteredBlockKey, enteredBlockState)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ENTER_BLOCK, () -> CODEC);
    }

}
