package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class EquipGlassesPacket extends Packet<EquipGlassesPacket, IMessage> {

    private Location locationUUID;
    private UUID playerUUID;

    private int width, height;

    public EquipGlassesPacket(Location locationUUID, EntityPlayer player, int width, int height) {
        this.playerUUID = player.getGameProfile().getId();
        this.locationUUID = locationUUID;
        this.width = width;
        this.height = height;
    }

    public EquipGlassesPacket() {}

    @Override
    protected void read() throws IOException {
        this.playerUUID = new UUID(readLong(), readLong());
        this.locationUUID = new Location(readInt(), readInt(), readInt(), readInt(), readLong());
        this.width = readInt();
        this.height = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeLong(playerUUID.getMostSignificantBits());
        writeLong(playerUUID.getLeastSignificantBits());
        writeInt(locationUUID.x);
        writeInt(locationUUID.y);
        writeInt(locationUUID.z);
        writeInt(locationUUID.dimID);
        writeLong(locationUUID.uniqueKey);
        writeInt(width);
        writeInt(height);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.subscribePlayer(playerUUID, locationUUID, width, height);
        return null;
    }

}
