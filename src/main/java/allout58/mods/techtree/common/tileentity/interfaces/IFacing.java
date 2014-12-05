package allout58.mods.bigfactories.common.tileentity.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public interface IFacing
{
    public int getFacing();
    public ForgeDirection getForgeFacing();
    public void setFacing(int facing);
    public void setFacing(ForgeDirection facing);
    public void rotate();
}
