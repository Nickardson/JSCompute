package com.nickardson.jscomputing.utility;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUtilities {

    public static int getCompassDirection(double yaw) {
        return MathHelper.floor_double((yaw * 4F) / 360F + 0.5D) & 3;
    }

    public static int getMetadataFromYaw(double yaw) {
        return getDirectionFromYaw(yaw).ordinal();
    }

    public static ForgeDirection getDirectionFromYaw(double yaw) {
        int look = getCompassDirection(yaw);

        ForgeDirection direction;
        switch (look)
        {
            default:
            case 0:
                direction = ForgeDirection.NORTH; break;
            case 1:
                direction = ForgeDirection.EAST; break;
            case 2:
                direction = ForgeDirection.SOUTH; break;
            case 3:
                direction = ForgeDirection.WEST; break;
        }
        return direction;
    }
}
