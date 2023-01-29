package com.bymarcin.openglasses.surface.widgets.component.face;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.bymarcin.openglasses.surface.IRenderableWidget;
import com.bymarcin.openglasses.surface.RenderType;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IItemable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IPositionable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IRotatable;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;
import com.bymarcin.openglasses.utils.RenderUtils;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class ItemIcon extends Widget implements IItemable, IPositionable, IScalable, IRotatable {

    ItemStack itemStack;
    float x;
    float y;
    float scale = 1.0f;
    float rotation;

    @Override
    public void writeData(ByteBuf buff) {
        ByteBufUtils.writeItemStack(buff, itemStack);
        buff.writeFloat(x);
        buff.writeFloat(y);
        buff.writeFloat(scale);
        buff.writeFloat(rotation);
    }

    @Override
    public void readData(ByteBuf buff) {
        itemStack = ByteBufUtils.readItemStack(buff);
        x = buff.readFloat();
        y = buff.readFloat();
        scale = buff.readFloat();
        rotation = buff.readFloat();
    }

    @Override
    public WidgetType getType() {
        return WidgetType.ITEMICON;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return this.new RenderableIcon();
    }

    @Override
    public double getPosX() {
        return x;
    }

    @Override
    public double getPosY() {
        return y;
    }

    @Override
    public void setPos(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    @Override
    public void setScale(double scale) {
        this.scale = (float) scale;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public boolean setItem(ItemStack newItem) {
        if (newItem == null || newItem.getItem() == null) {
            return false;
        }
        this.itemStack = newItem;

        return true;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @SideOnly(Side.CLIENT)
    class RenderableIcon implements IRenderableWidget {

        @Override
        public void render(EntityPlayer player, double playerX, double playerY, double playerZ) {
            try {
                RenderUtils.renderItemStackOnGUI(itemStack, x, y, scale, rotation);
            } catch (RuntimeException e) {
                itemStack = null;
            }
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
}
