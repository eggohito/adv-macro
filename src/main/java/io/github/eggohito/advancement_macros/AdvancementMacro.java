package io.github.eggohito.advancement_macros;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancementMacro implements ModInitializer {

	public static final String MOD_NAMESPACE = "advancement-macros";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized");
	}

}