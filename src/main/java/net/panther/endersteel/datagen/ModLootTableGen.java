package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.panther.endersteel.block.ModBlocks;

public class ModLootTableGen extends FabricBlockLootTableProvider {
    public ModLootTableGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.ENDER_STEEL_BLOCK);

    }
}
