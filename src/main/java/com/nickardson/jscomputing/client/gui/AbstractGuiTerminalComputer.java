package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerKey;
import com.nickardson.jscomputing.utility.ComputerUtilities;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;

public abstract class AbstractGuiTerminalComputer extends GuiContainer {
    public AbstractGuiTerminalComputer(Container container) {
        super(container);
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (ComputerUtilities.isValidComputerKey(key)) {
            ChannelHandler.sendToServer(new PacketComputerKey(key, character, true));
        } else {
            super.keyTyped(character, key);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
}
