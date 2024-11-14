package net.panther.endersteel;

import net.fabricmc.api.ModInitializer;

import net.panther.endersteel.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteel implements ModInitializer {
	public static final String MOD_ID = "ender-steel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Wazzup Danger");

		ModItems.registerModItems();
	}
}