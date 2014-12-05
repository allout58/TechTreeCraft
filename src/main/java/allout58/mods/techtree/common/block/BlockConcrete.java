package allout58.mods.bigfactories.common.block;

import allout58.mods.bigfactories.BigFactories;
import allout58.mods.bigfactories.lib.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class BlockConcrete extends Block
{
    public BlockConcrete()
    {
        super(Material.rock);
        setBlockName("concrete");
        setHardness(0.8f);
        setStepSound(soundTypeStone);
        setBlockTextureName(ModInfo.MOD_ID+":concrete");
        setCreativeTab(BigFactories.creativeTab);
    }
}
