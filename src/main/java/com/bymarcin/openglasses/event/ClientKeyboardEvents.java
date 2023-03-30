package com.bymarcin.openglasses.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import com.bymarcin.openglasses.OpenGlasses;
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
            p.openGui(OpenGlasses.instance, 0, p.getEntityWorld(), 0, 0, 0);
            return;
        }
    }
}
