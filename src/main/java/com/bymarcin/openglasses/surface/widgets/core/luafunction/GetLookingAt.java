package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ILookable;

public class GetLookingAt extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof ILookable) {
            return new Object[] { ((ILookable) widget).getLookingAtX(), ((ILookable) widget).getLookingAtY(),
                    ((ILookable) widget).getLookingAtZ(), ((ILookable) widget).isLookingAtEnable() };
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "getLookingAt";
    }

}
