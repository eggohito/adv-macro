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
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class BeeNestDestroyedCriterionMacro extends Macro {

    public static final String BROKEN_BEE_NEST_KEY_FIELD = "broken_bee_best_key";
    public static final String TOOL_ITEM_KEY_FIELD = "tool_item_key_field";
    public static final String BEE_COUNT_KEY_FIELD = "bee_count_key";

    public static final Codec<BeeNestDestroyedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(BROKEN_BEE_NEST_KEY_FIELD, "broken_bee_nest").forGetter(BeeNestDestroyedCriterionMacro::getBrokenBeeNestKey),
        Codec.STRING.optionalFieldOf(TOOL_ITEM_KEY_FIELD, "tool_item").forGetter(BeeNestDestroyedCriterionMacro::getToolItemKey),
        Codec.STRING.optionalFieldOf(BEE_COUNT_KEY_FIELD, "bee_count").forGetter(BeeNestDestroyedCriterionMacro::getBeeCountKey)
    ).apply(instance, BeeNestDestroyedCriterionMacro::new));

    private final String brokenBeeNestKey;
    private final String toolItemKey;
    private final String beeCountKey;

    public BeeNestDestroyedCriterionMacro(String brokenBeeNestKey, String toolItemKey, String beeCountKey) {
        super(Criteria.BEE_NEST_DESTROYED.getId());
        this.brokenBeeNestKey = brokenBeeNestKey;
        this.toolItemKey = toolItemKey;
        this.beeCountKey = beeCountKey;
    }

    public String getBrokenBeeNestKey() {
        return brokenBeeNestKey;
    }

    public String getToolItemKey() {
        return toolItemKey;
    }

    public String getBeeCountKey() {
        return beeCountKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<BlockState>ifPresent(BROKEN_BEE_NEST_KEY_FIELD, brokenBeeNestBlockState ->
            NbtUtil.writeBlockStateToNbt(rootNbt, brokenBeeNestKey, brokenBeeNestBlockState)
        );

        context.<ItemStack>ifPresent(TOOL_ITEM_KEY_FIELD, toolItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, toolItemKey, toolItemStack)
        );

        context.<Integer>ifPresent(BEE_COUNT_KEY_FIELD, beeCount ->
            rootNbt.putInt(beeCountKey, beeCount)
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.BEE_NEST_DESTROYED.getId(), () -> CODEC);
    }

}
