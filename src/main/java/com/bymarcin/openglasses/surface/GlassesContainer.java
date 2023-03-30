package com.bymarcin.openglasses.surface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GlassesContainer extends Container {

    public GlassesContainer() {

    }

    @Override

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
