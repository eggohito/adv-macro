package io.github.eggohito.advancement_macros.util;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.event.CriteriaTriggerCallback;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class VanillaCriteriaTriggerCallbacks {

    public static void register() {

        //  Common stuff
        CriteriaTriggerCallback.EVENT.register(
            AdvancementMacros.VANILLA_PHASE,
            (context, nbtToWriteTo) -> {
                for (Object obj : context.getData()) {

                    if (obj instanceof ItemStack stack) {
                        ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                            .resultOrPartial(AdvancementMacros.LOGGER::error)
                            .ifPresent(stackNbt -> nbtToWriteTo.put("item", stackNbt));
                    }

                    if (obj instanceof BlockPos pos) {

                        nbtToWriteTo.putString("block_location", "%s %s %s".formatted(pos.getX(), pos.getY(), pos.getZ()));

                        nbtToWriteTo.putInt("block_x", pos.getX());
                        nbtToWriteTo.putInt("block_y", pos.getY());
                        nbtToWriteTo.putInt("block_z", pos.getZ());

                    }

                }
            }
        );

        //  OnKilledCriterion
        CriteriaTriggerCallback.EVENT.register(
            AdvancementMacros.VANILLA_PHASE,
            (context, nbtToWriteTo) -> {

                if (!(context.getCriterion() instanceof OnKilledCriterion onKilled)) {
                    return;
                }

                for (Object obj : context.getData()) {

                    if (obj instanceof ServerPlayerEntity player) {
                        nbtToWriteTo.putString(onKilled.getId().equals(Criteria.PLAYER_KILLED_ENTITY.getId()) ? "killer" : "victim", player.getUuidAsString());
                    }

                    if (obj instanceof Entity entity) {
                        nbtToWriteTo.putString(onKilled.getId().equals(Criteria.ENTITY_KILLED_PLAYER.getId()) ? "victim" : "killer", entity.getUuidAsString());
                    }

                    if (obj instanceof DamageSource source) {
                        DamageType.CODEC.encodeStart(NbtOps.INSTANCE, source.getType())
                            .resultOrPartial(AdvancementMacros.LOGGER::error)
                            .ifPresent(dmgTypeNbt -> nbtToWriteTo.put("killing_blow", dmgTypeNbt));
                    }

                }

            }
        );

        //  ChangedDimensionCriterion
        CriteriaTriggerCallback.EVENT.register(
            AdvancementMacros.VANILLA_PHASE,
            (context, nbtToWriteTo) -> {

                if (!(context.getCriterion() instanceof ChangedDimensionCriterion)) {
                    return;
                }

                if (context.getMappedData().get("to") instanceof RegistryKey<?> toRegKey) {
                    nbtToWriteTo.putString("to", toRegKey.getValue().toString());
                }

                if (context.getMappedData().get("from") instanceof RegistryKey<?> fromRegKey) {
                    nbtToWriteTo.putString("from", fromRegKey.getValue().toString());
                }

            }
        );

    }

}
