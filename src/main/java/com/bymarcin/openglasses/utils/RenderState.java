package com.bymarcin.openglasses.utils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Every GL state change the glasses renderer makes goes through here.
//
// A pass is wrapped in begin*()/end*(), which saves the caller's state and restores it afterward,
// so nothing the widgets set can leak into the rest of the frame. Widgets only pick the mode they
// draw in and never restore anything themselves.
@SideOnly(Side.CLIENT)
public class RenderState {

    private static final int OVERLAY_ATTRIBUTES = GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT;
    // GL_LINE_BIT because Line3D changes glLineWidth
    private static final int WORLD_ATTRIBUTES = OVERLAY_ATTRIBUTES | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_LINE_BIT;

    private RenderState() {}

    public static void beginOverlay() {
        GL11.glPushAttrib(OVERLAY_ATTRIBUTES);
        GL11.glPushMatrix();
        shapes();
    }

    public static void endOverlay() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void beginWorld() {
        GL11.glPushAttrib(WORLD_ATTRIBUTES);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        shapes();
    }

    public static void endWorld() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    // untextured geometry, what most widgets draw
    public static void shapes() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    // fonts and item icons
    public static void textured() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public static void depthTest(boolean enabled) {
        if (enabled) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }
}
