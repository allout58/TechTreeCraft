package allout58.mods.bigfactories.common.tileentity.general;

import allout58.mods.bigfactories.common.tileentity.interfaces.IDropableInventory;
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
public class TileEntityMachineInput extends TileEntity
        implements IMBSlave, ISidedInventory, IDropableInventory
{
    public static final int INV_SIZE = 3;
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
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0, 1, 2 };
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
        return "container.machineInput";
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
