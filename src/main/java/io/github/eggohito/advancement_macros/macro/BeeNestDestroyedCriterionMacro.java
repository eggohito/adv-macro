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
import net.minecraft.util.Pair;

public class BeeNestDestroyedCriterionMacro extends Macro {

    public static final String BLOCK_KEY = "block";
    public static final String ITEM_KEY = "item";
    public static final String NUM_BEES_INSIDE_KEY = "num_bees_inside";

    public static final Codec<BeeNestDestroyedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(BLOCK_KEY, BLOCK_KEY).forGetter(BeeNestDestroyedCriterionMacro::getBlockKey),
        Codec.STRING.optionalFieldOf(ITEM_KEY, ITEM_KEY).forGetter(BeeNestDestroyedCriterionMacro::getItemKey),
        Codec.STRING.optionalFieldOf(NUM_BEES_INSIDE_KEY, NUM_BEES_INSIDE_KEY).forGetter(BeeNestDestroyedCriterionMacro::getNumBeesInsideKey)
    ).apply(instance, BeeNestDestroyedCriterionMacro::new));

    private final String blockKey;
    private final String itemKey;
    private final String numBeesInsideKey;

    public BeeNestDestroyedCriterionMacro(String blockKey, String itemKey, String numBeesInsideKey) {
        super(Criteria.BEE_NEST_DESTROYED);
        this.blockKey = blockKey;
        this.itemKey = itemKey;
        this.numBeesInsideKey = numBeesInsideKey;
    }

    public String getBlockKey() {
        return blockKey;
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
        return () -> new Pair<>(Criteria.BEE_NEST_DESTROYED, () -> CODEC);
    }

}
