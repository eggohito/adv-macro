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

public class UsedTotemCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";

    public static final Codec<UsedTotemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(UsedTotemCriterionMacro::getItemKey)
    ).apply(instance, UsedTotemCriterionMacro::new));

    private final String itemKey;

    public UsedTotemCriterionMacro(String itemKey) {
        super(Criteria.USED_TOTEM);
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
        context.<ItemStack>ifPresent(ITEM_KEY, usedTotemItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, usedTotemItemStack)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.USED_TOTEM, () -> CODEC);
    }

}
