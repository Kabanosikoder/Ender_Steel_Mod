package net.panther.endersteel;

import net.fabricmc.api.ModInitializer;

import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.item.ModItemGroup;
import net.panther.endersteel.item.ModItems;
import net.panther.endersteel.util.ModEventHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteel implements ModInitializer {
	public static final String MOD_ID = "endersteel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("EnderSteel, this mod is my first!");
		ModItemGroup.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEventHandlers.registerEventHandlers();


	}
}