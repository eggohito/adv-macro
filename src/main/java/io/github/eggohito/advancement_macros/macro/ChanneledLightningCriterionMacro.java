package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Pair;

import java.util.Collection;

public class ChanneledLightningCriterionMacro extends Macro {

    public static final String VICTIMS_KEY = "victims";

    public static final Codec<ChanneledLightningCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(VICTIMS_KEY, VICTIMS_KEY).forGetter(ChanneledLightningCriterionMacro::getVictimsKey)
    ).apply(instance, ChanneledLightningCriterionMacro::new));

    private final String victimsKey;

    public ChanneledLightningCriterionMacro(String victimsKey) {
        super(Criteria.CHANNELED_LIGHTNING);
        this.victimsKey = victimsKey;
    }

    public String getVictimsKey() {
        return victimsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Collection<? extends Entity>>ifPresent(VICTIMS_KEY, victims -> {

            NbtList victimsNbt = new NbtList();
            for (Entity victim : victims) {
                victimsNbt.add(NbtString.of(victim.getUuidAsString()));
            }

            rootNbt.put(victimsKey, victimsNbt);

        });
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.CHANNELED_LIGHTNING, () -> CODEC);
    }

}
