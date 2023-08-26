package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;

public class BrewedPotionCriterionMacro extends Macro {

    public static final String BREWED_POTION_ID_KEY_FIELD = "brewed_potion_id_key";
    public static final String STATUS_EFFECTS_KEY_FIELD = "status_effects_key";

    public static final Codec<BrewedPotionCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(BREWED_POTION_ID_KEY_FIELD, "brewed_potion_id").forGetter(BrewedPotionCriterionMacro::getBrewedPotionIdKey),
        Codec.STRING.optionalFieldOf(STATUS_EFFECTS_KEY_FIELD, "status_effects").forGetter(BrewedPotionCriterionMacro::getStatusEffectsKey)
    ).apply(instance, BrewedPotionCriterionMacro::new));

    private final String brewedPotionIdKey;
    private final String statusEffectsKey;

    public BrewedPotionCriterionMacro(String brewedPotionIdKey, String statusEffectsKey) {
        super(Criteria.BREWED_POTION.getId());
        this.brewedPotionIdKey = brewedPotionIdKey;
        this.statusEffectsKey = statusEffectsKey;
    }

    public String getBrewedPotionIdKey() {
        return brewedPotionIdKey;
    }

    public String getStatusEffectsKey() {
        return statusEffectsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Identifier>ifPresent(BREWED_POTION_ID_KEY_FIELD, brewedPotionId ->
            rootNbt.putString(brewedPotionIdKey, brewedPotionId.toString())
        );

        context.<List<StatusEffectInstance>>ifPresent(STATUS_EFFECTS_KEY_FIELD, statusEffects -> {

            NbtList statusEffectsNbt = new NbtList();
            for (StatusEffectInstance statusEffect : statusEffects) {

                NbtCompound statusEffectNbt = new NbtCompound();
                statusEffect.writeNbt(statusEffectNbt);

                statusEffectsNbt.add(statusEffectNbt);

            }

            rootNbt.put(statusEffectsKey, statusEffectsNbt);

        });

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.BREWED_POTION.getId(), () -> CODEC);
    }

}
