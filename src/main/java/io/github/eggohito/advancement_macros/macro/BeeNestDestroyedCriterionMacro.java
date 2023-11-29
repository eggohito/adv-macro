package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class BeeNestDestroyedCriterionMacro extends Macro {

    public static final String BLOCK_KEY = "block";
    public static final String STATE_KEY = "state";
    public static final String ITEM_KEY = "item";
    public static final String NUM_BEES_INSIDE_KEY = "num_bees_inside";

    public static final Codec<BeeNestDestroyedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(BLOCK_KEY, BLOCK_KEY).forGetter(BeeNestDestroyedCriterionMacro::getBlockKey),
        strictOptionalField(STATE_KEY, STATE_KEY).forGetter(BeeNestDestroyedCriterionMacro::getStateKey),
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(BeeNestDestroyedCriterionMacro::getItemKey),
        strictOptionalField(NUM_BEES_INSIDE_KEY, NUM_BEES_INSIDE_KEY).forGetter(BeeNestDestroyedCriterionMacro::getNumBeesInsideKey)
    ).apply(instance, BeeNestDestroyedCriterionMacro::new));

    private final String blockKey;
    private final String stateKey;
    private final String itemKey;
    private final String numBeesInsideKey;

    public BeeNestDestroyedCriterionMacro(String blockKey, String stateKey, String itemKey, String numBeesInsideKey) {
        
        this.blockKey = blockKey;
        this.stateKey = stateKey;
        this.itemKey = itemKey;
        this.numBeesInsideKey = numBeesInsideKey;
    }

    public String getBlockKey() {
        return blockKey;
    }

    public String getStateKey() {
        return stateKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getNumBeesInsideKey() {
        return numBeesInsideKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<BlockState>ifPresent(BLOCK_KEY, brokenBeeNestBlockState ->
            NbtUtil.writeBlockStateToNbt(rootNbt, blockKey, brokenBeeNestBlockState)
        );

        context.<ItemStack>ifPresent(ITEM_KEY, toolItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, toolItemStack)
        );

        context.<Integer>ifPresent(NUM_BEES_INSIDE_KEY, beeCount ->
            rootNbt.putInt(numBeesInsideKey, beeCount)
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.BEE_NEST_DESTROYED, () -> CODEC);
    }

}
