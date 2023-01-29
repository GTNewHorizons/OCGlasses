package com.bymarcin.openglasses.surface.widgets.core.attribute;

import net.minecraft.item.ItemStack;

public interface IItemable extends IAttribute {

    boolean setItem(ItemStack itemStack);

    ItemStack getItem();
}
