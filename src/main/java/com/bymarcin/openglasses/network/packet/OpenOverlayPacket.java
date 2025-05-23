package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class OpenOverlayPacket extends Packet<OpenOverlayPacket, IMessage> {

    private UUID playerUUID;

    public OpenOverlayPacket(EntityPlayer player) {
        this.playerUUID = player.getGameProfile().getId();
    }

    public OpenOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.playerUUID = new UUID(readLong(), readLong());
    }

    @Override
    protected void write() throws IOException {
        writeLong(playerUUID.getMostSignificantBits());
        writeLong(playerUUID.getLeastSignificantBits());
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.overlayOpened(playerUUID);
        return null;
    }

}
