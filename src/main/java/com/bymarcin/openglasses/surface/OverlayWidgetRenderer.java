package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.utils.RenderState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Base for the widgets drawn on the HUD. Subclasses only emit geometry in draw() - the matrix, the
// visibility check and the GL mode are handled here, and ClientSurface rolls the pass back after.
@SideOnly(Side.CLIENT)
public abstract class OverlayWidgetRenderer implements IRenderableWidget {

    protected final Widget widget;

    protected OverlayWidgetRenderer(Widget widget) {
        this.widget = widget;
    }

    @Override
    public final RenderType getRenderType() {
        return RenderType.GameOverlayLocated;
    }

    @Override
    public boolean shouldWidgetBeRendered() {
        return widget.isVisible();
    }

    @Override
    public final void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
        GL11.glPushMatrix();
        setupMode();
        draw();
        GL11.glPopMatrix();
    }

    // override when the widget needs textures
    protected void setupMode() {
        RenderState.shapes();
    }

    protected abstract void draw();
}
