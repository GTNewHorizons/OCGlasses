package com.bymarcin.openglasses.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.Packet;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.utils.Location;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class GlassesEventPacket extends Packet<GlassesEventPacket, IMessage> {

    public static enum EventType {
        EQUIPED_GLASSES,
        UNEQUIPED_GLASSES,
        INTERACT_OVERLAY,
        KEYBOARD_INTERACT_OVERLAY,
        BLOCK_INTERACT,
        OPEN_OVERLAY,
        CLOSE_OVERLAY
    }

    EventType eventType;
    Location UUID;
    String player;

    private int x, y, button, type;
    private int key;
    private char character;

    public GlassesEventPacket(EventType eventType, Location UUID, EntityPlayer player, int x, int y, int button,
            int type, char character, int key) {
        this.player = player.getGameProfile().getName();
        this.eventType = eventType;
        this.UUID = UUID;
        this.x = x;
        this.y = y;
        this.button = button;
        this.type = type;
        this.character = character;
        this.key = key;
    }

    public GlassesEventPacket() {}

    @Override
    protected void read() throws IOException {
        this.player = readString();
        this.eventType = EventType.values()[readInt()];

        switch (eventType) {
            case INTERACT_OVERLAY:
                x = readInt();
                y = readInt();
                button = readInt();
                type = readInt();
                return;
            case UNEQUIPED_GLASSES:
                return;
            case EQUIPED_GLASSES:
                this.UUID = new Location(readInt(), readInt(), readInt(), readInt(), readLong());
                this.x = readInt();
                this.y = readInt();
                return;
            case KEYBOARD_INTERACT_OVERLAY:
                key = readInt();
                character = (char) readInt();
                return;
            case BLOCK_INTERACT:
                x = readInt();
                y = readInt();
                button = readInt();
                type = readInt();
                return;
            case OPEN_OVERLAY:
                return;
            case CLOSE_OVERLAY:
                return;
        }

    }

    @Override
    protected void write() throws IOException {
        writeString(player);
        writeInt(eventType.ordinal());

        switch (eventType) {
            case INTERACT_OVERLAY:
                writeInt(x);
                writeInt(y);
                writeInt(button);
                writeInt(type);
                break;
            case UNEQUIPED_GLASSES:
                return;
            case EQUIPED_GLASSES:
                writeInt(UUID.x);
                writeInt(UUID.y);
                writeInt(UUID.z);
                writeInt(UUID.dimID);
                writeLong(UUID.uniqueKey);
                writeInt(x);
                writeInt(y);
            case KEYBOARD_INTERACT_OVERLAY:
                writeInt(key);
                writeInt(character);
                break;
            case BLOCK_INTERACT:
                writeInt(x);
                writeInt(y);
                writeInt(button);
                writeInt(type);
                break;
            case OPEN_OVERLAY:
                return;
            case CLOSE_OVERLAY:
                return;
        }
    }

    @Override
    protected IMessage executeOnClient() {
        return null;
    }

    @Override
    protected IMessage executeOnServer() {
        switch (eventType) {
            case EQUIPED_GLASSES:
                ServerSurface.instance.subscribePlayer(player, UUID, x, y);
                break;
            case UNEQUIPED_GLASSES:
                ServerSurface.instance.unsubscribePlayer(player);
                break;
            case KEYBOARD_INTERACT_OVERLAY:
                ServerSurface.instance.playerHudKeyboardInteract(player, character, key);
                break;
            case INTERACT_OVERLAY:
                ServerSurface.instance.playerHudInteract(player, x, y, button, type);
                break;
            case BLOCK_INTERACT:
                ServerSurface.instance.playerBlockInteract(player, x, y, button, type);
                break;
            case OPEN_OVERLAY:
                ServerSurface.instance.overlayOpened(player);
                break;
            case CLOSE_OVERLAY:
                ServerSurface.instance.overlayClosed(player);
                break;
        }
        return null;
    }

}
