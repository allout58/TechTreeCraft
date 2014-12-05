package allout58.mods.bigfactories.common.tileentity.energy;

import allout58.mods.bigfactories.common.tileentity.interfaces.IEnergyStorage;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBMaster;
import allout58.mods.bigfactories.lib.EnergyInfo;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class TileEntityBattery extends TileEntity implements IEnergyStorage
{

    private int tier = 0;
    private int energy = 0;

    private int masterX, masterY, masterZ;

    @Override
    public IMBMaster getMaster()
    {
        TileEntity te = worldObj.getTileEntity(masterX, masterY, masterZ);
        if (te instanceof IMBMaster) return (IMBMaster) te;
        else return null;
    }

    @Override
    public void setMaster(int x, int y, int z)
    {
        masterX = x;
        masterY = y;
        masterZ = z;
    }

    @Override
    public void reset()
    {
        masterX = 0;
        masterY = 0;
        masterZ = 0;
    }

    @Override
    public int getMaxPower()
    {
        switch (tier)
        {
            case 0:
                return EnergyInfo.TIER_0_MAX;
            case 1:
                return EnergyInfo.TIER_1_MAX;
            case 2:
                return EnergyInfo.TIER_2_MAX;
            default:
                return 0;
        }
    }

    @Override
    public int getCurrentPower()
    {
        return energy;
    }

    @Override
    public int getPowerSpeed()
    {
        switch (tier)
        {
            case 0:
                return EnergyInfo.TIER_0_SPEED;
            case 1:
                return EnergyInfo.TIER_1_SPEED;
            case 2:
                return EnergyInfo.TIER_2_SPEED;
            default:
                return 0;
        }
    }

    @Override
    public boolean decreasePower(int amount)
    {
        //TODO: Not Implemented
        return false;
    }

    @Override
    public boolean addPower(int amount)
    {
        //TODO: Not Implemented
        return false;
    }
}
