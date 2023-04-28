package com.bymarcin.openglasses.surface.widgets.core.luafunction;

import com.bymarcin.openglasses.lua.LuaFunction;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.widgets.core.attribute.ITextable;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

public class SetText extends LuaFunction {

    @Override
    public Object[] call(Context context, Arguments arguments) {
        Widget widget = getSelf().getWidget();
        if (widget instanceof ITextable) {
            ((ITextable) widget).setText(arguments.checkString(0));
            getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());
            return null;
        }
        throw new RuntimeException("Component does not exists!");
    }

    @Override
    public String getName() {
        return "setText";
    }

}
