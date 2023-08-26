package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class FilledBucketCriterionMacro extends Macro {

    public static final String FILLED_BUCKET_ITEM_KEY_FIELD = "filled_bucket_item_key";
    public static final Codec<FilledBucketCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(FILLED_BUCKET_ITEM_KEY_FIELD, "filled_bucket_item").forGetter(FilledBucketCriterionMacro::getFilledBucketItemKey)
    ).apply(instance, FilledBucketCriterionMacro::new));

    private final String filledBucketItemKey;
    public FilledBucketCriterionMacro(String filledBucketItemKey) {
        super(Criteria.FILLED_BUCKET.getId());
        this.filledBucketItemKey = filledBucketItemKey;
    }

    public String getFilledBucketItemKey() {
        return filledBucketItemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.FILLED_BUCKET.getId(), () -> CODEC);
    }

}
