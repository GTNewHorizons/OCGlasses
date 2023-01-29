package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderableWidget {

    void render(EntityPlayer player, double playerX, double playerY, double playerZ);

    RenderType getRenderType();

    boolean shouldWidgetBeRendered();
}
