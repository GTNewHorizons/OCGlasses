package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class UnequipGlassesPacket extends Packet<UnequipGlassesPacket, IMessage> {

    String player;

    public UnequipGlassesPacket(EntityPlayer player) {
        this.player = player.getGameProfile().getName();
    }

    public UnequipGlassesPacket() {}

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
        ServerSurface.instance.unsubscribePlayer(player);
        return null;
    }

}
