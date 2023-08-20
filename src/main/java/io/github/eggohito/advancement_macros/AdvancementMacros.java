package io.github.eggohito.advancement_macros;

import io.github.eggohito.advancement_macros.util.VanillaCriteriaTriggerCallbacks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancementMacros implements ModInitializer {

	public static final String MOD_NAMESPACE = "advancement-macros";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	public static final Identifier VANILLA_PHASE = new Identifier(MOD_NAMESPACE, "phase/vanilla");

	@Override
	public void onInitialize() {
		VanillaCriteriaTriggerCallbacks.register();
		LOGGER.info("Initialized");
	}

}