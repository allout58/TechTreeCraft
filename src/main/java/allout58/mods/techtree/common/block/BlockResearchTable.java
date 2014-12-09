package allout58.mods.techtree.common.block;

import allout58.mods.techtree.TechTreeMod;
import allout58.mods.techtree.client.GuiTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by James Hollowell on 12/9/2014.
 */
public class BlockResearchTable extends Block
{

    protected BlockResearchTable(Material mat)
    {
        super(mat);
        setCreativeTab(TechTreeMod.creativeTab);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
        if (entityPlayer.isSneaking()) return false;
        else
        {
            if (world.isRemote)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiTree(TechTreeMod.tree));
            }

            return true;
        }
    }
}
