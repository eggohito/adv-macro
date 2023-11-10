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

    public static final String MOVED_ITEM_KEY_FIELD = "moved_item_key";
    public static final String FULL_ITEMS_KEY_FIELD = "full_items_key";
    public static final String OCCUPIED_SLOTS_KEY_FIELD = "occupied_slots_key";
    public static final String EMPTY_SLOTS_KEY_FIELD = "empty_slots_key";

    public static final Codec<InventoryChangedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(MOVED_ITEM_KEY_FIELD, "moved_item").forGetter(InventoryChangedCriterionMacro::getMovedItemKey),
        Codec.STRING.optionalFieldOf(FULL_ITEMS_KEY_FIELD, "full_items").forGetter(InventoryChangedCriterionMacro::getFullItemsKey),
        Codec.STRING.optionalFieldOf(OCCUPIED_SLOTS_KEY_FIELD, "occupied_slots").forGetter(InventoryChangedCriterionMacro::getOccupiedSlotsKey),
        Codec.STRING.optionalFieldOf(EMPTY_SLOTS_KEY_FIELD, "empty_slots").forGetter(InventoryChangedCriterionMacro::getEmptySlotsKey)
    ).apply(instance, InventoryChangedCriterionMacro::new));

    private final String movedItemKey;
    private final String fullItemsKey;
    private final String occupiedSlotsKey;
    private final String emptySlotsKey;

    public InventoryChangedCriterionMacro(String movedItemKey, String fullItemsKey, String occupiedSlotsKey, String emptySlotsKey) {
        super(Criteria.INVENTORY_CHANGED);
        this.movedItemKey = movedItemKey;
        this.fullItemsKey = fullItemsKey;
        this.occupiedSlotsKey = occupiedSlotsKey;
        this.emptySlotsKey = emptySlotsKey;
    }

    public String getMovedItemKey() {
        return movedItemKey;
    }

    public String getFullItemsKey() {
        return fullItemsKey;
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

        context.<ItemStack>ifPresent(MOVED_ITEM_KEY_FIELD, movedItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, movedItemKey, movedItemStack)
        );

        context.<Integer>ifPresent(FULL_ITEMS_KEY_FIELD, fullItems ->
            rootNbt.putInt(fullItemsKey, fullItems)
        );

        context.<Integer>ifPresent(EMPTY_SLOTS_KEY_FIELD, emptySlots ->
            rootNbt.putInt(emptySlotsKey, emptySlots)
        );

        context.<Integer>ifPresent(OCCUPIED_SLOTS_KEY_FIELD, occupiedSlots ->
            rootNbt.putInt(occupiedSlotsKey, occupiedSlots)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.INVENTORY_CHANGED, () -> CODEC);
    }

}
