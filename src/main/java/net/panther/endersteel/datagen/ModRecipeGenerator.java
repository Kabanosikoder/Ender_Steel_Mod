package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
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
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {

                createShaped(RecipeCategory.FOOD, ModItems.BAGEL, 2)
                        .pattern(" W ")
                        .pattern("WSW")
                        .pattern(" W ")
                        .input('W', Items.WHEAT)
                        .input('S', Items.WHEAT_SEEDS)
                        .criterion("has_wheat", conditionsFromItem(Items.WHEAT))
                        .criterion("has_seeds", conditionsFromItem(Items.WHEAT_SEEDS))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, ModItems.ENDER_STEEL_SCYTHE, 1)
                        .pattern("EEB")
                        .pattern(" S ")
                        .pattern("S  ")
                        .input('S', Items.STICK)
                        .input('E', ModItems.ENDER_STEEL_INGOT)
                        .input('B', ModBlocks.ENDER_STEEL_BLOCK)
                        .criterion("has_ingot", conditionsFromItem(ModItems.ENDER_STEEL_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE, 2)
                        .pattern("EDE")
                        .pattern("ESE")
                        .pattern("EDE")
                        .input('S', ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE)
                        .input('E', Items.ENDER_PEARL)
                        .input('D', Items.DIAMOND)
                        .criterion("has_pearl", conditionsFromItem(Items.ENDER_PEARL))
                        .offerTo(exporter);

                createShaped(RecipeCategory.REDSTONE, ModBlocks.ENDER_STEEL_BLOCK, 1)
                        .pattern("###")
                        .pattern("###")
                        .pattern("###")
                        .input('#', ModItems.ENDER_STEEL_INGOT)
                        .criterion("has_ingot", conditionsFromItem(ModItems.ENDER_STEEL_INGOT))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.ENDER_STEEL_INGOT, 9)
                        .input(ModBlocks.ENDER_STEEL_BLOCK)
                        .criterion("has_block", conditionsFromItem(ModBlocks.ENDER_STEEL_BLOCK.asItem()))
                        .offerTo(exporter);

                offerSmelting(
                        List.of(ModBlocks.ENDER_REMNANT),
                        RecipeCategory.MISC,
                        ModItems.ENDER_SCRAP,
                        8.0f,
                        200,
                        "ender_scrap");

                offerBlasting(
                        List.of(ModBlocks.ENDER_REMNANT),
                        RecipeCategory.MISC,
                        ModItems.ENDER_SCRAP,
                        8.0f,
                        100,
                        "ender_scrap");

                offerSmelting(
                        List.of(ModItems.ENDER_SCRAP),
                        RecipeCategory.MISC,
                        ModItems.ENDER_STEEL_INGOT,
                        4.0f,
                        200,
                        "ender_steel_ingot");

                offerBlasting(
                        List.of(ModItems.ENDER_SCRAP),
                        RecipeCategory.MISC,
                        ModItems.ENDER_STEEL_INGOT,
                        4.0f,
                        100,
                        "ender_steel_ingot");


                generateSmithingRecipe(exporter, Items.NETHERITE_HELMET, ModItems.ENDER_STEEL_HELMET, "ender_steel_helmet");
                generateSmithingRecipe(exporter, Items.NETHERITE_CHESTPLATE, ModItems.ENDER_STEEL_CHESTPLATE, "ender_steel_chestplate");
                generateSmithingRecipe(exporter, Items.NETHERITE_LEGGINGS, ModItems.ENDER_STEEL_LEGGINGS, "ender_steel_leggings");
                generateSmithingRecipe(exporter, Items.NETHERITE_BOOTS, ModItems.ENDER_STEEL_BOOTS, "ender_steel_boots");

                generateSmithingRecipe(exporter, Items.NETHERITE_SWORD, ModItems.ENDER_STEEL_SWORD, "ender_steel_sword");
                generateSmithingRecipe(exporter, Items.NETHERITE_PICKAXE, ModItems.ENDER_STEEL_PICKAXE, "ender_steel_pickaxe");
                generateSmithingRecipe(exporter, Items.NETHERITE_AXE, ModItems.ENDER_STEEL_AXE, "ender_steel_axe");
                generateSmithingRecipe(exporter, Items.NETHERITE_SHOVEL, ModItems.ENDER_STEEL_SHOVEL, "ender_steel_shovel");
                generateSmithingRecipe(exporter, Items.MACE, ModItems.VOID_MACE, "void_mace");


                new SmithingTransformRecipeJsonBuilder(
                        Ingredient.ofItems(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.fromTag(
                                registries.getOrThrow(RegistryKeys.ITEM)
                                        .getOrThrow(ModItemTagProvider.ENDER_STEEL_UPGRADABLE)
                        ),
                        Ingredient.ofItems(ModItems.ENDER_STEEL_INGOT),
                        RecipeCategory.COMBAT,
                        ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE
                )
                        .criterion("has_template", conditionsFromItem(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE))
                        .offerTo(exporter, String.valueOf((Identifier.of("endersteel", "combat_smithing"))));
            }

        };
    }

    @Override
    public String getName() {
        return "EnderSteel Recipes";
    }

    private void generateSmithingRecipe(RecipeExporter exporter, Item base, Item result, String name) {
    }
}