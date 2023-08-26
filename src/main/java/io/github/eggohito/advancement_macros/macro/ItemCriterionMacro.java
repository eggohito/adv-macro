package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;

public abstract class ItemCriterionMacro extends Macro {

    public static final String LOCATION_KEY_FIELD = "location_key";
    public static final String ITEM_KEY_FIELD = "item_key";

    private final String locationKey;
    private final String itemKey;

    public ItemCriterionMacro(Identifier id, String locationKey, String itemKey) {
        super(id);
        this.locationKey = locationKey;
        this.itemKey = itemKey;
    }

    protected static Codec<ItemCriterionMacro> getCodec(BiFunction<String, String, ItemCriterionMacro> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf(LOCATION_KEY_FIELD, "location").forGetter(ItemCriterionMacro::getLocationKey),
            Codec.STRING.optionalFieldOf(ITEM_KEY_FIELD, "item").forGetter(ItemCriterionMacro::getItemKey)
        ).apply(instance, macroFunction));
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<BlockPos>ifPresent(LOCATION_KEY_FIELD, locationPos ->
            NbtUtil.writeBlockPosToNbt(rootNbt, locationKey, locationPos)
        );

        context.<ItemStack>ifPresent(ITEM_KEY_FIELD, itemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, itemStack)
        );

    }

    public String getLocationKey() {
        return locationKey;
    }

    public String getItemKey() {
        return itemKey;
    }

}
