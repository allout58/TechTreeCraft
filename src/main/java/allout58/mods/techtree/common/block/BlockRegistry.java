package allout58.mods.techtree.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class BlockRegistry
{
    public static void register()
    {
        GameRegistry.registerBlock(new BlockResearchTable(Material.rock), "researchTable");
    }
}
