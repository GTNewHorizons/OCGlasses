package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.IScalable;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class SetScale extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof IScalable) {
            ((IScalable) widget).setScale(arguments.checkDouble(0));
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "setScale";
    }

}
