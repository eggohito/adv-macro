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

public class UsedTotemCriterionMacro extends Macro {

    public static final String USED_TOTEM_KEY_FIELD = "used_totem_key";

    public static final Codec<UsedTotemCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(USED_TOTEM_KEY_FIELD, "used_totem").forGetter(UsedTotemCriterionMacro::getUsedTotemKey)
    ).apply(instance, UsedTotemCriterionMacro::new));

    private final String usedTotemKey;

    public UsedTotemCriterionMacro(String usedTotemKey) {
        super(Criteria.USED_TOTEM);
        this.usedTotemKey = usedTotemKey;
    }

    public String getUsedTotemKey() {
        return usedTotemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<ItemStack>ifPresent(USED_TOTEM_KEY_FIELD, usedTotemItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, usedTotemKey, usedTotemItemStack)
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.USED_TOTEM, () -> CODEC);
    }

}
