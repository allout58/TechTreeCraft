package allout58.mods.bigfactories.common.block;

import allout58.mods.bigfactories.BigFactories;
import allout58.mods.bigfactories.common.tileentity.general.TileEntityMachineInput;
import allout58.mods.bigfactories.common.tileentity.general.TileEntityMachineOutput;
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
public class BlockMBMachineGeneral extends BlockContainer
{
    public IIcon iconSide;
    public IIcon[] iconIO = new IIcon[2];

    protected BlockMBMachineGeneral()
    {
        super(Material.iron);
        setStepSound(soundTypeMetal);
        setBlockName("mbMachine");
        setCreativeTab(BigFactories.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        blockIcon = ir.registerIcon("minecraft:stone");

        iconSide = ir.registerIcon(ModInfo.MOD_ID + ":mbgeneral/side");
        iconIO[0] = ir.registerIcon(ModInfo.MOD_ID + ":mbgeneral/input");
        iconIO[1] = ir.registerIcon(ModInfo.MOD_ID + ":mbgeneral/output");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            return iconSide;

        int meta = world.getBlockMetadata(x, y, z);
        switch (meta)
        {
            case 0:
                return iconIO[0];
            case 1:
                return iconIO[1];
            default:
                return blockIcon;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    //For item rendering
    public IIcon getIcon(int side, int meta)
    {
        if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            return iconSide;

        switch (meta)
        {
            case 0:
                return iconIO[0];
            case 1:
                return iconIO[1];
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
                return new TileEntityMachineInput();
            case 1:
                return new TileEntityMachineOutput();
            default:
                return null;
        }
    }
}
