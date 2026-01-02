package com.bymarcin.openglasses.integration.nei;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.integration.nei.recipe.RecipHandlerOpenGlassesChatBoxUpgrade;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIOpenGlassesConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return OpenGlasses.MODID;
    }

    @Override
    public String getVersion() {
        return OpenGlasses.VERSION;
    }

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new RecipHandlerOpenGlassesChatBoxUpgrade());
        API.registerUsageHandler(new RecipHandlerOpenGlassesChatBoxUpgrade());
    }
}
