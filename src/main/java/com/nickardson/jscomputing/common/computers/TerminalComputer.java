package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketScreenUpdate;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.javascript.api.APIComputer;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrint;
import com.nickardson.jscomputing.javascript.methods.APIFunctionPrintToScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import org.mozilla.javascript.ScriptableObject;

/**
 * A JavaScriptComputer with a physical TileEntity, and a basic DOS interface.
 */
public class TerminalComputer extends JavaScriptComputer implements IScreenedComputer {

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 25;
    }

    private char[][] lines;

    private int cursorX = 0;
    private int cursorY = 9;

    public void setLines(char[][] lines) {
        linesUpdated = true;
        this.lines = lines;
        tickTerminalLines();
    }

    public char[][] getLines() {
        return lines;
    }

    public static int LINE_SEND_TICK_INTERVAL = 2;
    private int lastLineSend = 0;

    private boolean linesUpdated;
    public boolean isLinesUpdated() {
        return linesUpdated;
    }

    @Override
    public void tickTerminalLines() {
        if (isLinesUpdated() && lastLineSend >= LINE_SEND_TICK_INTERVAL) {
            linesUpdated = false;
            lastLineSend = 0;

            PacketScreenUpdate packet = new PacketScreenUpdate(lines);

            for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
                if (((ContainerTerminalComputer) player.openContainer).getComputer() == this) {
                    ChannelHandler.sendTo(packet, player);
                }
            }
        }
    }

    @Override
    public int getCursorX() {
        return cursorX;
    }

    @Override
    public int getCursorY() {
        return cursorY;
    }

    @Override
    public void setCursor(int x, int y) {
        this.cursorY = Math.min(Math.max(x, 0), getWidth() - 1);
        this.cursorY = Math.min(Math.max(y, 0), getHeight() - 1);
    }

    private TileEntityTerminalComputer tileEntity;

    public TerminalComputer(int id, TileEntityTerminalComputer entityComputer) {
        super(id);

        this.lines = new char[getHeight()][getWidth()];

        /*
        new char[][] {
                "Testing this amazing          ".toCharArray(),
                "new rendering component thing ".toCharArray(),
        };
        */

        this.tileEntity = entityComputer;
    }

    public TileEntityTerminalComputer getTileEntity() {
        return tileEntity;
    }

    @Override
    public void init() {
        getScope().defineProperty("print", new APIFunctionPrintToScreen(this), ScriptableObject.READONLY);
        getScope().defineProperty("stdout", new APIFunctionPrint(), ScriptableObject.READONLY);
        getScope().defineProperty("computer", new APIComputer(tileEntity), ScriptableObject.READONLY);

        super.init();
    }

    @Override
    public void tick() {
        tickTerminalLines();
        lastLineSend++;
    }

    @Override
    public void onPlayerOpenGui() {
        lastLineSend = LINE_SEND_TICK_INTERVAL;
        tickTerminalLines();
    }
}
