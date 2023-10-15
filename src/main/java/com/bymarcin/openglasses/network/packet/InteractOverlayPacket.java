package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class InteractOverlayPacket extends Packet<InteractOverlayPacket, IMessage> {

    private UUID playerUUID;

    private int x, y, button, type;

    public InteractOverlayPacket(EntityPlayer player, int x, int y, int button, int type) {
        this.playerUUID = player.getGameProfile().getId();
        this.x = x;
        this.y = y;
        this.button = button;
        this.type = type;

    }

    public InteractOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.playerUUID = new UUID(readLong(), readLong());
        this.x = readInt();
        this.y = readInt();
        this.button = readInt();
        this.type = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeLong(playerUUID.getMostSignificantBits());
        writeLong(playerUUID.getLeastSignificantBits());
        writeInt(x);
        writeInt(y);
        writeInt(button);
        writeInt(type);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.playerHudInteract(playerUUID, x, y, button, type);
        return null;
    }

}
