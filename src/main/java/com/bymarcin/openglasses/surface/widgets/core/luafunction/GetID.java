package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class GetID extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        return new Object[] { getSelf().getWidgetRef() };
    }

    @Override
    public String getName() {
        return "getID";
    }

}
