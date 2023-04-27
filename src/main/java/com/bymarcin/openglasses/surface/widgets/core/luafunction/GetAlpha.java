package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IAlpha;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class GetAlpha extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof IAlpha) {
            return new Object[] { ((IAlpha) widget).getAlpha() };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getAlpha";
    }

}
