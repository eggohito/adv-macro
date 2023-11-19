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

public class FilledBucketCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";

    public static final Codec<FilledBucketCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(FilledBucketCriterionMacro::getItemKey)
    ).apply(instance, FilledBucketCriterionMacro::new));

    private final String itemKey;

    public FilledBucketCriterionMacro(String itemKey) {
        super(Criteria.FILLED_BUCKET);
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
        context.<ItemStack>ifPresent(ITEM_KEY, itemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, itemStack)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.FILLED_BUCKET, () -> CODEC);
    }

}
