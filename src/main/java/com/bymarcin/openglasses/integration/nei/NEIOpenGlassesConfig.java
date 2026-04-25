package com.bymarcin.openglasses.integration.nei;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.integration.nei.recipe.RecipHandlerOpenGlassesChatBoxUpgrade;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
        if (OpenGlasses.computronics) {
            API.registerRecipeHandler(new RecipHandlerOpenGlassesChatBoxUpgrade());
            API.registerUsageHandler(new RecipHandlerOpenGlassesChatBoxUpgrade());
        }
    }
}
