package io.github.eggohito.advancement_macros;

import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.macro.*;
import io.github.eggohito.advancement_macros.util.MacroSupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class AdvancementMacros implements ModInitializer {

	public static final String MOD_NAMESPACE = "advancement-macros";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	public static final Pattern CRITERION_NAME_REGEX = Pattern.compile("[\\s.:/-]");

	public static final RegistryKey<Registry<Macro.Type>> REGISTRY_KEY;
	public static final Registry<Macro.Type> REGISTRY;

	static {
		REGISTRY_KEY = RegistryKey.ofRegistry(of("macro_types"));
		REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
	}

	@Override
	public void onInitialize() {

		register(PlacedBlockCriterionMacro::getFactory);
		register(PlayerKilledEntityCriterionMacro::getFactory);
		register(EntityKilledPlayerCriterionMacro::getFactory);
		register(ChangedDimensionCriterionMacro::getFactory);
		register(AllayDropItemOnBlockCriterionMacro::getFactory);
		register(UsedEnderEyeCriterionMacro::getFactory);
		register(BredAnimalsCriterionMacro::getFactory);
		register(ConsumeItemCriterionMacro::getFactory);
		register(TameAnimalCriterionMacro::getFactory);

		LOGGER.info("Initialized");

	}

	public static Identifier of(String path) {
		return new Identifier(MOD_NAMESPACE, path);
	}

	public static void register(MacroSupplier supplier) {
		Pair<Identifier, Macro.Type> factory = supplier.getFactory();
		Registry.register(REGISTRY, factory.getLeft(), factory.getRight());
	}

}