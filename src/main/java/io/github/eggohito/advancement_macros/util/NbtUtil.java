package io.github.eggohito.advancement_macros.util;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;

public final class NbtUtil {

    public static void writeItemStackToNbt(NbtCompound rootNbt, String name, ItemStack stack) {
        ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
            .resultOrPartial(AdvancementMacros.LOGGER::error)
            .ifPresent(stackNbt -> rootNbt.put(name, stackNbt));
    }

    public static void writeBlockPosToNbt(NbtCompound rootNbt, String name, BlockPos blockPos) {

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        rootNbt.putString(name, "%s %s %s".formatted(x, y, z));

        rootNbt.putInt(name + "_x", x);
        rootNbt.putInt(name + "_y", y);
        rootNbt.putInt(name + "_z", z);

    }

    public static void writeDamageSourceToNbt(NbtCompound rootNbt, String name, DamageSource damageSource) {
        DamageType.CODEC.encodeStart(NbtOps.INSTANCE, damageSource.getType())
            .resultOrPartial(AdvancementMacros.LOGGER::error)
            .ifPresent(damageTypeNbt -> rootNbt.put(name, damageTypeNbt));
    }

}
