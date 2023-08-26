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

public class SlideDownBlockCriterionMacro extends Macro {

    public static final String SLIDED_BLOCK_KEY_FIELD = "slided_block_key";
    public static final Codec<SlideDownBlockCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(SLIDED_BLOCK_KEY_FIELD, "slided_block").forGetter(SlideDownBlockCriterionMacro::getSlidedBlockKey)
    ).apply(instance, SlideDownBlockCriterionMacro::new));

    private final String slidedBlockKey;
    public SlideDownBlockCriterionMacro(String slidedBlockKey) {
        super(Criteria.SLIDE_DOWN_BLOCK.getId());
        this.slidedBlockKey = slidedBlockKey;
    }

    public String getSlidedBlockKey() {
        return slidedBlockKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<BlockState>ifPresent(SLIDED_BLOCK_KEY_FIELD, slidedBlockState ->
            NbtUtil.writeBlockStateToNbt(rootNbt, slidedBlockKey, slidedBlockState)
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.SLIDE_DOWN_BLOCK.getId(), () -> CODEC);
    }

}
