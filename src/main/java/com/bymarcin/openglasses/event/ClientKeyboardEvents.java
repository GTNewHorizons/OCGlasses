package com.bymarcin.openglasses.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.BlockInteractPacket;
import com.bymarcin.openglasses.network.packet.OpenOverlayPacket;
import com.bymarcin.openglasses.surface.ClientSurface;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class ClientKeyboardEvents {

    public static KeyBinding interactGUIKey = new KeyBinding(
            "key.openglasses.interact",
            0,
            "key.categories.openGlasses");

    public static KeyBinding interactGUIKeyToggle = new KeyBinding(
            "key.openglasses.interactToggle",
            0,
            "key.categories.openGlasses");

    public ClientKeyboardEvents() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
        ClientRegistry.registerKeyBinding(interactGUIKeyToggle);
    }

    @SubscribeEvent
    public void onKeyDown(InputEvent.KeyInputEvent event) {
        if (interactGUIKey.isPressed() || interactGUIKeyToggle.isPressed()) {
            if (!ClientSurface.instances.haveGlasses) return; // No glasses equipped, do nothing
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            MovingObjectPosition pos = ClientSurface.getBlockCoordsLookingAt(player, 5);
            if (pos != null) {
                GlassesNetworkRegistry.packetHandler
                        .sendToServer(new BlockInteractPacket(player, pos.blockX, pos.blockY, pos.blockZ, pos.sideHit));
            }
            GlassesNetworkRegistry.packetHandler.sendToServer(new OpenOverlayPacket(player));
            player.openGui(OpenGlasses.instance, 0, player.getEntityWorld(), 0, 0, 0);
        }
    }

}
