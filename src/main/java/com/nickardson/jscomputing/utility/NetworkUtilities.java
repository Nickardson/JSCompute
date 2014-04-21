package com.nickardson.jscomputing.utility;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NetworkUtilities {
    public static boolean isServerWorld(World world) {
        return !world.isRemote;
    }

    /**
     * Gets a list of players who have the given type of container open.
     * @param clazz The class of container to search for.
     */
    public static List<EntityPlayerMP> getPlayersWithContainer(Class<?> clazz) {
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
