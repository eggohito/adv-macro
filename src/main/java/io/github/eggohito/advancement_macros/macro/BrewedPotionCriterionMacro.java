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

    public static final String POTION_KEY = "potion";
    public static final String STATUS_EFFECTS_KEY = "status_effects";

    public static final Codec<BrewedPotionCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(POTION_KEY, POTION_KEY).forGetter(BrewedPotionCriterionMacro::getPotionKey),
        Codec.STRING.optionalFieldOf(STATUS_EFFECTS_KEY, STATUS_EFFECTS_KEY).forGetter(BrewedPotionCriterionMacro::getStatusEffectsKey)
    ).apply(instance, BrewedPotionCriterionMacro::new));

    private final String potionKey;
    private final String statusEffectsKey;

    public BrewedPotionCriterionMacro(String potionKey, String statusEffectsKey) {
        super(Criteria.BREWED_POTION);
        this.potionKey = potionKey;
        this.statusEffectsKey = statusEffectsKey;
    }

    public String getPotionKey() {
        return potionKey;
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

        context.<Identifier>ifPresent(POTION_KEY, potionId ->
            rootNbt.putString(potionKey, potionId.toString())
        );

        context.<List<StatusEffectInstance>>ifPresent(STATUS_EFFECTS_KEY, statusEffects -> {

            NbtList statusEffectsNbt = new NbtList();
            for (StatusEffectInstance statusEffect : statusEffects) {

                NbtCompound statusEffectNbt = new NbtCompound();
                statusEffect.writeNbt(statusEffectNbt);

                statusEffectsNbt.add(statusEffectNbt);

            }

            rootNbt.put(statusEffectsKey, statusEffectsNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.BREWED_POTION, () -> CODEC);
    }

}
