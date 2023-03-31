package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalStatus;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

public class ServerSurface {

    public static ServerSurface instance = new ServerSurface();

    HashMap<EntityPlayer, Location> players = new HashMap<EntityPlayer, Location>();

    public void subscribePlayer(String playerUUID, Location UUID) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = UUID.getTerminal();
            if (terminal != null && terminal.getTerminalUUID().equals(UUID)) {
                players.put(player, UUID);
                sendSync(player, UUID, terminal);
                sendPowerInfo(UUID, terminal.isPowered() ? TerminalStatus.HavePower : TerminalStatus.NoPower);
                terminal.onGlassesPutOn(player.getDisplayName());
            }
        }
    }

    public void unsubscribePlayer(String playerUUID) {
        EntityPlayerMP p = checkUUID(playerUUID);
        Location l = players.remove(p);
        if (l != null) {
            OpenGlassesTerminalTileEntity terminal = l.getTerminal();
            if (terminal != null) {
                terminal.onGlassesPutOff(p.getDisplayName());
            }
        }
    }

    public void playerHudInteract(String playerUUID, int x, int y, int button, int type) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onHudInteract(player.getDisplayName(), x, y, button, type);
            }
        }
    }

    public void playerHudKeyboardInteract(String playerUUID, char character, int key) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onHudInteractKeyboard(player.getDisplayName(), character, key);
            }
        }
    }

    public void playerBlockInteract(String playerUUID, int x, int y, int z, int side) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onBlockInteract(player.getDisplayName(), x, y, z, side);
            }
        }
    }

    public void overlayOpened(String playerUUID) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.overlayOpened(player.getDisplayName());
            }
        }
    }

    public void overlayClosed(String playerUUID) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.overlayClosed(player.getDisplayName());
            }
        }
    }

    public String[] getActivePlayers(Location l) {
        LinkedList<String> players = new LinkedList<String>();
        for (Entry<EntityPlayer, Location> p : this.players.entrySet()) {
            if (p.getValue().equals(l)) {
                players.add(p.getKey().getGameProfile().getName());
            }
        }
        return players.toArray(new String[] {});
    }

    public void sendSync(EntityPlayer p, Location coords, OpenGlassesTerminalTileEntity t) {
        WidgetUpdatePacket packet = new WidgetUpdatePacket(((OpenGlassesTerminalTileEntity) t).widgetList);
        GlassesNetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
    }

    public void sendPowerInfo(Location loc, TerminalStatus status) {
        TerminalStatusPacket packet = new TerminalStatusPacket(status);
        for (Entry<EntityPlayer, Location> e : players.entrySet()) {
            if (e.getValue().equals(loc)) {
                GlassesNetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
            }
        }
    }

    public void sendToUUID(WidgetUpdatePacket packet, Location UUID) {
        for (Entry<EntityPlayer, Location> e : players.entrySet()) {
            if (e.getValue().equals(UUID)) {
                GlassesNetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) e.getKey());
            }
        }
    }

    private EntityPlayerMP checkUUID(String uuid) {
        for (Object p : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (((EntityPlayerMP) p).getGameProfile().getName().equals(uuid)) return (EntityPlayerMP) p;
        }
        return null;
    }

}
