package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ConsumeItemCriterionMacro extends Macro {

    public static final Codec<ConsumeItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("item_key", "item").forGetter(ConsumeItemCriterionMacro::getItemKey)
    ).apply(instance, ConsumeItemCriterionMacro::new));

    private final String itemKey;
    public ConsumeItemCriterionMacro(String itemKey) {
        super(Criteria.CONSUME_ITEM.getId());
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
    public void writeToNbt(NbtCompound rootNbt, Object object) {
        if (object instanceof ItemStack stack) {
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, stack);
        }
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.CONSUME_ITEM.getId(), () -> CODEC);
    }

}
