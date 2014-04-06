package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.client.rendering.RenderUtilities;
import com.nickardson.jscomputing.client.rendering.UnicodeFontRenderer;
import com.nickardson.jscomputing.common.computers.ClientTerminalComputer;
import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerInput;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class GuiTerminalComputer extends GuiContainer {

    private UnicodeFontRenderer font = null;

    private String text = "";

    private TileEntityTerminalComputer terminalComputer;
    private ClientTerminalComputer computer;

    private int screenWidth = 0;
    private int screenHeight = 0;

    public GuiTerminalComputer(EntityPlayer player, TileEntityTerminalComputer entityTerminalComputer) {
        super(new ContainerTerminalComputer(player, entityTerminalComputer));

        //this.font = new UnicodeFontRenderer(new Font("Comic Sans MS", 0, 12));
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

    private boolean isValidKey(int key) {
        return (key >= Keyboard.KEY_1 && key <= Keyboard.KEY_EQUALS) ||
            (key >= Keyboard.KEY_Q && key <= Keyboard.KEY_RBRACKET) ||
            (key >= Keyboard.KEY_A && key <= Keyboard.KEY_APOSTROPHE) ||
            (key >= Keyboard.KEY_Z && key <= Keyboard.KEY_SLASH) ||
            (key >= Keyboard.KEY_NUMPAD7 && key <= Keyboard.KEY_DECIMAL) ||
            key == Keyboard.KEY_GRAVE || key == Keyboard.KEY_BACKSLASH ||
            key == Keyboard.KEY_DIVIDE || key == Keyboard.KEY_MULTIPLY ||
            key == Keyboard.KEY_SPACE;
    }
    
    @Override
    protected void keyTyped(char character, int key) {
        if (key == Keyboard.KEY_BACK && text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        } else if (key == Keyboard.KEY_RETURN) {
            onInput(text);
            text = "";
        } else if (isValidKey(key)) {
            text += Character.toString(character);
        } else {
            super.keyTyped(character, key);
        }
    }

    public void onInput(String text) {
        ChannelHandler.sendToServer(new PacketComputerInput(text));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        glEnable(GL_TEXTURE_2D);

        //char[][] lines = computer.getLines();
        int xPos = 10,
            yPos = 10;
        char[][] lines = computer.getLines();
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

        fontRendererObj.drawString(text, RenderUtilities.getWidth() / 2 - fontRendererObj.getStringWidth(text) / 2, RenderUtilities.getHeight() / 2 - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);

        RenderUtilities.unprepare2D();
    }
}
