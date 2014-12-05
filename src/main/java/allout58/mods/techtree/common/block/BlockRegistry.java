package allout58.mods.bigfactories.common.block;

import allout58.mods.bigfactories.common.item.itemblock.ItemBlockMBEnergy;
import allout58.mods.bigfactories.common.item.itemblock.ItemBlockMBMaster;
import allout58.mods.bigfactories.common.item.itemblock.ItemBlockMachineGeneral;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Created by James Hollowell on 8/2/2014.
 */
public class BlockRegistry
{
    public static Block mbMaster;
    public static Block mbEnergy;
    public static Block mbGeneral;
    public static Block concrete;

    public static void register()
    {
        mbMaster=new BlockMBMaster();
        mbEnergy=new BlockMBEnergy();
        mbGeneral=new BlockMBMachineGeneral();

        concrete=new BlockConcrete();

        GameRegistry.registerBlock(mbMaster, ItemBlockMBMaster.class,"multiblockmaster");
        GameRegistry.registerBlock(mbEnergy, ItemBlockMBEnergy.class, "multiblockenergy");
        GameRegistry.registerBlock(mbGeneral, ItemBlockMachineGeneral.class, "multiblockgeneral");

        GameRegistry.registerBlock(concrete, "concrete");

    }
}
