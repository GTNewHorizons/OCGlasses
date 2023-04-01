package com.bymarcin.openglasses.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

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
            Keyboard.KEY_C,
            "key.categories.openGlasses");

    public ClientKeyboardEvents() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!ClientSurface.instances.haveGlasses) return; // No glasses equipped, do nothing
        if (interactGUIKey.isPressed()) {
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
            MovingObjectPosition pos = ClientSurface.getBlockCoordsLookingAt(p, 5);
            if (pos != null) {
                GlassesNetworkRegistry.packetHandler
                        .sendToServer(new BlockInteractPacket(p, pos.blockX, pos.blockY, pos.blockZ, pos.sideHit));
            }
            GlassesNetworkRegistry.packetHandler.sendToServer(new OpenOverlayPacket(p));
            p.openGui(OpenGlasses.instance, 0, p.getEntityWorld(), 0, 0, 0);
            return;
        }
    }
}
