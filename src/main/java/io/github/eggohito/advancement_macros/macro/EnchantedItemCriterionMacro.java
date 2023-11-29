package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class EnchantedItemCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";
    public static final String LEVELS_KEY = "levels";

    public static final Codec<EnchantedItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(EnchantedItemCriterionMacro::getItemKey),
        strictOptionalField(LEVELS_KEY, LEVELS_KEY).forGetter(EnchantedItemCriterionMacro::getLevelsKey)
    ).apply(instance, EnchantedItemCriterionMacro::new));

    private final String itemKey;
    private final String levelsKey;

    public EnchantedItemCriterionMacro(String itemKey, String levelsKey) {
        this.itemKey = itemKey;
        this.levelsKey = levelsKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getLevelsKey() {
        return levelsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(ITEM_KEY, enchantedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, enchantedItemStack)
        );

        context.<Integer>ifPresent(LEVELS_KEY, spentLevels ->
            rootNbt.putInt(levelsKey, spentLevels)
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.ENCHANTED_ITEM, () -> CODEC);
    }

}
