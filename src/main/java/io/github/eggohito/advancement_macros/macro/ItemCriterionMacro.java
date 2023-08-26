package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;

public abstract class ItemCriterionMacro extends Macro {

    private final String locationKey;
    private final String itemKey;

    public ItemCriterionMacro(Identifier id, String locationKey, String itemKey) {
        super(id);
        this.locationKey = locationKey;
        this.itemKey = itemKey;
    }

    protected static Codec<ItemCriterionMacro> getCodec(BiFunction<String, String, ItemCriterionMacro> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("location_key", "location").forGetter(ItemCriterionMacro::getLocationKey),
            Codec.STRING.optionalFieldOf("item_key", "item").forGetter(ItemCriterionMacro::getItemKey)
        ).apply(instance, macroFunction));
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
