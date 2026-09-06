package com.bymarcin.openglasses.surface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.widgets.component.face.Text;
import com.bymarcin.openglasses.utils.Location;
import com.bymarcin.openglasses.utils.RenderState;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSurface {

    public static ClientSurface instances = new ClientSurface();
    public Map<Integer, IRenderableWidget> renderables = new HashMap<Integer, IRenderableWidget>();
    public Map<Integer, IRenderableWidget> renderablesWorld = new HashMap<Integer, IRenderableWidget>();
    boolean isPowered = false;
    public boolean haveGlasses = false;
    public Location lastBind;
    IRenderableWidget noPowerRender;

    private ClientSurface() {
        noPowerRender = getNoPowerRender();
    }

    public void updateWigets(Set<Entry<Integer, Widget>> widgets) {
        for (Entry<Integer, Widget> widget : widgets) {
            IRenderableWidget r = widget.getValue().getRenderable();
            switch (r.getRenderType()) {
                case GameOverlayLocated:
                    renderables.put(widget.getKey(), r);
                    break;
                case WorldLocated:
                    renderablesWorld.put(widget.getKey(), r);
                    break;
            }
        }
    }

    public void removeWidgets(List<Integer> ids) {
        for (Integer id : ids) {
            renderables.remove(id);
            renderablesWorld.remove(id);
        }
    }

    public void removeAllWidgets() {
        renderables.clear();
        renderablesWorld.clear();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
        if (evt.type != ElementType.HELMET || !(evt instanceof RenderGameOverlayEvent.Post) || !haveGlasses) return;

        RenderState.beginOverlay();
        try {
            if (!isPowered || lastBind == null) {
                noPowerRender.render(null, 0, 0, 0);
                return;
            }
            // GL11.glScaled(evt.resolution.getScaledWidth_double()/512D,
            // evt.resolution.getScaledHeight_double()/512D*16D/9D, 0);
            for (IRenderableWidget renderable : renderables.values()) {
                if (renderable.shouldWidgetBeRendered()) renderable.render(null, 0, 0, 0);
            }
        } finally {
            RenderState.endOverlay();
        }
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if (!isPowered || !haveGlasses || lastBind == null) return;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        EntityLivingBase viewEntity = Minecraft.getMinecraft().renderViewEntity != null
                ? Minecraft.getMinecraft().renderViewEntity
                : player;
        double playerX = viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * event.partialTicks;
        double playerY = viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * event.partialTicks;
        double playerZ = viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * event.partialTicks;

        RenderState.beginWorld();
        try {
            GL11.glTranslated(-playerX, -playerY, -playerZ);
            GL11.glTranslated(lastBind.x, lastBind.y, lastBind.z);

            for (IRenderableWidget renderable : renderablesWorld.values()) {
                if (renderable.shouldWidgetBeRendered())
                    renderable.render(player, playerX - lastBind.x, playerY - lastBind.y, playerZ - lastBind.z);
            }
        } finally {
            RenderState.endWorld();
        }
    }

    public static MovingObjectPosition getBlockCoordsLookingAt(EntityPlayer player, int distance) {
        MovingObjectPosition objectMouseOver;
        objectMouseOver = player.rayTrace(distance, 1);
        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
            return objectMouseOver;
        }
        return null;
    }

    public void setPowered(boolean isPowered) {
        this.isPowered = isPowered;
    }

    private IRenderableWidget getNoPowerRender() {
        Text t = new Text();
        t.setText("NO POWER");
        t.setAlpha(0.5);
        t.setScale(1);
        t.setColor(1, 0, 0);
        return t.getRenderable();
    }

}
