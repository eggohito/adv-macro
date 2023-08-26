package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ConsumeItemCriterionMacro extends Macro {

    public static final String CONSUMED_ITEM_KEY_FIELD = "consumed_item_key";
    public static final Codec<ConsumeItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(CONSUMED_ITEM_KEY_FIELD, "consumed_item").forGetter(ConsumeItemCriterionMacro::getConsumedItemKey)
    ).apply(instance, ConsumeItemCriterionMacro::new));

    private final String consumedItemKey;
    public ConsumeItemCriterionMacro(String consumedItemKey) {
        super(Criteria.CONSUME_ITEM.getId());
        this.consumedItemKey = consumedItemKey;
    }

    public String getConsumedItemKey() {
        return consumedItemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<ItemStack>ifPresent(CONSUMED_ITEM_KEY_FIELD, consumedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, consumedItemKey, consumedItemStack)
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.CONSUME_ITEM.getId(), () -> CODEC);
    }

}
