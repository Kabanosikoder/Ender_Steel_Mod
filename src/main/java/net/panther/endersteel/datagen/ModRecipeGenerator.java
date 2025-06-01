package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.item.ModItems;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Bagel Recipe, the best goddamn food in the whole game
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.BAGEL, 2)
                .pattern(" W ")
                .pattern("WSW")
                .pattern(" W ")
                .input('W', Items.WHEAT)
                .input('S', Items.WHEAT_SEEDS)
                .criterion(FabricRecipeProvider.hasItem(Items.WHEAT),
                        FabricRecipeProvider.conditionsFromItem(Items.WHEAT))
                .offerTo(exporter);

        // Smithing Template Recipe
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_STEEL_SCYTHE, 1)
                .pattern("EEB")
                .pattern(" S ")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('E', ModItems.ENDER_STEEL_INGOT)
                .input('B', ModBlocks.ENDER_STEEL_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(ModItems.ENDER_SCRAP),
                        FabricRecipeProvider.conditionsFromItem(ModItems.ENDER_SCRAP))
                .offerTo(exporter);

        // Smithing Template Duplication Recipe
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE, 2)
                .pattern("EDE")
                .pattern("ESE")
                .pattern("EDE")
                .input('S', ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE)
                .input('E', Items.ENDER_PEARL)
                .input('D', Items.DIAMOND)
                .criterion(FabricRecipeProvider.hasItem(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE),
                        FabricRecipeProvider.conditionsFromItem(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE))
                .offerTo(exporter, Identifier.of("endersteel", "ender_steel_upgrade_smithing_template_duplication"));

        // Smelting and Blasting Recipes
        offerSmelting(exporter, 
            List.of(ModBlocks.ENDER_REMNANT),
            RecipeCategory.MISC,
            ModItems.ENDER_SCRAP,
            8.0f,
            200,
            "ender_scrap");
            
        offerBlasting(exporter,
            List.of(ModBlocks.ENDER_REMNANT),
            RecipeCategory.MISC,
            ModItems.ENDER_SCRAP,
            8.0f,
            100,
            "ender_scrap");

        offerSmelting(exporter,
            List.of(ModItems.ENDER_SCRAP),
            RecipeCategory.MISC,
            ModItems.ENDER_STEEL_INGOT,
            4.0f,
            200,
            "ender_steel_ingot");

        offerBlasting(exporter,
            List.of(ModItems.ENDER_SCRAP),
            RecipeCategory.MISC,
            ModItems.ENDER_STEEL_INGOT,
            4.0f,
            100,
            "ender_steel_ingot");

        // Ender Steel Block Crafting Recipe (9 ingots -> 1 block)
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENDER_STEEL_BLOCK, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.ENDER_STEEL_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.ENDER_STEEL_INGOT),
                        FabricRecipeProvider.conditionsFromItem(ModItems.ENDER_STEEL_INGOT))
                .offerTo(exporter);

        // Ender Steel Ingot Crafting Recipe (1 block -> 9 ingots)
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_STEEL_INGOT, 9)
                .input(ModBlocks.ENDER_STEEL_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.ENDER_STEEL_BLOCK),
                        FabricRecipeProvider.conditionsFromItem(ModBlocks.ENDER_STEEL_BLOCK))
                .offerTo(exporter, Identifier.of("endersteel", "ender_steel_ingot_from_block"));

        // Armor Smithing Recipes
        generateSmithingRecipe(exporter, Items.NETHERITE_HELMET, ModItems.ENDER_STEEL_HELMET, "ender_steel_helmet");
        generateSmithingRecipe(exporter, Items.NETHERITE_CHESTPLATE, ModItems.ENDER_STEEL_CHESTPLATE, "ender_steel_chestplate");
        generateSmithingRecipe(exporter, Items.NETHERITE_LEGGINGS, ModItems.ENDER_STEEL_LEGGINGS, "ender_steel_leggings");
        generateSmithingRecipe(exporter, Items.NETHERITE_BOOTS, ModItems.ENDER_STEEL_BOOTS, "ender_steel_boots");

        // Tool and Weapon Smithing Recipes
        generateSmithingRecipe(exporter, Items.NETHERITE_SWORD, ModItems.ENDER_STEEL_SWORD, "ender_steel_sword");
        generateSmithingRecipe(exporter, Items.NETHERITE_PICKAXE, ModItems.ENDER_STEEL_PICKAXE, "ender_steel_pickaxe");
        generateSmithingRecipe(exporter, Items.NETHERITE_AXE, ModItems.ENDER_STEEL_AXE, "ender_steel_axe");
        generateSmithingRecipe(exporter, Items.NETHERITE_SHOVEL, ModItems.ENDER_STEEL_SHOVEL, "ender_steel_shovel");
        generateSmithingRecipe(exporter, Items.MACE, ModItems.VOID_MACE, "void_mace");

    }

    private void generateSmithingRecipe(RecipeExporter exporter, Item base, Item result, String name) {
        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(base),
                Ingredient.ofItems(ModItems.ENDER_STEEL_INGOT),
                RecipeCategory.COMBAT,
                result)
            .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
            .offerTo(exporter, Identifier.of("endersteel", name + "_smithing"));
    }
}
