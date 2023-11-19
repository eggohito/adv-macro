package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Pair;

import java.util.Collection;

public class FishingRodHookedCriterionMacro extends Macro {

    public static final String FISHING_BOBBER_KEY = "fishing_bobber";
    public static final String FISHING_ROD_KEY = "rod";
    public static final String ENTITY_KEY = "entity";
    public static final String ITEM_KEY = "item";

    public static final Codec<FishingRodHookedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(FISHING_BOBBER_KEY, FISHING_BOBBER_KEY).forGetter(FishingRodHookedCriterionMacro::getFishingBobberKey),
        strictOptionalField(FISHING_ROD_KEY, FISHING_ROD_KEY).forGetter(FishingRodHookedCriterionMacro::getFishingRodKey),
        strictOptionalField(ENTITY_KEY, ENTITY_KEY).forGetter(FishingRodHookedCriterionMacro::getEntityKey),
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(FishingRodHookedCriterionMacro::getItemKey)
    ).apply(instance, FishingRodHookedCriterionMacro::new));

    private final String fishingBobberKey;
    private final String fishingRodKey;
    private final String entityKey;
    private final String itemKey;

    public FishingRodHookedCriterionMacro(String fishingBobberKey, String fishingRodKey, String entityKey, String itemKey) {
        super(Criteria.FISHING_ROD_HOOKED);
        this.fishingBobberKey = fishingBobberKey;
        this.fishingRodKey = fishingRodKey;
        this.entityKey = entityKey;
        this.itemKey = itemKey;
    }

    public String getFishingBobberKey() {
        return fishingBobberKey;
    }

    public String getFishingRodKey() {
        return fishingRodKey;
    }

    public String getEntityKey() {
        return entityKey;
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

        context.<Entity>ifPresent(FISHING_BOBBER_KEY, fishingBobberEntity ->
            rootNbt.putString(fishingBobberKey, fishingBobberEntity.getUuidAsString())
        );

        context.<ItemStack>ifPresent(FISHING_ROD_KEY, fishingRodItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, fishingRodKey, fishingRodItemStack)
        );

        context.<Entity>ifPresent(ENTITY_KEY, hookedEntity ->
            rootNbt.putString(entityKey, hookedEntity.getUuidAsString())
        );

        context.<Collection<ItemStack>>ifPresent(ITEM_KEY, stacks -> {

            NbtList stacksNbt = new NbtList();
            for (ItemStack stack : stacks) {
                ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                    .resultOrPartial(AdvancementMacros.LOGGER::error)
                    .ifPresent(stacksNbt::add);
            }

            rootNbt.put(itemKey, stacksNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.FISHING_ROD_HOOKED, () -> CODEC);
    }

}
