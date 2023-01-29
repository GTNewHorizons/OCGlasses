package com.bymarcin.openglasses.surface.widgets.component.face;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IRotatable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;
import com.bymarcin.openglasses.utils.OGUtils;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class Text extends Dot implements ITextable, IRotatable {

    String text = "";
    float rotation;

    public Text() {}

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        ByteBufUtils.writeUTF8String(buff, text);
        buff.writeFloat(rotation);
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        text = ByteBufUtils.readUTF8String(buff);
        rotation = buff.readFloat();
    }

    @Override
    public WidgetType getType() {
        return WidgetType.TEXT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderText();
    }

    class RenderText implements IRenderableWidget {

        int color = OGUtils.getIntFromColor(r, g, b, alpha);
        FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;

        @Override
        public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 100);
            GL11.glScaled(size, size, 0);
            GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
            fontRender.drawString(text, 0, 0, color);
            GL11.glPopMatrix();

        }

        @Override
        public RenderType getRenderType() {
            return RenderType.GameOverlayLocated;
        }

        @Override
        public boolean shouldWidgetBeRendered() {
            return isVisible();
        }

    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
