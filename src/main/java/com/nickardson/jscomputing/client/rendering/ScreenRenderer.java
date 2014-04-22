package com.nickardson.jscomputing.client.rendering;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class ScreenRenderer {
    private int lineSeparation = 20;


    private IScreenedComputer computer;
    private ImageFontRenderer font;

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

    public ScreenRenderer(IScreenedComputer computer) {
        this.computer = computer;

        screenWidth = computer.getWidth();
        screenHeight = computer.getHeight();
        lines = new char[screenHeight][screenWidth];

        font = new ImageFontRenderer(new ResourceLocation(JSComputingMod.ASSET_ID, "fonts/font-half.png"), 160, 294, 10, 21);
    }

    public ImageFontRenderer getFont() {
        return font;
    }

    /**
     * Updates the lines this screen is rendering.
     */
    public void updateLines(byte[][] rawLines) {
        for (int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {
                // Turn byte into unsigned byte, then cast to char.
                lines[y][x] = (char) (rawLines[y][x] & 0xFF);
            }
        }

        updateDisplayList();
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

    /**
     * Internal render method.
     */
    private void render() {
        glColor3f(1, 1, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        font.begin();

        for (int y = 0; y < screenHeight; y++) {
            font.drawString(lines[y], 0, y * lineSeparation);
        }
    }

    /**
     * Draws the screen.
     */
    public void draw() {
        if (displayList == -1) {
            // Load textures for the first time.
            font.begin();
            updateDisplayList();
        }
        glCallList(displayList);

        if (computer.isCursorVisible() && Math.floor(System.currentTimeMillis() / 750) % 2 == 0) {
            glPushMatrix();
            {
                glTranslated(computer.getCursorX() * font.getCharacterWidth(), computer.getCursorY() * lineSeparation, 0);
                if (cursorDisplayList == -1) {
                    createCursorDisplayList();
                }

                glCallList(cursorDisplayList);
            }
            glPopMatrix();
        }
    }

}
