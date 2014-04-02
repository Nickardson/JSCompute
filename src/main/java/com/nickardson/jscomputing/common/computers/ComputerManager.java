package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.inventory.ContainerComputer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerManager {
    private static Map<Integer, IComputer> computers = new HashMap<Integer, IComputer>();

    public static boolean has(int tempID) {
        return computers.containsKey(tempID);
    }

    public static IComputer get(int tempID) {
        if (has(tempID)) {
            return computers.get(tempID);
        } else {
            return null;
        }
    }

    public static void add(IComputer computer) {
        computers.put(computer.getTempID(), computer);
    }

    public static void remove(int tempID) {
        computers.remove(tempID);
    }

    public static void remove(IComputer computer) {
        remove(computer.getTempID());
    }

    /**
     * Gets a list of players who have the given type of container open.
     */
    public static List<EntityPlayerMP> getPlayersWithContainer(Class<? extends Container> clazz) {
        List<EntityPlayerMP> ls = new ArrayList<EntityPlayerMP>();

        List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (Object p : players) {
            EntityPlayerMP player = (EntityPlayerMP) p;
            if (clazz.isInstance(player.openContainer)) {
                ls.add(player);
            }
        }

        return ls;
    }
}
