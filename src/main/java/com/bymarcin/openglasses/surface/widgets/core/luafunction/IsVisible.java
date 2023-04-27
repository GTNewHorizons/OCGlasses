package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class IsVisible extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget != null) {
            return new Object[] { widget.isVisible() };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "isVisible";
    }

}
