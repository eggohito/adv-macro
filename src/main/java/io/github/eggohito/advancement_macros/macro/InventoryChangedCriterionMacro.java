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

public class InventoryChangedCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";
    public static final String FULL_SLOTS_KEY = "full_slots";
    public static final String OCCUPIED_SLOTS_KEY = "occupied_slots";
    public static final String EMPTY_SLOTS_KEY = "empty_slots";

    public static final Codec<InventoryChangedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(ITEM_KEY, ITEM_KEY).forGetter(InventoryChangedCriterionMacro::getItemKey),
        Codec.STRING.optionalFieldOf(FULL_SLOTS_KEY, FULL_SLOTS_KEY).forGetter(InventoryChangedCriterionMacro::getFullSlotsKey),
        Codec.STRING.optionalFieldOf(OCCUPIED_SLOTS_KEY, OCCUPIED_SLOTS_KEY).forGetter(InventoryChangedCriterionMacro::getOccupiedSlotsKey),
        Codec.STRING.optionalFieldOf(EMPTY_SLOTS_KEY, EMPTY_SLOTS_KEY).forGetter(InventoryChangedCriterionMacro::getEmptySlotsKey)
    ).apply(instance, InventoryChangedCriterionMacro::new));

    private final String itemKey;
    private final String fullSlotsKey;
    private final String occupiedSlotsKey;
    private final String emptySlotsKey;

    public InventoryChangedCriterionMacro(String itemKey, String fullSlotsKey, String occupiedSlotsKey, String emptySlotsKey) {
        super(Criteria.INVENTORY_CHANGED);
        this.itemKey = itemKey;
        this.fullSlotsKey = fullSlotsKey;
        this.occupiedSlotsKey = occupiedSlotsKey;
        this.emptySlotsKey = emptySlotsKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getFullSlotsKey() {
        return fullSlotsKey;
    }

    public String getOccupiedSlotsKey() {
        return occupiedSlotsKey;
    }

    public String getEmptySlotsKey() {
        return emptySlotsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(ITEM_KEY, movedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, movedItemStack)
        );

        context.<Integer>ifPresent(FULL_SLOTS_KEY, fullItems ->
            rootNbt.putInt(fullSlotsKey, fullItems)
        );

        context.<Integer>ifPresent(EMPTY_SLOTS_KEY, emptySlots ->
            rootNbt.putInt(emptySlotsKey, emptySlots)
        );

        context.<Integer>ifPresent(OCCUPIED_SLOTS_KEY, occupiedSlots ->
            rootNbt.putInt(occupiedSlotsKey, occupiedSlots)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.INVENTORY_CHANGED, () -> CODEC);
    }

}
