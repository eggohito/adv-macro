package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class EnterBlockCriterionMacro extends Macro {

    public static final String BLOCK_KEY = "block";
    public static final String STATE_KEY = "state";

    public static final Codec<EnterBlockCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(BLOCK_KEY, BLOCK_KEY).forGetter(EnterBlockCriterionMacro::getBlockKey),
        Codec.STRING.optionalFieldOf(STATE_KEY, STATE_KEY).forGetter(EnterBlockCriterionMacro::getStateKey)
    ).apply(instance, EnterBlockCriterionMacro::new));

    private final String blockKey;
    private final String stateKey;

    public EnterBlockCriterionMacro(String blockKey, String stateKey) {
        super(Criteria.ENTER_BLOCK);
        this.blockKey = blockKey;
        this.stateKey = stateKey;
    }

    public String getBlockKey() {
        return blockKey;
    }

    public String getStateKey() {
        return stateKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Identifier>ifPresent(BLOCK_KEY, blockId ->
            rootNbt.putString(blockKey, blockId.toString())
        );

        context.<BlockState>ifPresent(STATE_KEY, blockState ->
            NbtUtil.writeBlockStateToNbt(rootNbt, stateKey, blockState)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ENTER_BLOCK, () -> CODEC);
    }

}
