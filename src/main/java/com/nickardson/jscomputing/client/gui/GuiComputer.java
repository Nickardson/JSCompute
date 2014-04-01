package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.client.rendering.RenderUtilities;
import com.nickardson.jscomputing.common.inventory.ContainerComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public class GuiComputer extends GuiContainer {

    private String text = "";

    public GuiComputer(EntityPlayer player, TileEntityComputer computer) {
        super(new ContainerComputer(player, computer));
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
            System.out.println("Text input: " + text);
            text = "";
        } else if (isValidKey(key)) {
            text += Character.toString(character);
        } else {
            super.keyTyped(character, key);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        glEnable(GL_TEXTURE_2D);

        fontRendererObj.drawString(text, RenderUtilities.getWidth() / 2 - fontRendererObj.getStringWidth(text) / 2, RenderUtilities.getHeight() / 2 - fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);

        RenderUtilities.unprepare2D();
    }
}
