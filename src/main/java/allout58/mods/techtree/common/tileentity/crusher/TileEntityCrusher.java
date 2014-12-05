package allout58.mods.bigfactories.common.tileentity.crusher;

import allout58.mods.bigfactories.common.tileentity.interfaces.IFacing;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBMaster;
import allout58.mods.bigfactories.common.tileentity.interfaces.IMBSlave;
import allout58.mods.bigfactories.util.DirectionHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/1/2014.
 */
public class TileEntityCrusher extends TileEntity implements IMBMaster, IFacing
{
    private boolean isFormed = false;
    private List<IMBSlave> slaveList = new ArrayList<IMBSlave>();

    private int facing = ForgeDirection.UP.ordinal();

    /*
     * -----------------------------------------------------------------
     *                      Multi-block Functions
     * -----------------------------------------------------------------
     */
    @Override
    public void reset()
    {
        isFormed = false;
    }

    /*
     * ------------------------------------------------------------------
     *                        IFacing Functions
     * ------------------------------------------------------------------
     */
    @Override
    public int getFacing()
    {
        return facing;
    }

    @Override
    public ForgeDirection getForgeFacing()
    {
        return ForgeDirection.getOrientation(facing);
    }

    @Override
    public void setFacing(int facing)
    {
        this.facing = facing;
    }

    @Override
    public void setFacing(ForgeDirection facing)
    {
        this.facing = facing.ordinal();
    }

    @Override
    public void rotate()
    {
        this.facing = DirectionHelper.nextDirection(this.facing, false);
    }

    /*
     * ----------------------------------------------------------------------------------------
     *                                      NBT Functions
     * ----------------------------------------------------------------------------------------
     */
    @Override
    public void writeToNBT(NBTTagCompound data)
    {
        super.writeToNBT(data);
        data.setInteger("facing", facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound data)
    {
        super.writeToNBT(data);
        facing = data.getInteger("facing");
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

}
