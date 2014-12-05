package allout58.mods.bigfactories.common.tileentity.energy;

import allout58.mods.bigfactories.common.tileentity.interfaces.IDropableInventory;
import allout58.mods.bigfactories.common.tileentity.interfaces.IEnergyProducer;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBMaster;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBSlave;
import allout58.mods.bigfactories.lib.EnergyInfo;
import allout58.mods.bigfactories.util.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class TileEntityFurnaceGenerator extends TileEntity
        implements IEnergyProducer, IMBSlave, ISidedInventory, IDropableInventory
{
    public static final int INV_SIZE = 1;

    private int tier = 0;
    private int energy = 0;
    private int productionRate = 0;

    private int masterX, masterY, masterZ;

    private ItemStack[] inventory = new ItemStack[INV_SIZE];

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
                return EnergyInfo.TIER_0_MAX / 2;
            case 1:
                return EnergyInfo.TIER_1_MAX / 2;
            case 2:
                return EnergyInfo.TIER_2_MAX / 2;
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
        if (energy > 0)
        {
            energy -= amount;
            if (energy < 0) energy = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean addPower(int amount)
    {
        if (energy < getMaxPower())
        {
            energy += amount;
            if (energy > getMaxPower()) energy = getMaxPower();
            return true;
        }
        else
            return false;
    }

    @Override
    public int getProductionRate()
    {
        return productionRate;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack var2, int side)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return INV_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot > INV_SIZE) return null;
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return "container.furnaceGenerator";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    public void dropContents()
    {
        for (int i = 0; i < INV_SIZE; i++)
        {
            ItemStackHelper.spawnItemStackInWorld(getStackInSlot(i), worldObj, xCoord, yCoord + 1, zCoord);
            setInventorySlotContents(i, null);
        }
    }
}
