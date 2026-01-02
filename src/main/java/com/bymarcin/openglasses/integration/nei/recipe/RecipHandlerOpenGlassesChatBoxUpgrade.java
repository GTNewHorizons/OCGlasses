package com.bymarcin.openglasses.integration.nei.recipe;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipHandlerOpenGlassesChatBoxUpgrade extends ShapelessRecipeHandler {

    CachedShapelessRecipe recipe = new CachedShapelessRecipe(
            Arrays.asList(
                    new ItemStack(OpenGlasses.openGlasses),
                    new ItemStack(
                            Item.getItemFromBlock(GameRegistry.findBlock("computronics", "computronics.chatBox")))),
            OpenGlassesItem.setChatBoxUpgrade(new ItemStack(OpenGlasses.openGlasses), true));

    public RecipHandlerOpenGlassesChatBoxUpgrade() {
        super();
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.getResult().item, result)) {
            arecipes.add(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getIngredients().get(0).item, ingredient)) {
            if (!OpenGlassesItem.hasChatBoxUpgrade(ingredient)) {
                arecipes.add(recipe);
            }
        } else if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getIngredients().get(1).item, ingredient)) {
            arecipes.add(recipe);
        }
    }
}
