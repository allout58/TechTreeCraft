package allout58.mods.bigfactories.common.block;

import allout58.mods.bigfactories.BigFactories;
import allout58.mods.bigfactories.common.tileentity.crusher.TileEntityCrusher;
import allout58.mods.bigfactories.common.tileentity.interfaces.IFacing;
import allout58.mods.bigfactories.lib.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class BlockMBMaster extends BlockContainer
{
    public IIcon[] iconCrusher = new IIcon[2];

    protected BlockMBMaster()
    {
        super(Material.iron);
        setStepSound(soundTypeMetal);
        setBlockName("mbMaster");
        setCreativeTab(BigFactories.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        blockIcon = ir.registerIcon("minecraft:stone");

        iconCrusher[0] = ir.registerIcon(ModInfo.MOD_ID + ":crusherFace");
        iconCrusher[1] = ir.registerIcon(ModInfo.MOD_ID + ":crusherSide");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IFacing)
        {
            switch (meta)
            {
                case 0:
                    if (side == ((IFacing) te).getFacing())
                        return iconCrusher[0];
                    else return iconCrusher[1];
                default:
                    return blockIcon;
            }
        }
        else
            return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    //For item rendering
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
            case 0:
                if (side == ForgeDirection.SOUTH.ordinal())
                    return iconCrusher[0];
                else return iconCrusher[1];
            default:
                return blockIcon;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 1; i++)
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
                return new TileEntityCrusher();
            default:
                return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack par6ItemStack)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IFacing)
        {


            ((IFacing) te).setFacing(direction);
        }
    }
}