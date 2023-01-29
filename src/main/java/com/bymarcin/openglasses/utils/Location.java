package com.bymarcin.openglasses.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

public class Location {

    public int x, y, z, dimID;
    public long uniqueKey;

    public Location(int x, int y, int z, int dimID, long uniqueKey) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        this.uniqueKey = uniqueKey;
    }

    public Location() {}

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Location) {
            if (((Location) arg0).x == x && ((Location) arg0).y == y
                    && ((Location) arg0).z == z
                    && ((Location) arg0).dimID == dimID
                    && ((Location) arg0).uniqueKey == uniqueKey) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "X:" + x + " Y:" + y + " Z:" + z + " DIM:" + dimID + "\n Key:" + uniqueKey;
    }

    public String[] toArrayString() {
        return new String[] { "X:" + x + " Y:" + y + " Z:" + z, "DIM:" + dimID, "Key:" + uniqueKey };
    }

    public TileEntity getTileEntity() {
        World world = MinecraftServer.getServer().worldServerForDimension(dimID);
        if (world == null) return null;
        return world.getTileEntity(x, y, z);
    }

    public OpenGlassesTerminalTileEntity getTerminal() {
        TileEntity te = getTileEntity();
        if (te instanceof OpenGlassesTerminalTileEntity) {
            return (OpenGlassesTerminalTileEntity) te;
        }
        return null;
    }

    public Location readFromNBT(NBTTagCompound nbt) {
        x = nbt.getInteger("locX");
        y = nbt.getInteger("locY");
        z = nbt.getInteger("locZ");
        dimID = nbt.getInteger("locDIM");
        uniqueKey = nbt.getLong("uniqueKey");
        return this;
    }

    public Location writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("locX", x);
        nbt.setInteger("locY", y);
        nbt.setInteger("locZ", z);
        nbt.setInteger("locDIM", dimID);
        nbt.setLong("uniqueKey", uniqueKey);
        return this;
    }
}
