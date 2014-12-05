package allout58.mods.bigfactories.common.block;

import allout58.mods.bigfactories.BigFactories;
import allout58.mods.bigfactories.common.tileentity.energy.TileEntityBattery;
import allout58.mods.bigfactories.common.tileentity.energy.TileEntityFurnaceGenerator;
import allout58.mods.bigfactories.common.tileentity.interfaces.IFacing;
import allout58.mods.bigfactories.lib.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class BlockMBEnergy extends BlockContainer
{
    private IIcon[] iconFurnace = new IIcon[3];
    private IIcon[] iconBattery = new IIcon[2];

    protected BlockMBEnergy()
    {
        super(Material.iron);
        setStepSound(soundTypeMetal);
        setBlockName("mbEnergy");
        setCreativeTab(BigFactories.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        blockIcon = ir.registerIcon("minecraft:stone");

        iconFurnace[0] = ir.registerIcon(ModInfo.MOD_ID + ":energy/furnaceOff");
        iconFurnace[1] = ir.registerIcon(ModInfo.MOD_ID + ":energy/furnaceOn");
        iconFurnace[2] = ir.registerIcon(ModInfo.MOD_ID + ":energy/furnaceSide");

        iconBattery[0] = ir.registerIcon(ModInfo.MOD_ID + ":energy/batteryFace");
        iconBattery[1] = ir.registerIcon(ModInfo.MOD_ID + ":energy/batterySide");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);

        switch (meta)
        {
            case 0:
                if (te instanceof IFacing)
                {
                    if (side == ((IFacing) te).getFacing())
                        return iconFurnace[0];
                        //TODO: Check if is on for on face
                    else return iconFurnace[2];
                }
                else
                    return blockIcon;
            case 1:
                return getIcon(side, 1);
            default:
                return blockIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    //For item rendering
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
            case 0:
                if (side == ForgeDirection.NORTH.ordinal())
                    return iconFurnace[0];
                else return iconFurnace[2];
            case 1:
                if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
                    return iconBattery[1];
                else return iconBattery[0];
            default:
                return blockIcon;
        }
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 2; i++)
        {
            list.add(new ItemStack(id, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
        {
            case 0:
                return new TileEntityFurnaceGenerator();
            case 1:
                return new TileEntityBattery();
            default:
                return null;
        }
    }
}
