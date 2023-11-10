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
import net.minecraft.util.Pair;

public class VillagerTradeCriterionMacro extends Macro {

    public static final String TRADER_KEY_FIELD = "trader_key";
    public static final String SOLD_ITEM_KEY_FIELD = "sold_item_key";

    public static final Codec<VillagerTradeCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(TRADER_KEY_FIELD, "trader").forGetter(VillagerTradeCriterionMacro::getTradedMerchantKey),
        Codec.STRING.optionalFieldOf(SOLD_ITEM_KEY_FIELD, "sold_item").forGetter(VillagerTradeCriterionMacro::getSoldItemKey)
    ).apply(instance, VillagerTradeCriterionMacro::new));

    private final String tradedMerchantKey;
    private final String soldItemKey;

    public VillagerTradeCriterionMacro(String tradedMerchantKey, String soldItemKey) {
        super(Criteria.VILLAGER_TRADE);
        this.tradedMerchantKey = tradedMerchantKey;
        this.soldItemKey = soldItemKey;
    }

    public String getTradedMerchantKey() {
        return tradedMerchantKey;
    }

    public String getSoldItemKey() {
        return soldItemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<MerchantEntity>ifPresent(TRADER_KEY_FIELD, traderEntity ->
            rootNbt.putString(tradedMerchantKey, traderEntity.getUuidAsString())
        );

        context.<ItemStack>ifPresent(SOLD_ITEM_KEY_FIELD, soldItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, soldItemKey, soldItemStack)
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.VILLAGER_TRADE, () -> CODEC);
    }

}
