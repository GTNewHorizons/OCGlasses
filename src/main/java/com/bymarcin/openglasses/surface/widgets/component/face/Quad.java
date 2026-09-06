package com.bymarcin.openglasses.surface.widgets.component.face;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.OverlayWidgetRenderer;
import com.bymarcin.openglasses.surface.WidgetType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class Quad extends TriangleWidget {

    public Quad() {
        x = new float[4];
        y = new float[4];
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        buff.writeFloat(x[3]);
        buff.writeFloat(y[3]);
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        x[3] = buff.readFloat();
        y[3] = buff.readFloat();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderQuad();
    }

    @SideOnly(Side.CLIENT)
    class RenderQuad extends OverlayWidgetRenderer {

        RenderQuad() {
            super(Quad.this);
        }

        @Override
        protected void draw() {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL11.GL_QUADS);
            tessellator.setColorRGBA_F(r, g, b, alpha);
            tessellator.addVertex(x[0], y[0], 0);
            tessellator.addVertex(x[1], y[1], 0);
            tessellator.addVertex(x[2], y[2], 0);
            tessellator.addVertex(x[3], y[3], 0);
            tessellator.draw();
        }
    }

    @Override
    public WidgetType getType() {
        return WidgetType.QUAD;
    }
}
