package io.github.eggohito.advancement_macros.macro;

import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public abstract class ItemCriterionMacro extends Macro {

    private final String locationKey;
    private final String itemKey;

    public ItemCriterionMacro(Identifier id, String locationKey, String itemKey) {
        super(id);
        this.locationKey = locationKey;
        this.itemKey = itemKey;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, Object object) {

        if (object instanceof BlockPos blockPos) {
            NbtUtil.writeBlockPosToNbt(rootNbt, locationKey, blockPos);
        }

        if (object instanceof ItemStack stack) {
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, stack);
        }

    }

    public String getLocationKey() {
        return locationKey;
    }

    public String getItemKey() {
        return itemKey;
    }

}
