package com.bymarcin.openglasses.event;

import static com.bymarcin.openglasses.item.OpenGlassesItem.findAllEquippedGlasses;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.EquipGlassesPacket;
import com.bymarcin.openglasses.network.packet.UnequipGlassesPacket;
import com.bymarcin.openglasses.surface.ClientSurface;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientEventHandler {

    int tick = 0;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent e) {
        if (e.player != Minecraft.getMinecraft().thePlayer) return;
        tick++;
        if (tick % 40 != 0) {
            return;
        }
        tick = 0;

        List<ItemStack> glasses = findAllEquippedGlasses(e.player);
        ItemStack glassesStack = glasses.stream().findFirst().orElse(null);

        if (glassesStack != null) {
            Location uuid = OpenGlassesItem.getUUID(glassesStack);
            boolean alreadyHasGlasses = ClientSurface.instances.haveGlasses;
            if (uuid != null && !alreadyHasGlasses) {
                equiped(e.player, uuid);
            } else if (alreadyHasGlasses && (uuid == null || !uuid.equals(ClientSurface.instances.lastBind))) {
                unEquiped(e.player);
            }
        } else if (ClientSurface.instances.haveGlasses) {
            unEquiped(e.player);
        }
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if ((e.entity == Minecraft.getMinecraft().thePlayer) && (e.world.isRemote)) {
            ClientSurface.instances.removeAllWidgets();
            ClientSurface.instances.haveGlasses = false;
        }
    }

    public static void unEquiped(EntityPlayer player) {
        ClientSurface.instances.haveGlasses = false;
        ClientSurface.instances.removeAllWidgets();
        GlassesNetworkRegistry.packetHandler.sendToServer(new UnequipGlassesPacket(player));
    }

    public static void equiped(EntityPlayer player, Location uuid) {
        ScaledResolution sr = new ScaledResolution(
                Minecraft.getMinecraft(),
                Minecraft.getMinecraft().displayWidth,
                Minecraft.getMinecraft().displayHeight);
        ClientSurface.instances.lastBind = uuid;
        GlassesNetworkRegistry.packetHandler
                .sendToServer(new EquipGlassesPacket(uuid, player, sr.getScaledWidth(), sr.getScaledHeight()));
        ClientSurface.instances.haveGlasses = true;
    }
}
