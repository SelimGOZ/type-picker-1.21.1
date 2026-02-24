package net.fortuneblack05.typerando.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fortuneblack05.typerando.item.TypePickerItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TypePickerRecipeProvider extends FabricRecipeProvider {

    public TypePickerRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // A placeholder recipe: A diamond surrounded by 4 dirt blocks
        // You can change these Items.DIRT / Items.DIAMOND to whatever you want later!
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, TypePickerItems.FAKE_TYPE_SHARD)
                .pattern(" D ")
                .pattern("DSD")
                .pattern(" D ")
                .input('D', Items.DIRT)
                .input('S', Items.DIAMOND)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }
}