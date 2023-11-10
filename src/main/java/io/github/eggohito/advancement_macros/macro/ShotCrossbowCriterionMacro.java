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

public class ShotCrossbowCriterionMacro extends Macro {

    public static final String USED_CROSSBOW_ITEM_KEY_FIELD = "used_crossbow_item_key";

    public static final Codec<ShotCrossbowCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(USED_CROSSBOW_ITEM_KEY_FIELD, "used_crossbow_item").forGetter(ShotCrossbowCriterionMacro::getUsedCrossbowItemKey)
    ).apply(instance, ShotCrossbowCriterionMacro::new));

    private final String usedCrossbowItemKey;

    public ShotCrossbowCriterionMacro(String usedCrossbowItemKey) {
        super(Criteria.SHOT_CROSSBOW);
        this.usedCrossbowItemKey = usedCrossbowItemKey;
    }

    public String getUsedCrossbowItemKey() {
        return usedCrossbowItemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<ItemStack>ifPresent(USED_CROSSBOW_ITEM_KEY_FIELD, usedCrossbowItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, usedCrossbowItemKey, usedCrossbowItemStack)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.SHOT_CROSSBOW, () -> CODEC);
    }

}
