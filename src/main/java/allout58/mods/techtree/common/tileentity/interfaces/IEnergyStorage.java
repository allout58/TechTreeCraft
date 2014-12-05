package allout58.mods.bigfactories.common.tileentity.interfaces;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public interface IEnergyStorage extends IMBSlave
{
    public int getMaxPower();
    public int getCurrentPower();
    public int getPowerSpeed();
    public boolean decreasePower(int amount);
    public boolean addPower(int amount);
}
