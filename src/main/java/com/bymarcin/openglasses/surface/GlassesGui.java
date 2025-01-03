package com.bymarcin.openglasses.surface;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import com.bymarcin.openglasses.network.GlassesNetworkRegistry;
import com.bymarcin.openglasses.network.packet.CloseOverlayPacket;
import com.bymarcin.openglasses.network.packet.InteractOverlayPacket;
import com.bymarcin.openglasses.network.packet.KeyboardInteractOverlayPacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlassesGui extends GuiContainer {

    private EntityPlayer player;
    private boolean holdScreen = true;
    private long dragTimer = 0;

    public GlassesGui(EntityPlayer player) {
        super(new GlassesContainer());
        this.player = player;
    }

    @Override
    public void initGui() {
        // Empty GUI
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
        boolean keyState = Keyboard
                .isKeyDown(com.bymarcin.openglasses.event.ClientKeyboardEvents.interactGUIKey.getKeyCode());
        if (keyState && holdScreen) { // Flag to keep the GUI open when it wasn't opened with the interactGUIKey
            holdScreen = false;
        }
        if (!keyState && !holdScreen) {
            player.closeScreen();
        }
        // Empty GUI
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        GlassesNetworkRegistry.packetHandler.sendToServer(new InteractOverlayPacket(player, x, y, button, 0));
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        // Need to limit how often the packet is sent
        if (dragTimer != time) {
            GlassesNetworkRegistry.packetHandler.sendToServer(new InteractOverlayPacket(player, x, y, button, 1));
            dragTimer = time;
        }
    }

    @Override
    protected void keyTyped(char character, int button) {
        if (button == 1) { // Escape to close the window
            super.keyTyped(character, button);
        } else {
            GlassesNetworkRegistry.packetHandler
                    .sendToServer(new KeyboardInteractOverlayPacket(player, character, button));
        }
    }

    @Override
    public void onGuiClosed() {
        GlassesNetworkRegistry.packetHandler.sendToServer(new CloseOverlayPacket(player));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
