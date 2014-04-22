package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.inventory.AbstractContainerComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.IPacket;
import com.nickardson.jscomputing.common.network.PacketCursorUpdate;
import com.nickardson.jscomputing.common.network.PacketScreenUpdate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

/**
 * A terminal computer which can send screen information to clients.
 */
public abstract class AbstractServerTerminalComputer extends AbstractTerminalComputer implements IServerComputer {
    public AbstractServerTerminalComputer(int id) {
        super(id);
    }

    /**
     * Implementation should return the class of the GUI Container players will have open.
     * @return The container's class.
     */
    public abstract Class<? extends AbstractContainerComputer> getContainerClass();

    /**
     * Gets the number of ticks that must pass before sending a terminal update.
     * @return Number of ticks.
     */
    public int getLineSendTickInterval() {
        return 2;
    }

    /**
     * How many ticks ago the last terminal update was.
     */
    private int lastSend = 0;

    /**
     * The timestamp of the last screen update.
     */
    private long lastUpdate = 0;

    /**
     * Whether the terminal has changed since the last update sent.
     */
    private boolean linesUpdated;

    @Override
    public void sendLines(boolean force) {
        if (force || (lastSend >= getLineSendTickInterval() && (System.currentTimeMillis() - lastUpdate > 70 || lastSend > getLineSendTickInterval() * 3))) {
            lastSend = 0;

            if (linesUpdated) {
                linesUpdated = false;
                sendPacket(createPacketScreenUpdate());
            } else if (isCursorUpdated()) {
                sendPacket(createPacketCursorUpdate());
            }
            setCursorUpdated(false);
        }
    }

    /**
     * Sends the given packet to all players who match canPlayerGetUpdate.
     * @param packet The packet to be sent.
     */
    private void sendPacket(IPacket packet) {
        List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (Object p : players) {
            EntityPlayerMP player = (EntityPlayerMP) p;
            if (canPlayerGetUpdate(player)) {
                IComputer computer = ((IContainerComputer) player.openContainer).getServerComputer();
                if (computer != null && computer.getID() == this.getID()) {
                    ChannelHandler.sendTo(packet, player);
                }
            }
        }
    }

    /**
     * Creates a screen update packet.
     * @return The created packet.
     */
    public PacketScreenUpdate createPacketScreenUpdate() {
        return new PacketScreenUpdate(getID(), getLines(), isCursorVisible(), getCursorX(), getCursorY());
    }

    /**
     * Creates a cursor update packet.
     * @return The created packet.
     */
    public PacketCursorUpdate createPacketCursorUpdate() {
        return new PacketCursorUpdate(getID(), isCursorVisible(), getCursorX(), getCursorY());
    }

    /**
     * Gets whether the given player should be sent screen/cursor update packets.
     * @param player The player to check
     * @return Whether they should receive the update.
     */
    public boolean canPlayerGetUpdate(EntityPlayerMP player) {
        return getContainerClass().isInstance(player.openContainer);
    }

    @Override
    public void updateLines(byte[][] lines) {
        lastUpdate = System.currentTimeMillis();
        linesUpdated = true;
        setLines(lines);
    }

    @Override
    public void tick() {
        sendLines(false);
        lastSend++;
    }

    @Override
    public void onPlayerOpenGui() {
        sendLines(true);
    }
}
