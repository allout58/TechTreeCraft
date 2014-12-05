package allout58.mods.bigfactories.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by James Hollowell on 8/3/2014.
 */
public class DirectionHelper
{
    public static int nextDirection(int currentDirection, boolean includeTopBottom)
    {
        return nextDirection(ForgeDirection.getOrientation(currentDirection), includeTopBottom).ordinal();
    }

    public static ForgeDirection nextDirection(ForgeDirection currentDirection, boolean includeTopBottom)
    {
        return currentDirection.getRotation(ForgeDirection.UP);
    }

    public static int getFacingFromEntity(EntityLivingBase entityLiving)
    {
        int direction = 0;
        int facing = MathHelper.floor_double((double) (entityLiving.rotationYaw * 4F / 360) + 0.5D) & 3;

        switch (facing)
        {
            case 0:
                direction = 2;
                break;
            case 1:
                direction = 5;
                break;
            case 2:
                direction = 3;
                break;
            case 3:
                direction = 4;
                break;
        }

        return direction;
    }
}
