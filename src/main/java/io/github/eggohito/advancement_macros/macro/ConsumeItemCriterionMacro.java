package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ConsumeItemCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";

    public static final Codec<ConsumeItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(ConsumeItemCriterionMacro::getItemKey)
    ).apply(instance, ConsumeItemCriterionMacro::new));

    private final String itemKey;

    public ConsumeItemCriterionMacro(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<ItemStack>ifPresent(ITEM_KEY, consumedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, consumedItemStack)
        );
    }

    public static Factory getFactory() {
        return new Factory(Criteria.CONSUME_ITEM, () -> CODEC);
    }

}
