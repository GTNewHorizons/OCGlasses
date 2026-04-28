package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class ScreenResizePacket extends Packet<ScreenResizePacket, IMessage> {

    private int width;
    private int height;
    private Location locationUUID;
    private String playerName;

    public ScreenResizePacket(int width, int height, Location locationUUID, String playerName) {
        this.width = width;
        this.height = height;
        this.locationUUID = locationUUID;
        this.playerName = playerName;
    }

    public ScreenResizePacket() {}

    @Override
    protected void read() throws IOException {
        this.width = readInt();
        this.height = readInt();
        this.locationUUID = new Location(readInt(), readInt(), readInt(), readInt(), readLong());
        this.playerName = readString();
    }

    @Override
    protected void write() throws IOException {
        writeInt(width);
        writeInt(height);
        writeInt(locationUUID.x);
        writeInt(locationUUID.y);
        writeInt(locationUUID.z);
        writeInt(locationUUID.dimID);
        writeLong(locationUUID.uniqueKey);
        writeString(playerName);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        OpenGlassesTerminalTileEntity tile = locationUUID.getTerminal();
        if (tile != null) tile.onResize(playerName, width, height);
        return null;
    }

}
