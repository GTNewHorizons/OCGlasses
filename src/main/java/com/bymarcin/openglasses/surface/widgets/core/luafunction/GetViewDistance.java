package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IViewDistance;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class GetViewDistance extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof IViewDistance) {
            return new Object[] { ((IViewDistance) widget).getDistanceView() };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getViewDistance";
    }

}
