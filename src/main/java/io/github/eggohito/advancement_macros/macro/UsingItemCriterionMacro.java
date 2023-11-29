package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class UsingItemCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";

    public static final Codec<UsingItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(UsingItemCriterionMacro::getItemKey)
    ).apply(instance, UsingItemCriterionMacro::new));

    private final String itemKey;

    public UsingItemCriterionMacro(String itemKey) {
        this.itemKey = itemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(ITEM_KEY, itemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, itemStack)
        );

    }

    public String getItemKey() {
        return itemKey;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.USING_ITEM, () -> CODEC);
    }

}
