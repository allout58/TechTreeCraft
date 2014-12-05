package allout58.mods.bigfactories.common.tileentity.interfaces;

/**
 * Created by James Hollowell on 8/1/2014.
 */
public interface IMBSlave
{
    public IMBMaster getMaster();
    public void setMaster(int x, int y, int z);
    public void reset();
}
