package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IThroughVisibility;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;
import com.bymarcin.openglasses.utils.OGUtils;
import com.bymarcin.openglasses.utils.RenderState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Base for the widgets drawn in the world. Same idea as OverlayWidgetRenderer, plus view distance
// and looking-at culling and the depth test rule.
@SideOnly(Side.CLIENT)
public abstract class WorldWidgetRenderer implements IRenderableWidget {

    private static final int LOOKING_AT_RANGE = 200;

    protected final Widget widget;

    protected WorldWidgetRenderer(Widget widget) {
        this.widget = widget;
    }

    @Override
    public final RenderType getRenderType() {
        return RenderType.WorldLocated;
    }

    @Override
    public boolean shouldWidgetBeRendered() {
        return widget.isVisible();
    }

    @Override
    public final void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
        if (isCulled(player, playerX, playerY, playerZ)) return;

        GL11.glPushMatrix();
        setupMode();
        RenderState.depthTest(!isVisibleThroughObjects());
        draw(player);
        GL11.glPopMatrix();
    }

    // override when the widget needs textures
    protected void setupMode() {
        RenderState.shapes();
    }

    protected abstract void draw(EntityPlayer player);

    private boolean isVisibleThroughObjects() {
        return widget instanceof IThroughVisibility && ((IThroughVisibility) widget).isVisibleThroughObjects();
    }

    // each filter applies only to widgets implementing the matching attribute
    private boolean isCulled(EntityPlayer player, double playerX, double playerY, double playerZ) {
        if (widget instanceof IViewDistance) {
            IViewDistance ranged = (IViewDistance) widget;
            if (!OGUtils.inRange(
                    playerX,
                    playerY,
                    playerZ,
                    ranged.getPosX(),
                    ranged.getPosY(),
                    ranged.getPosZ(),
                    ranged.getDistanceView()))
                return true;
        }
        if (widget instanceof ILookable) {
            ILookable lookable = (ILookable) widget;
            if (lookable.isLookingAtEnable()) {
                MovingObjectPosition hit = ClientSurface.getBlockCoordsLookingAt(player, LOOKING_AT_RANGE);
                if (hit == null || hit.blockX != lookable.getLookingAtX()
                        || hit.blockY != lookable.getLookingAtY()
                        || hit.blockZ != lookable.getLookingAtZ())
                    return true;
            }
        }
        return false;
    }
}
