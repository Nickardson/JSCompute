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

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public class GuiTerminalComputer extends GuiContainer {
    /**
     * The font to render terminal inputText in.
     */
    private static UnicodeFontRenderer font = null;

    /**
     * Whether keys are sent to the developer console.
     */
    private boolean devConsoleOpen = false;

    /**
     * Developer console field inputText.
     */
    private String inputText = "";

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

    /**
     * The displayed lines.  The conversion from the stored computer data to a character array is somewhat inefficient, so updateLines should be called before rendering.
     */
    private char[][] lines;

    public GuiTerminalComputer(TileEntityTerminalComputer entityTerminalComputer) {
        super(new ContainerTerminalComputer(entityTerminalComputer));

        if (font == null) {
            font = UnicodeFontRenderer.fromInputStream(GuiTerminalComputer.class.getResourceAsStream("/assets/jscomputing/fonts/Inconsolata.ttf"), 20, Font.PLAIN);
        }

        this.computer = (ClientTerminalComputer) entityTerminalComputer.getClientComputer();

        if (computer != null) {
            screenWidth = computer.getWidth();
            screenHeight = computer.getHeight();
            lines = new char[screenHeight][screenWidth];
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

    private void updateLines() {
        if (computer.pollUpdated()) {
            byte[][] rawLines = computer.getLines();

            for (int y = 0; y < screenHeight; y++) {
                for (int x = 0; x < screenWidth; x++) {
                    // Turn byte into unsigned byte, then cast to char.
                    lines[y][x] = (char) (rawLines[y][x] & 0xFF);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        int xPos = 10,
            yPos = 10;

        updateLines();

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
