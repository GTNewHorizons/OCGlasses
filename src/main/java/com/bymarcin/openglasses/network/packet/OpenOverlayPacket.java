package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class OpenOverlayPacket extends Packet<OpenOverlayPacket, IMessage> {

    String player;

    public OpenOverlayPacket(EntityPlayer player) {
        this.player = player.getGameProfile().getName();
    }

    public OpenOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
    }

    @Override
    protected void write() throws IOException {
        writeString(player);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.overlayOpened(player);
        return null;
    }

}
