package com.bymarcin.openglasses.lua;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;

import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.utils.Location;

public abstract class LuaFunction implements Value {

    LuaReference ref;

    public LuaFunction() {}

    LuaFunction(int id, Location loc) {
        ref = new LuaReference(id, loc);
    }

    public abstract String getName();

    public void setRef(LuaReference ref) {
        this.ref = ref;
    }

    public LuaReference getSelf() {
        return ref;
    }

    @Override
    public void load(NBTTagCompound nbt) {
        ref = new LuaReference().readFromNBT(nbt.getCompoundTag("ref"));
    }

    @Override
    public void save(NBTTagCompound nbt) {
        NBTTagCompound refTag = new NBTTagCompound();
        ref.writeToNBT(refTag);
        nbt.setTag("ref", refTag);
    }

    @Override
    public Object apply(Context context, Arguments arguments) {
        throw new RuntimeException("You can't replace this function");
    }

    @Override
    public void unapply(Context context, Arguments arguments) {
        throw new RuntimeException("You can't replace this function");
    }

    @Override
    public void dispose(Context context) {}

}
