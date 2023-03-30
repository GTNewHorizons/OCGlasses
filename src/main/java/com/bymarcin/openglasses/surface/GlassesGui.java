package com.bymarcin.openglasses.surface;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket;
import com.bymarcin.openglasses.network.packet.GlassesEventPacket.EventType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlassesGui extends GuiContainer {

    private EntityPlayer player;
    private long dragTimer = 0;

    public GlassesGui(EntityPlayer player) {
        super(new GlassesContainer());
        this.player = player;
    }

    @Override
    public void initGui() {
        // super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        // Empty GUI
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        // Empty GUI
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        // Empty GUI
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        GlassesNetworkRegistry.packetHandler.sendToServer(
                new GlassesEventPacket(EventType.INTERACT_OVERLAY, null, player, x, y, button, 0, '-', -1));
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        // Need to limit how often the packet is sent
        if (dragTimer != time) {
            GlassesNetworkRegistry.packetHandler.sendToServer(
                    new GlassesEventPacket(EventType.INTERACT_OVERLAY, null, player, x, y, button, 1, '-', -1));
            dragTimer = time;
        }
    }

    @Override
    protected void keyTyped(char character, int button) {
        if (button == 1) { //
            super.keyTyped(character, button);
        } else {
            GlassesNetworkRegistry.packetHandler.sendToServer(
                    new GlassesEventPacket(
                            EventType.KEYBOARD_INTERACT_OVERLAY,
                            null,
                            player,
                            -1,
                            -1,
                            -1,
                            -1,
                            character,
                            button));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
