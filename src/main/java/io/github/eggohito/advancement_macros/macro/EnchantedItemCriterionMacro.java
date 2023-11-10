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

public class EnchantedItemCriterionMacro extends Macro {

    public static final String ENCHANTED_ITEM_KEY_FIELD = "enchanted_item_key";
    public static final String SPENT_LEVELS_KEY = "spent_levels_key";

    public static final Codec<EnchantedItemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(ENCHANTED_ITEM_KEY_FIELD, "enchanted_item").forGetter(EnchantedItemCriterionMacro::getEnchantedItemKey),
        Codec.STRING.optionalFieldOf(SPENT_LEVELS_KEY, "spent_levels").forGetter(EnchantedItemCriterionMacro::getSpentLevelsKey)
    ).apply(instance, EnchantedItemCriterionMacro::new));

    private final String enchantedItemKey;
    private final String spentLevelsKey;

    public EnchantedItemCriterionMacro(String enchantedItemKey, String spentLevelsKey) {
        super(Criteria.ENCHANTED_ITEM);
        this.enchantedItemKey = enchantedItemKey;
        this.spentLevelsKey = spentLevelsKey;
    }

    public String getEnchantedItemKey() {
        return enchantedItemKey;
    }

    public String getSpentLevelsKey() {
        return spentLevelsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(ENCHANTED_ITEM_KEY_FIELD, enchantedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, enchantedItemKey, enchantedItemStack)
        );

        context.<Integer>ifPresent(SPENT_LEVELS_KEY, spentLevels ->
            rootNbt.putInt(spentLevelsKey, spentLevels)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ENCHANTED_ITEM, () -> CODEC);
    }

}
