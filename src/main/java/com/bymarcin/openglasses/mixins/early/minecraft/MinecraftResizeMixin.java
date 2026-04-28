package com.bymarcin.openglasses.mixins.early.minecraft;

import static com.bymarcin.openglasses.item.OpenGlassesItem.findAllEquippedGlasses;
import static com.bymarcin.openglasses.network.GlassesNetworkRegistry.packetHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.packet.ScreenResizePacket;
import com.bymarcin.openglasses.utils.Location;

@Mixin(Minecraft.class)
public class MinecraftResizeMixin {

    @Shadow
    public EntityClientPlayerMP thePlayer;

    @Inject(method = "resize", at = @At("HEAD"))
    private void onResize(int width, int height, CallbackInfo ci) {
        findAllEquippedGlasses(thePlayer).forEach((s) -> {
            Location uuid = OpenGlassesItem.getUUID(s);
            if (uuid != null) {
                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution sr = new ScaledResolution(mc, width <= 0 ? 1 : width, height <= 0 ? 1 : height);
                packetHandler.sendToServer(
                        new ScreenResizePacket(
                                sr.getScaledWidth(),
                                sr.getScaledHeight(),
                                uuid,
                                thePlayer.getDisplayName()));
            }
        });
    }
}
