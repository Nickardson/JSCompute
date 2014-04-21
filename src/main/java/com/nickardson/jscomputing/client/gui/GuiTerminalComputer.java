package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.client.rendering.ImageFontRenderer;
import com.nickardson.jscomputing.client.rendering.RenderUtilities;
import com.nickardson.jscomputing.common.computers.ClientTerminalComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerKey;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.utility.ComputerUtilities;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

public class GuiTerminalComputer extends GuiContainer {
    public static int BACK_WIDTH = 560;
    public static int BACK_HEIGHT = 470;
    public static int TEXT_OFFSET_X = 28;
    public static int TEXT_OFFSET_Y = 22;
    public static int LINE_SEPARATION = 20;

    public static boolean USE_DISPLAY_LISTS = true;

    private static ImageFontRenderer font;
    private static ResourceLocation back;

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
            font = new ImageFontRenderer(new ResourceLocation(JSComputingMod.ASSET_ID, "fonts/font-half.png"), 160, 294, 10, 21);
        }
        if (back == null) {
            back = new ResourceLocation(JSComputingMod.ASSET_ID, "textures/gui/terminal.png");
        }

        this.computer = (ClientTerminalComputer) entityTerminalComputer.getClientComputer();

        if (computer != null) {
            screenWidth = computer.getWidth();
            screenHeight = computer.getHeight();
            lines = new char[screenHeight][screenWidth];
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (ComputerUtilities.isValidComputerKey(key)) {
            ChannelHandler.sendToServer(new PacketComputerKey(key, character, true));
        } else {
            super.keyTyped(character, key);
        }
    }

    private void updateLines(boolean force) {
        if (force || computer.pollUpdated()) {
            byte[][] rawLines = computer.getLines();

            for (int y = 0; y < screenHeight; y++) {
                for (int x = 0; x < screenWidth; x++) {
                    // Turn byte into unsigned byte, then cast to char.
                    lines[y][x] = (char) (rawLines[y][x] & 0xFF);
                }
            }

            if (USE_DISPLAY_LISTS) {
                updateDisplayList();
            }
        }
    }

    int displayList = -1;
    int cursorDisplayList = -1;

    private void updateDisplayList() {
        if (displayList != -1) {
            glDeleteLists(displayList, 1);
        }

        displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);
        {
            render();
        }
        glEndList();
    }

    private void createCursorDisplayList() {
        if (cursorDisplayList == -1) {
            cursorDisplayList = glGenLists(1);
            glNewList(cursorDisplayList, GL_COMPILE);
            {
                renderCursor();
            }
            glEndList();
        }
    }

    private void renderCursor() {
        glLineWidth(2);
        glColor3d(1.0, 1.0, 1.0);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);

        glBegin(GL_LINES);
        {
            glVertex2d(2, 0);
            glVertex2d(2, font.getCharacterHeight());
        }
        glEnd();
    }

    private void render() {
        glColor3f(1, 1, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        mc.renderEngine.bindTexture(back);
        RenderUtilities.drawBoundImage(0, 0, BACK_WIDTH, BACK_HEIGHT);

        font.begin();

        for (int y = 0; y < screenHeight; y++) {
            font.drawString(lines[y], TEXT_OFFSET_X, TEXT_OFFSET_Y + y * LINE_SEPARATION);
        }

        glDisable(GL_TEXTURE_2D);
    }

    private void draw() {
        int x = (int) (RenderUtilities.getWidth() / 2.0 - BACK_WIDTH / 2.0),
            y = (int) (RenderUtilities.getHeight() / 2.0 - BACK_HEIGHT / 2.0);

        glPushMatrix();
        {
            glTranslated(x, y, 0);

            if (USE_DISPLAY_LISTS) {
                if (displayList == -1) {
                    // Load textures for the first time.
                    mc.renderEngine.bindTexture(back);
                    mc.renderEngine.bindTexture(font.getResource());
                    updateLines(true);
                }

                glCallList(displayList);
            } else {
                render();
            }

            if (computer.isCursorVisible() && Math.floor(System.currentTimeMillis() / 750) % 2 == 0) {
                glPushMatrix();
                {
                    glTranslated(TEXT_OFFSET_X + computer.getCursorX() * font.getCharacterWidth(), TEXT_OFFSET_Y + computer.getCursorY() * LINE_SEPARATION, 0);
                    if (USE_DISPLAY_LISTS) {
                        if (cursorDisplayList == -1) {
                            createCursorDisplayList();
                        }

                        glCallList(cursorDisplayList);
                    } else {
                        renderCursor();
                    }
                }
                glPopMatrix();
            }
        }
        glPopMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        updateLines(false);
        draw();

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
