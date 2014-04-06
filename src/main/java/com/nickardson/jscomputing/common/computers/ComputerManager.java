package com.nickardson.jscomputing.common.computers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Manages the running computers.
 */
public class ComputerManager {
    private static Map<Integer, IClientComputer> clientComputers = new HashMap<Integer, IClientComputer>();
    private static Map<Integer, IServerComputer> serverComputers = new HashMap<Integer, IServerComputer>();

    /**
     * Get a map of the Client computers.
     */
    public static Map<Integer, IClientComputer> getClientComputers() {
        return clientComputers;
    }
    /**
     * Get a map of the Server computers.
     */
    public static Map<Integer, IServerComputer> getServerComputers() {
        return serverComputers;
    }

    /**
     * Gets the Client computer with the given ID.
     * @param id The computer's ID
     */
    public static IClientComputer getClientComputer(int id) {
        if (clientComputers.containsKey(id)) {
            return clientComputers.get(id);
        } else {
            return null;
        }
    }
    /**
     * Gets the Server computer with the given ID.
     * @param id The computer's ID
     */
    public static IServerComputer getServerComputer(int id) {
        if (serverComputers.containsKey(id)) {
            return serverComputers.get(id);
        } else {
            return null;
        }
    }

    public static IComputer getComputer(int id) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return getClientComputer(id);
        } else {
            return getServerComputer(id);
        }
    }

    /**
     * Removes the Client computer with the given ID.
     * @param id The computer's ID
     */
    public static void removeClientComputer(int id) {
        if (clientComputers.containsKey(id)) {
            clientComputers.get(id);
        }
    }
    /**
     * Removes the Server computer with the given ID.
     * @param id The computer's ID
     */
    public static void removeServerComputer(int id) {
        if (serverComputers.containsKey(id)) {
            serverComputers.remove(id);
        }
    }

    /**
     * Removes any Client or Server computer with the given ID.
     * @param id The computer's ID
     */
    public static void removeComputer(int id) {
        removeClientComputer(id);
        removeServerComputer(id);
    }

    /**
     * Adds a Client computer.
     * @param computer The computer to add.
     */
    public static void addClientComputer(IClientComputer computer) {
        clientComputers.put(computer.getID(), computer);
    }
    /**
     * Adds a Server computer.
     * @param computer The computer to add.
     */
    public static void addServerComputer(IServerComputer computer) {
        serverComputers.put(computer.getID(), computer);
    }

    public static File getWorldDirectory() {
        return DimensionManager.getWorlds()[0].getSaveHandler().getWorldDirectory();
    }

    private static int ID = 1337;
    public static int getNextAvailableID() {
        File f = new File(getWorldDirectory(), "jscomputingid.txt");
        try {
            if (f.exists()) {
                Scanner s = new Scanner(f).useDelimiter("\\Z");
                ID = Integer.parseInt(s.next()) + 1;
                s.close();
            }

            PrintWriter writer = new PrintWriter(f);
            writer.println(ID);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ID;
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
