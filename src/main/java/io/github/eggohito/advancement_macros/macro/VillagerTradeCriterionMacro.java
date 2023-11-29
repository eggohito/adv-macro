package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class VillagerTradeCriterionMacro extends Macro {

    public static final String VILLAGER_KEY = "villager";
    public static final String ITEM_KEY = "item";

    public static final Codec<VillagerTradeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(VILLAGER_KEY, VILLAGER_KEY).forGetter(VillagerTradeCriterionMacro::getVillagerKey),
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(VillagerTradeCriterionMacro::getItemKey)
    ).apply(instance, VillagerTradeCriterionMacro::new));

    private final String villagerKey;
    private final String itemKey;

    public VillagerTradeCriterionMacro(String villagerKey, String itemKey) {
        this.villagerKey = villagerKey;
        this.itemKey = itemKey;
    }

    public String getVillagerKey() {
        return villagerKey;
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

        context.<MerchantEntity>ifPresent(VILLAGER_KEY, traderEntity ->
            rootNbt.putString(villagerKey, traderEntity.getUuidAsString())
        );

        context.<ItemStack>ifPresent(ITEM_KEY, soldItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, soldItemStack)
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.VILLAGER_TRADE, () -> CODEC);
    }

}
