package com.bymarcin.openglasses.surface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalStatus;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class ServerSurface {

    public static ServerSurface instance = new ServerSurface();

    private final Map<UUID, Location> players = new HashMap<>();

    public void subscribePlayer(UUID playerUUID, Location UUID, int width, int height) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = UUID.getTerminal();
            if (terminal != null && terminal.getTerminalUUID().equals(UUID)) {
                players.put(playerUUID, UUID);
                sendSync(player, UUID, terminal);
                sendPowerInfo(UUID, terminal.isPowered() ? TerminalStatus.HavePower : TerminalStatus.NoPower);
                terminal.onGlassesPutOn(player.getDisplayName(), width, height);
            }
        }
    }

    public void unsubscribePlayer(UUID playerUUID) {
        EntityPlayerMP p = checkUUID(playerUUID);
        Location l = players.remove(p);
        if (l != null) {
            OpenGlassesTerminalTileEntity terminal = l.getTerminal();
            if (terminal != null) {
                terminal.onGlassesPutOff(p.getDisplayName());
            }
        }
    }

    public void playerHudInteract(UUID playerUUID, int x, int y, int button, int type) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onHudInteract(player.getDisplayName(), x, y, button, type);
            }
        }
    }

    public void playerHudKeyboardInteract(UUID playerUUID, char character, int key) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onHudInteractKeyboard(player.getDisplayName(), character, key);
            }
        }
    }

    public void playerBlockInteract(UUID playerUUID, int x, int y, int z, int side) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.onBlockInteract(player.getDisplayName(), x, y, z, side);
            }
        }
    }

    public void overlayOpened(UUID playerUUID) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.overlayOpened(player.getDisplayName());
            }
        }
    }

    public void overlayClosed(UUID playerUUID) {
        EntityPlayerMP player = checkUUID(playerUUID);
        if (player != null) {
            OpenGlassesTerminalTileEntity terminal = players.get(player).getTerminal();
            if (terminal != null) {
                terminal.overlayClosed(player.getDisplayName());
            }
        }
    }

    public UUID[] getActivePlayers(Location l) {
        List<UUID> players = new ArrayList<>();
        for (Entry<UUID, Location> p : this.players.entrySet()) {
            if (p.getValue().equals(l)) {
                players.add(p.getKey());
            }
        }
        return players.toArray(new UUID[0]);
    }

    public String[] getActivePlayerNames(Location l) {
        List<String> players = new ArrayList<>();
        for (Entry<UUID, Location> p : this.players.entrySet()) {
            EntityPlayerMP player = checkUUID(p.getKey());
            if (p.getValue().equals(l) && player != null) {
                players.add(player.getGameProfile().getName());
            }
        }
        return players.toArray(new String[0]);
    }

    public void sendSync(EntityPlayer p, Location coords, OpenGlassesTerminalTileEntity t) {
        WidgetUpdatePacket packet = new WidgetUpdatePacket(((OpenGlassesTerminalTileEntity) t).widgetList);
        GlassesNetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
    }

    public void sendPowerInfo(Location loc, TerminalStatus status) {
        TerminalStatusPacket packet = new TerminalStatusPacket(status);
        sendToUUID(packet, loc);
    }

    public void sendToUUID(IMessage packet, Location UUID) {
        for (Iterator<Entry<UUID, Location>> it = players.entrySet().iterator(); it.hasNext();) {
            Entry<UUID, Location> e = it.next();
            EntityPlayerMP player = checkUUID(e.getKey());
            if (player == null) {
                it.remove();
                continue;
            }

            if (e.getValue().equals(UUID)) {
                GlassesNetworkRegistry.packetHandler.sendTo(packet, player);
            }
        }
    }

    private EntityPlayerMP checkUUID(UUID uuid) {
        for (EntityPlayerMP p : (List<EntityPlayerMP>) MinecraftServer.getServer()
                .getConfigurationManager().playerEntityList) {
            if (p.getGameProfile().getId().equals(uuid)) return p;
        }
        return null;
    }

}
