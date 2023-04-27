package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class KeyboardInteractOverlayPacket extends Packet<KeyboardInteractOverlayPacket, IMessage> {

    String player;

    private int key;
    private char character;

    public KeyboardInteractOverlayPacket(EntityPlayer player, char character, int key) {
        this.player = player.getGameProfile().getName();
        this.character = character;
        this.key = key;
    }

    public KeyboardInteractOverlayPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
        this.key = readInt();
        this.character = (char) readInt();
    }

    @Override
    protected void write() throws IOException {
        writeString(player);
        writeInt(key);
        writeInt(character);
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        ServerSurface.instance.playerHudKeyboardInteract(player, character, key);
        return null;
    }
}
