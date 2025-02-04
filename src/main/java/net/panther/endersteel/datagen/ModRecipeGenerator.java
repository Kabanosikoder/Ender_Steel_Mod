package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_SCRAP)
                .pattern("NPN")
                .pattern("PEP")
                .pattern("NPN")
                .input('E', Items.ENDER_EYE)
                .input('N', Items.NETHERITE_SCRAP)
                .input('P', Items.ENDER_EYE)
                .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter, Identifier.of("endersteel", getRecipeName(ModItems.ENDER_SCRAP)));


        offerSmithingTrimRecipe(exporter, ModItems.ENDER_STEEL_ARMOR_SMITHING_TEMPLATE, Identifier.of(EnderSteel.MOD_ID, "endersteel"));
    }
}
