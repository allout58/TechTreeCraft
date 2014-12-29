/******************************************************************************
 * The MIT License (MIT)                                                      *
 *                                                                            *
 * Copyright (c) 2014 allout58                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package allout58.mods.techtree.common.block;

import allout58.mods.techtree.TechTreeMod;
import allout58.mods.techtree.research.ResearchServer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        setBlockName("basicResearchTable");
        setHardness(1.5f);
        setHarvestLevel("axe", 0);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, placer, stack);
        if (!world.isRemote)
        {
            String uuid = placer.getUniqueID().toString();
            ResearchServer.getInstance().setResearchRate(uuid, ResearchServer.getInstance().getResearchRate(uuid) + 5);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        //        if (world.isRemote)
        //        {
        //        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (!world.isRemote)
            ResearchServer.getInstance().setResearchRate(player.getUniqueID().toString(), ResearchServer.getInstance().getResearchRate(player.getUniqueID().toString()) - 5);

        for (int i = 0; i < 10; i++)
        {
            double xSpawn = x + world.rand.nextDouble();
            double ySpawn = y + world.rand.nextDouble() + 1;
            double zSpawn = z + world.rand.nextDouble();

            double xRate = xSpawn - (x + 0.5);
            double yRate = ySpawn - (y + 0.5);
            double zRate = zSpawn - (z + 0.5);

            xRate = yRate = zRate = 0;

            world.spawnParticle("smoke", xSpawn, ySpawn, zSpawn, xRate, yRate, zRate);
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
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
                TechTreeMod.proxy.openGui(0, entityPlayer);
            }

            return true;
        }
    }
}
