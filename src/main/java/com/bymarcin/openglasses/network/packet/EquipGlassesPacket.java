package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class EquipGlassesPacket extends Packet<EquipGlassesPacket, IMessage> {

    Location UUID;
    String player;

    private int width, height;

    public EquipGlassesPacket(Location UUID, EntityPlayer player, int width, int height) {
        this.player = player.getGameProfile().getName();
        this.UUID = UUID;
        this.width = width;
        this.height = height;
    }

    public EquipGlassesPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
        this.UUID = new Location(readInt(), readInt(), readInt(), readInt(), readLong());
        this.width = readInt();
        this.height = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeString(player);
        writeInt(UUID.x);
        writeInt(UUID.y);
        writeInt(UUID.z);
        writeInt(UUID.dimID);
        writeLong(UUID.uniqueKey);
        writeInt(width);
        writeInt(height);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.subscribePlayer(player, UUID, width, height);
        return null;
    }

}
