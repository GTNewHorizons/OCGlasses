package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class InteractOverlayPacket extends Packet<InteractOverlayPacket, IMessage> {

    String player;

    private int x, y, button, type;

    public InteractOverlayPacket(EntityPlayer player, int x, int y, int button, int type) {
        this.player = player.getGameProfile().getName();
        this.x = x;
        this.y = y;
        this.button = button;
        this.type = type;

    }

    public InteractOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
        this.x = readInt();
        this.y = readInt();
        this.button = readInt();
        this.type = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeString(player);
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
        ServerSurface.instance.playerHudInteract(player, x, y, button, type);
        return null;
    }

}
