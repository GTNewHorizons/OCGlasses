package com.bymarcin.openglasses.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import tconstruct.armor.ArmorProxyClient;
import tconstruct.armor.player.TPlayerStats;
import baubles.api.BaublesApi;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.NetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
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

        ItemStack glassesStack = e.player.inventory.armorInventory[3];
        Item glasses = glassesStack != null ? glassesStack.getItem() : null;

        if (OpenGlasses.tinkers && !(glasses instanceof OpenGlassesItem)) {
            IInventory inventory = TPlayerStats.get(e.player).armor;
            for (int i = 0; i != inventory.getSizeInventory(); i++) {
                glassesStack = e.side.isClient() ? ArmorProxyClient.armorExtended.getStackInSlot(i)
                        : inventory.getStackInSlot(i);
                glasses = glassesStack != null ? glassesStack.getItem() : null;
                if (glasses instanceof OpenGlassesItem) break;
            }
        }
        if (OpenGlasses.baubles && !(glasses instanceof OpenGlassesItem)) // try bauble
        {
            IInventory handler = BaublesApi.getBaubles(e.player);
            if (handler != null) {
                for (int i = 0; i < handler.getSizeInventory(); ++i) {
                    glassesStack = handler.getStackInSlot(i);
                    glasses = glassesStack != null ? glassesStack.getItem() : null;
                    if (glasses instanceof OpenGlassesItem) break;
                }
            }
        }
        if (glasses instanceof OpenGlassesItem) {
            Location uuid = OpenGlassesItem.getUUID(glassesStack);
            if (uuid != null && ClientSurface.instances.haveGlasses == false) {
                equiped(e.player, uuid);
            } else if (ClientSurface.instances.haveGlasses == true
                    && (uuid == null || !uuid.equals(ClientSurface.instances.lastBind))) {
                        unEquiped(e.player);
                    }
        } else if (ClientSurface.instances.haveGlasses == true) {
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
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.UNEQUIPED_GLASSES, null, player));
    }

    public static void equiped(EntityPlayer player, Location uuid) {
        ClientSurface.instances.lastBind = uuid;
        NetworkRegistry.packetHandler.sendToServer(new GlassesEventPacket(EventType.EQUIPED_GLASSES, uuid, player));
        ClientSurface.instances.haveGlasses = true;
    }
}
