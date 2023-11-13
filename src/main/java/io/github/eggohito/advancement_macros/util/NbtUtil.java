package io.github.eggohito.advancement_macros.util;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class NbtUtil {

    public static void writeItemStackToNbt(NbtCompound rootNbt, String name, ItemStack stack) {
        ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
            .resultOrPartial(AdvancementMacros.LOGGER::error)
            .ifPresent(stackNbt -> rootNbt.put(name, stackNbt));
    }

    public static void writeBlockStateToNbt(NbtCompound rootNbt, String name, BlockState blockState) {
        BlockState.CODEC.encodeStart(NbtOps.INSTANCE, blockState)
            .resultOrPartial(AdvancementMacros.LOGGER::error)
            .ifPresent(stateNbt -> rootNbt.put(name, stateNbt));
    }

    public static void writeVec3dToNbt(NbtCompound rootNbt, String name, Vec3d vec3d) {

        double x = vec3d.getX();
        double y = vec3d.getY();
        double z = vec3d.getZ();

        rootNbt.putString(name, "%s %s %s".formatted(x, y, z));

        rootNbt.putDouble(name + "_x", x);
        rootNbt.putDouble(name + "_y", y);
        rootNbt.putDouble(name + "_z", z);

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

    public static void writeDamageTypeToNbt(NbtCompound rootNbt, String name, DamageType damageType) {
        DamageType.CODEC.encodeStart(NbtOps.INSTANCE, damageType)
            .resultOrPartial(AdvancementMacros.LOGGER::error)
            .ifPresent(damageTypeNbt -> rootNbt.put(name, damageTypeNbt));
    }

}
