package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class BlockInteractPacket extends Packet<BlockInteractPacket, IMessage> {

    String player;
    private int x, y, z, side;

    public BlockInteractPacket(EntityPlayer player, int x, int y, int z, int side) {
        this.player = player.getGameProfile().getName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
    }

    public BlockInteractPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
        this.x = readInt();
        this.y = readInt();
        this.z = readInt();
        this.side = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeString(player);
        writeInt(x);
        writeInt(y);
        writeInt(z);
        writeInt(side);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.playerBlockInteract(player, x, y, z, side);
        return null;
    }

}
