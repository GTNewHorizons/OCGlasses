package com.bymarcin.openglasses.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    public void init() {

    }

    public World getWorld(int dimensionId) {
        return MinecraftServer.getServer().worldServerForDimension(dimensionId);
    }

    public int getCurrentClientDimension() {
        return -9001;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

}
