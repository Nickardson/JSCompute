package com.nickardson.jscomputing.common.computers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    /**
     * Gets a computer on the current side (client / server).
     * @param id The ID of the computer.
     * @return The found computer, or null.
     */
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

    /**
     * Changes the ID of both client and server computers.
     * @param oldID The ID of the computers.
     * @param newID The new ID to assign.
     */
    public static void changeID(int oldID, int newID) {
        if (newID == oldID) {
            return;
        }

        IClientComputer client = ComputerManager.getClientComputer(oldID);
        IServerComputer server = ComputerManager.getServerComputer(oldID);

        ComputerManager.removeComputer(oldID);

        if (client != null) {
            client.setID(newID);
            ComputerManager.addClientComputer(client);
        }
        if (server != null) {
            server.setID(newID);
            ComputerManager.addServerComputer(server);
        }
    }

    /**
     * Gets the directory of the main world, or null if no worlds are available.
     * @return The file location of the world.
     */
    public static File getWorldDirectory() {
        try {
            return DimensionManager.getWorlds()[0].getSaveHandler().getWorldDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The initial ID of created computers.
     */
    private static int ID = 1;

    /**
     * Gets the next available ID for a new computer.
     * @return The available ID.
     */
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
}
