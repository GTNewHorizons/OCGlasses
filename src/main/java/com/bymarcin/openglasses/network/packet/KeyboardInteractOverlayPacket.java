package com.bymarcin.openglasses.network.packet;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class KeyboardInteractOverlayPacket extends Packet<KeyboardInteractOverlayPacket, IMessage> {

    private UUID playerUUID;

    private int key;
    private char character;

    public KeyboardInteractOverlayPacket(EntityPlayer player, char character, int key) {
        this.playerUUID = player.getGameProfile().getId();
        this.character = character;
        this.key = key;
    }

    public KeyboardInteractOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.playerUUID = new UUID(readLong(), readLong());
        this.key = readInt();
        this.character = (char) readInt();
    }

    @Override
    protected void write() throws IOException {
        writeLong(playerUUID.getMostSignificantBits());
        writeLong(playerUUID.getLeastSignificantBits());
        writeInt(key);
        writeInt(character);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.playerHudKeyboardInteract(playerUUID, character, key);
        return null;
    }
}
