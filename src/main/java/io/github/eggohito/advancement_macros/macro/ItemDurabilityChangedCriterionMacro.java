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

public class ItemDurabilityChangedCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";
    public static final String DELTA_KEY = "delta";
    public static final String DURABILITY_KEY = "durability";

    public static final Codec<ItemDurabilityChangedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(ItemDurabilityChangedCriterionMacro::getItemKey),
        strictOptionalField(DELTA_KEY, DELTA_KEY).forGetter(ItemDurabilityChangedCriterionMacro::getDeltaKey),
        strictOptionalField(DURABILITY_KEY, DURABILITY_KEY).forGetter(ItemDurabilityChangedCriterionMacro::getDurabilityKey)
    ).apply(instance, ItemDurabilityChangedCriterionMacro::new));

    private final String itemKey;
    private final String deltaKey;
    private final String durabilityKey;

    public ItemDurabilityChangedCriterionMacro(String itemKey, String deltaKey, String durabilityKey) {
        this.itemKey = itemKey;
        this.deltaKey = deltaKey;
        this.durabilityKey = durabilityKey;
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

        context.<Integer>ifPresent(DELTA_KEY, delta ->
            rootNbt.putInt(deltaKey, delta)
        );

        context.<Integer>ifPresent(DURABILITY_KEY, durability ->
            rootNbt.putInt(durabilityKey, durability)
        );

    }

    public String getItemKey() {
        return itemKey;
    }

    public String getDeltaKey() {
        return deltaKey;
    }

    public String getDurabilityKey() {
        return durabilityKey;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ITEM_DURABILITY_CHANGED, () -> CODEC);
    }

}
