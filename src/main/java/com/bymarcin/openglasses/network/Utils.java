package com.bymarcin.openglasses.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bymarcin.openglasses.OpenGlasses;

public class Utils {

    public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
        World world = OpenGlasses.proxy.getWorld(dimensionId);
        if (world == null) return null;
        return world.getTileEntity(x, y, z);
    }

    public static TileEntity getTileEntityServer(int dimensionId, int x, int y, int z) {
        World world = MinecraftServer.getServer().worldServerForDimension(dimensionId);
        if (world == null) return null;
        return world.getTileEntity(x, y, z);
    }
}
