package com.bymarcin.openglasses.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.lua.LuaReference;
import com.bymarcin.openglasses.network.packet.TerminalStatusPacket.TerminalStatus;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.surface.Widget;
import com.bymarcin.openglasses.surface.WidgetType;
import com.bymarcin.openglasses.surface.widgets.component.face.Dot;
import com.bymarcin.openglasses.surface.widgets.component.face.ItemIcon;
import com.bymarcin.openglasses.surface.widgets.component.face.Quad;
import com.bymarcin.openglasses.surface.widgets.component.face.SquareWidget;
import com.bymarcin.openglasses.surface.widgets.component.face.Text;
import com.bymarcin.openglasses.surface.widgets.component.face.TriangleWidget;
import com.bymarcin.openglasses.surface.widgets.component.world.Cube3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Dot3D;
import com.bymarcin.openglasses.surface.widgets.component.world.FloatingText;
import com.bymarcin.openglasses.surface.widgets.component.world.Line3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Quad3D;
import com.bymarcin.openglasses.surface.widgets.component.world.Triangle3D;
import com.bymarcin.openglasses.utils.Location;

import cpw.mods.fml.common.Optional;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class OpenGlassesTerminalTileEntity extends TileEntityEnvironment {

    public HashMap<Integer, Widget> widgetList = new HashMap<>();
    int currID = 0;
    Location loc;
    boolean isPowered;

    public OpenGlassesTerminalTileEntity() {
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName())
                .withConnector(OpenGlasses.energyBuffer).create();
    }

    public String getComponentName() {
        return "glasses";
    }

    public Location getTerminalUUID() {
        if (loc != null) {
            return loc;
        }
        return loc = new Location(
                xCoord,
                yCoord,
                zCoord,
                worldObj.provider.dimensionId,
                UUID.randomUUID().getMostSignificantBits());
    }

    public void onGlassesPutOn(String user, int width, int height) {
        if (node != null) {
            node.sendToReachable("computer.signal", "glasses_on", user, width, height);
        }
    }

    public void onGlassesPutOff(String user) {
        if (node != null) {
            node.sendToReachable("computer.signal", "glasses_off", user);
        }
    }

    public void onHudInteract(String user, int x, int y, int button, int type) {
        if (node != null) {
            switch (type) {
                case 0:
                    node.sendToReachable("computer.signal", "hud_click", user, x, y, button);
                    break;
                case 1:
                    node.sendToReachable("computer.signal", "hud_drag", user, x, y, button);
                    break;
            }
        }
    }

    public void onHudInteractKeyboard(String user, char character, int key) {
        if (node != null) {
            node.sendToReachable("computer.signal", "hud_keyboard", user, character, key);
        }
    }

    public void onBlockInteract(String user, int x, int y, int z, int side) {
        if (node != null) {
            node.sendToReachable("computer.signal", "block_interact", user, x, y, z, side);
        }
    }

    public void overlayOpened(String user) {
        if (node != null) {
            node.sendToReachable("computer.signal", "overlay_opened", user);
        }
    }

    public void overlayClosed(String user) {
        if (node != null) {
            node.sendToReachable("computer.signal", "overlay_closed", user);
        }
    }

    @Callback(
            direct = true,
            doc = "function():string... -- Lists the name of all players currently wearing glasses linked to the terminal.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] getBindPlayers(Context context, Arguments args) {
        return ServerSurface.instance.getActivePlayerNames(getTerminalUUID());
    }

    @Callback(direct = true, doc = "function():number -- Returns the number of instantiated widgets.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] getObjectCount(Context context, Arguments args) {
        return new Object[] { widgetList.size() };
    }

    @Callback(
            direct = true,
            doc = "function(id:number):boolean -- Removes the widget with the corresponding id, returning if it was successful.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] removeObject(Context context, Arguments args) {
        int id = args.checkInteger(0);
        return new Object[] { removeWidget(id) };
    }

    @Callback(direct = true, doc = "function() -- Removes all widgets.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] removeAll(Context context, Arguments args) {
        currID = 0;
        widgetList.clear();
        ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), getTerminalUUID());
        return new Object[] {};
    }

    @Callback(direct = true, doc = "function():number -- Generates a new random UUID.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] newUniqueKey(Context context, Arguments args) {
        UUID[] players = ServerSurface.instance.getActivePlayers(loc);
        for (UUID p : players) {
            ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), loc);
            ServerSurface.instance.unsubscribePlayer(p);
        }
        loc.uniqueKey = UUID.randomUUID().getMostSignificantBits();
        return new Object[] { loc.uniqueKey };
    }

    /* Object manipulation */

    @Callback(direct = true, doc = "function():Rect2D -- Adds a new rectangle widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addRect(Context context, Arguments args) {
        Widget w = new SquareWidget();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Dot2D -- Adds a new dot widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addDot(Context context, Arguments args) {
        Widget w = new Dot();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():ItemIcon -- Adds a new item widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addItem(Context context, Arguments args) {
        Widget w = new ItemIcon();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Cube3D -- Adds a new 3D cube widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addCube3D(Context context, Arguments args) {
        Widget w = new Cube3D();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Text3D -- Adds a new floating text widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addFloatingText(Context context, Arguments args) {
        Widget w = new FloatingText();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Triangle2D -- Adds a new triangle widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addTriangle(Context context, Arguments args) {
        Widget w = new TriangleWidget();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Dot3D -- Adds a new 3D dot widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addDot3D(Context context, Arguments args) {
        Widget w = new Dot3D();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Text2D -- Adds a new text label widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addTextLabel(Context context, Arguments args) {
        Widget w = new Text();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Line3D -- Adds a new 3D line widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addLine3D(Context context, Arguments args) {
        Widget w = new Line3D();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Triangle3D -- Adds a new 3D triangle widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addTriangle3D(Context context, Arguments args) {
        Widget w = new Triangle3D();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Quad3D -- Adds a new 3D quad widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addQuad3D(Context context, Arguments args) {
        Widget w = new Quad3D();
        return addWidget(w);
    }

    @Callback(direct = true, doc = "function():Quad2D -- Adds a new quad widget to the render surface.")
    @Optional.Method(modid = "OpenComputers")
    public Object[] addQuad(Context context, Arguments args) {
        Widget w = new Quad();
        return addWidget(w);
    }

    public boolean removeWidget(int id) {
        if (widgetList.containsKey(id) && widgetList.remove(id) != null) {
            ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id), getTerminalUUID());
            return true;
        }
        return false;
    }

    public Object[] addWidget(Widget w) {
        widgetList.put(currID, w);
        ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(currID, w), getTerminalUUID());
        int t = currID;
        currID++;
        return w.getLuaObject(new LuaReference(t, getTerminalUUID()));
    }

    public void updateWidget(int id) {
        Widget w = widgetList.get(id);
        if (w != null) ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(id, w), getTerminalUUID());
    }

    public Widget getWidget(int id) {
        return widgetList.get(id);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("currID", currID);
        NBTTagCompound tag = new NBTTagCompound();
        int size = widgetList.size();
        nbt.setInteger("listSize", size);
        int i = 0;
        for (Entry<Integer, Widget> e : widgetList.entrySet()) {
            NBTTagCompound widget = new NBTTagCompound();
            widget.setString("widgetType", e.getValue().getType().name());
            widget.setInteger("ID", e.getKey());
            NBTTagCompound wNBT = new NBTTagCompound();
            e.getValue().writeToNBT(wNBT);
            widget.setTag("widget", wNBT);
            tag.setTag(String.valueOf(i), widget);
            i++;
        }
        nbt.setTag("widgetList", tag);

        NBTTagCompound tagLoc = new NBTTagCompound();
        if (loc != null) {
            loc.writeToNBT(tagLoc);
            nbt.setTag("uniqueKey", tagLoc);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        widgetList.clear();
        if (nbt.hasKey("currID")) {
            currID = nbt.getInteger("currID");
        }

        if (nbt.hasKey("widgetList") && nbt.hasKey("listSize")) {
            NBTTagCompound list = (NBTTagCompound) nbt.getTag("widgetList");
            int size = nbt.getInteger("listSize");
            for (int i = 0; i < size; i++) {
                if (list.hasKey(String.valueOf(i))) {
                    NBTTagCompound wiget = (NBTTagCompound) list.getTag(String.valueOf(i));
                    if (wiget.hasKey("widgetType") && wiget.hasKey("widget") && wiget.hasKey("ID")) {
                        WidgetType type = WidgetType.valueOf(wiget.getString(("widgetType")));
                        Widget w = type.getNewInstance();
                        w.readFromNBT((NBTTagCompound) wiget.getTag("widget"));
                        widgetList.put(wiget.getInteger("ID"), w);
                    }
                }
            }
        }
        if (nbt.hasKey("uniqueKey")) {
            loc = new Location().readFromNBT((NBTTagCompound) nbt.getTag("uniqueKey"));
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        boolean lastStatus = isPowered;
        if ((node() != null)
                && ((Connector) node()).tryChangeBuffer(-widgetList.size() / 10f * OpenGlasses.energyMultiplier)) {
            isPowered = true;
        } else {
            isPowered = false;
        }

        if (lastStatus != isPowered) {
            ServerSurface.instance
                    .sendPowerInfo(getTerminalUUID(), isPowered ? TerminalStatus.HavePower : TerminalStatus.NoPower);
        }

    }

    public boolean isPowered() {
        return isPowered;
    }
}
