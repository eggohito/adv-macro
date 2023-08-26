package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Collection;

public class FishingRodHookedCriterionMacro extends Macro {

    public static final String FISHING_ROD_KEY_FIELD = "fishing_rod_key";
    public static final String FISHING_BOBBER_KEY_FIELD = "fishing_bobber_key";
    public static final String FISHING_LOOTS_KEY_FIELD = "fishing_loots_key";

    public static final Codec<FishingRodHookedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(FISHING_ROD_KEY_FIELD, "fishing_rod").forGetter(FishingRodHookedCriterionMacro::getFishingRodKey),
        Codec.STRING.optionalFieldOf(FISHING_BOBBER_KEY_FIELD, "fishing_bobber").forGetter(FishingRodHookedCriterionMacro::getFishingBobberKey),
        Codec.STRING.optionalFieldOf(FISHING_LOOTS_KEY_FIELD, "fishing_loots").forGetter(FishingRodHookedCriterionMacro::getFishingLootsKey)
    ).apply(instance, FishingRodHookedCriterionMacro::new));

    private final String fishingRodKey;
    private final String fishingBobberKey;
    private final String fishingLootsKey;

    public FishingRodHookedCriterionMacro(String fishingRodKey, String fishingBobberKey, String fishingLootsKey) {
        super(Criteria.FISHING_ROD_HOOKED.getId());
        this.fishingRodKey = fishingRodKey;
        this.fishingBobberKey = fishingBobberKey;
        this.fishingLootsKey = fishingLootsKey;
    }

    public String getFishingRodKey() {
        return fishingRodKey;
    }

    public String getFishingBobberKey() {
        return fishingBobberKey;
    }

    public String getFishingLootsKey() {
        return fishingLootsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<ItemStack>ifPresent(FISHING_ROD_KEY_FIELD, fishingRodItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, fishingRodKey, fishingRodItemStack)
        );

        context.<FishingBobberEntity>ifPresent(FISHING_BOBBER_KEY_FIELD, fishingBobberEntity ->
            rootNbt.putString(fishingBobberKey, fishingBobberEntity.getUuidAsString())
        );

        context.<Collection<ItemStack>>ifPresent(FISHING_LOOTS_KEY_FIELD, fishingLoots -> {

            NbtList fishingLootsNbt = new NbtList();
            for (ItemStack fishingLoot : fishingLoots) {
                ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, fishingLoot)
                    .resultOrPartial(AdvancementMacros.LOGGER::error)
                    .ifPresent(fishingLootsNbt::add);
            }

            rootNbt.put(fishingLootsKey, fishingLootsNbt);

        });

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.FISHING_ROD_HOOKED.getId(), () -> CODEC);
    }

}
