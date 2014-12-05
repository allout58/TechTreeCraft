package allout58.mods.bigfactories.common.tileentity.crusher;

import allout58.mods.bigfactories.common.tileentity.interfaces.IDropableInventory;
import allout58.mods.bigfactories.common.tileentity.interfaces.IEnergyConsumer;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBMaster;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBSlave;
import allout58.mods.bigfactories.util.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by James Hollowell on 8/1/2014.
 */
public class TileEntityCrusherGrinder extends TileEntity
        implements IMBSlave, ISidedInventory, IDropableInventory, IEnergyConsumer
{
    private int masterX, masterY, masterZ;
    private ItemStack inventory;

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
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return true;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot != 0) return null;
        return inventory;
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
        inventory = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
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
        //TODO: make sure it is a valid item
        return false;
    }

    @Override
    public void dropContents()
    {
        ItemStackHelper.spawnItemStackInWorld(getStackInSlot(0), worldObj, xCoord, yCoord + 1, zCoord);
        setInventorySlotContents(0, null);
    }

    @Override
    public int getConsumptionRate()
    {
        return 5;
    }
}
