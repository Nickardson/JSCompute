package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.client.rendering.RenderUtilities;
import com.nickardson.jscomputing.client.rendering.UnicodeFontRenderer;
import com.nickardson.jscomputing.common.computers.ClientTerminalComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerInput;
import com.nickardson.jscomputing.common.network.PacketComputerKey;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class GuiTerminalComputer extends GuiContainer {
    /**
     * The font to render terminal inputText in.
     */
    private UnicodeFontRenderer font = null;

    /**
     * Whether keys are sent to the developer console.
     */
    private boolean devConsoleOpen = false;

    /**
     * Developer console field inputText.
     */
    private String inputText = "";

    /**
     * The TileEntity this GUI represents.
     */
    private TileEntityTerminalComputer terminalComputer;

    /**
     * The Computer object this GUI represents.
     */
    private ClientTerminalComputer computer;

    /**
     * Width, in characters, of the terminal screen.
     */
    private int screenWidth = 0;

    /**
     * Height, in characters, of the terminal screen.
     */
    private int screenHeight = 0;

    public GuiTerminalComputer(TileEntityTerminalComputer entityTerminalComputer) {
        super(new ContainerTerminalComputer(entityTerminalComputer));

        try {
            this.font = new UnicodeFontRenderer(Font.createFont(Font.TRUETYPE_FONT, GuiTerminalComputer.class.getResourceAsStream("/assets/jscomputing/fonts/Inconsolata.ttf")).deriveFont(0, 20));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.terminalComputer = entityTerminalComputer;

        this.computer = (ClientTerminalComputer) entityTerminalComputer.getClientComputer();

        if (computer != null) {
            screenWidth = computer.getWidth();
            screenHeight = computer.getHeight();
        }
    }

    /**
     * Gets whether the given LWJGL Key ID is a valid, displayable character.
     * @param key The LWJGL Key ID of the key to check.
     * @return Whether the key is valid input.
     */
    private boolean isValidDevConsoleKey(int key) {
        return (key >= Keyboard.KEY_1 && key <= Keyboard.KEY_EQUALS) ||
                (key >= Keyboard.KEY_Q && key <= Keyboard.KEY_RBRACKET) ||
                (key >= Keyboard.KEY_A && key <= Keyboard.KEY_APOSTROPHE) ||
                (key >= Keyboard.KEY_Z && key <= Keyboard.KEY_SLASH) ||
                (key >= Keyboard.KEY_NUMPAD7 && key <= Keyboard.KEY_DECIMAL) ||
                (key == Keyboard.KEY_GRAVE) ||
                (key == Keyboard.KEY_BACKSLASH) ||
                (key == Keyboard.KEY_DIVIDE) ||
                (key == Keyboard.KEY_MULTIPLY) ||
                (key == Keyboard.KEY_SPACE);
    }

    /**
     * Gets whether the given LWJGL Key ID should be consumed by the computer.
     * @param key The LWJGL Key ID of the key to check.
     * @return Whether the key is valid input.
     */
    private boolean isValidComputerKey(int key) {
        return key != Keyboard.KEY_ESCAPE;
    }
    
    @Override
    protected void keyTyped(char character, int key) {
        if (key == Keyboard.KEY_GRAVE) {
            devConsoleOpen = !devConsoleOpen;
            return;
        }

        if (devConsoleOpen) {
            if (key == Keyboard.KEY_BACK && inputText.length() > 0) {
                inputText = inputText.substring(0, inputText.length() - 1);
            } else if (key == Keyboard.KEY_RETURN) {
                onInput(inputText);
                inputText = "";
            } else if (isValidDevConsoleKey(key)) {
                inputText += Character.toString(character);
            } else {
                super.keyTyped(character, key);
            }
        } else {
            if (isValidComputerKey(key)) {
                ChannelHandler.sendToServer(new PacketComputerKey(key, character, true));
            } else {
                super.keyTyped(character, key);
            }
        }
    }

    /**
     * Called when input is taken from the Developer console.
     * @param text The input.
     */
    public void onInput(String text) {
        ChannelHandler.sendToServer(new PacketComputerInput(text));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        int xPos = 10,
            yPos = 10;
        char[][] lines = computer.getLines();

        glEnable(GL_TEXTURE_2D);
        for (int y = 0; y < screenHeight; y++) {
            String s = new String(lines[y]);

            if (font != null) {
                font.drawString(s, xPos, yPos, 0xFFFFFF);
            } else {
                // Fallback to minecraft font renderer
                fontRendererObj.drawString(s, xPos, yPos, 0xFFFFFF);
            }
            yPos += 20;
        }

        if (devConsoleOpen) {
            fontRendererObj.drawString("> " + inputText, RenderUtilities.getWidth() / 2 - fontRendererObj.getStringWidth(inputText) / 2, RenderUtilities.getHeight() / 2 - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
        }

        RenderUtilities.unprepare2D();
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
