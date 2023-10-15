package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class BlockInteractPacket extends Packet<BlockInteractPacket, IMessage> {

    private UUID playerUUID;
    private int x, y, z, side;

    public BlockInteractPacket(EntityPlayer player, int x, int y, int z, int side) {
        this.playerUUID = player.getGameProfile().getId();
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
    }

    public BlockInteractPacket() {}

    @Override
    protected void read() throws IOException {
        this.playerUUID = new UUID(readLong(), readLong());
        this.x = readInt();
        this.y = readInt();
        this.z = readInt();
        this.side = readInt();
    }

    @Override
    protected void write() throws IOException {
        writeLong(playerUUID.getMostSignificantBits());
        writeLong(playerUUID.getLeastSignificantBits());
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
        ServerSurface.instance.playerBlockInteract(playerUUID, x, y, z, side);
        return null;
    }

}
