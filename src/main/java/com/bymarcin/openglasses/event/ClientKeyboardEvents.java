package com.bymarcin.openglasses.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;
import com.bymarcin.openglasses.surface.ClientSurface;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
        if (!ClientSurface.instances.haveGlasses) return;
        if (interactGUIKey.isPressed()) {
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
            MovingObjectPosition pos = ClientSurface.getBlockCoordsLookingAt(p, 5);
            if (pos != null) {
                GlassesNetworkRegistry.packetHandler.sendToServer(
                        new GlassesEventPacket(
                                EventType.BLOCK_INTERACT,
                                null,
                                p,
                                pos.blockX,
                                pos.blockY,
                                pos.blockZ,
                                pos.sideHit,
                                '-',
                                -1));
            }
            p.openGui(OpenGlasses.instance, 0, p.getEntityWorld(), 0, 0, 0);
            return;
        }
    }
}
