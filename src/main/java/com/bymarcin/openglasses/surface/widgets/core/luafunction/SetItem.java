package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.network.Component;
import li.cil.oc.api.network.Node;
import net.minecraft.item.ItemStack;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IItemable;

import li.cil.oc.api.internal.Database;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Environment;

public class SetItem extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof IItemable) {
            Node node = context.node().network().node(arguments.checkString(0));
            if (node instanceof Component) {
                Environment env = node.host();
                if (env instanceof Database) {
                    ItemStack itemStack = ((Database) env).getStackInSlot(arguments.checkInteger(1) - 1);
                    ((IItemable) widget).setItem(itemStack);
                    getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
                    return null;
                }
            }
            throw new RuntimeException("Not a database");
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "setItem";
    }

}
