package com.nickardson.jscomputing.client.gui;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.client.rendering.RenderUtilities;
import com.nickardson.jscomputing.client.rendering.ScreenRenderer;
import com.nickardson.jscomputing.common.computers.ClientTerminalComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class GuiTerminalComputer extends AbstractGuiTerminalComputer {
    public static int BACK_WIDTH = 560;
    public static int BACK_HEIGHT = 470;
    public static int TEXT_OFFSET_X = 28;
    public static int TEXT_OFFSET_Y = 22;

    private static ResourceLocation back;
    private ScreenRenderer screenRenderer;

    /**
     * The Computer object this GUI represents.
     */
    private ClientTerminalComputer computer;

    public GuiTerminalComputer(TileEntityTerminalComputer entity) {
        super(new ContainerTerminalComputer(entity));

        this.computer = (ClientTerminalComputer) entity.getClientComputer();

        if (this.computer != null) {
            this.screenRenderer = new ScreenRenderer(this.computer);
        }

        if (back == null) {
            back = new ResourceLocation(JSComputingMod.ASSET_ID, "textures/gui/terminal.png");
        }
    }

    private void draw() {
        int x = RenderUtilities.getCenteredX(BACK_WIDTH),
            y = RenderUtilities.getCenteredY(BACK_HEIGHT);

        RenderUtilities.bindImage(back);
        RenderUtilities.drawBoundImage(x, y, BACK_WIDTH, BACK_HEIGHT);

        glPushMatrix();
        {
            glTranslated(x + TEXT_OFFSET_X, y + TEXT_OFFSET_Y, 0);
            screenRenderer.draw();
        }
        glPopMatrix();
    }

    private boolean firstRender = true;

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        RenderUtilities.prepare2D();

        draw();

        if (computer.pollUpdated() || firstRender) {
            firstRender = false;
            screenRenderer.updateLines(computer.getLines());
        }

        RenderUtilities.unprepare2D();
    }
}
