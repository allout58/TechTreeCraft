package allout58.mods.bigfactories;

import allout58.mods.bigfactories.common.block.BlockRegistry;
import allout58.mods.bigfactories.common.tileentity.crusher.TileEntityCrusher;
import allout58.mods.bigfactories.common.tileentity.crusher.TileEntityCrusherGrinder;
import allout58.mods.bigfactories.common.tileentity.energy.TileEntityBattery;
import allout58.mods.bigfactories.common.tileentity.energy.TileEntityFurnaceGenerator;
import allout58.mods.bigfactories.common.tileentity.general.TileEntityMachineInput;
import allout58.mods.bigfactories.common.tileentity.general.TileEntityMachineOutput;
import allout58.mods.bigfactories.lib.ModInfo;
import allout58.mods.bigfactories.proxy.ISidedProxy;
import allout58.mods.bigfactories.util.LogHelper;
import allout58.mods.bigfactories.util.VersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by James Hollowell on 12/5/2014.
 */

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = "@VERSION@")
public class BigFactories
{
    public static String version = "@VERSION@";

    @SidedProxy(clientSide = "allout58.mods.bigfactories.proxy.ClientProxy", serverSide = "allout58.mods.bigfactories.proxy.ServerProxy")
    public static ISidedProxy proxy;

    @Mod.Instance(ModInfo.MOD_ID)
    public static BigFactories instance;

    public static CreativeTabs creativeTab = new CreativeTabs(ModInfo.MOD_ID)
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.lit_furnace);
        }
    };

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        LogHelper.init(event.getModLog());
        VersionChecker.execute();

        /*--------------- Register Events ---------------- */
        MinecraftForge.EVENT_BUS.register(VersionChecker.instance);

        /*--------------- Register blocks/items/TEs ------------------*/
        BlockRegistry.register();
        registerTileEntities();
    }

    static
    {
        if (version.contains("VERSION"))
            version = "1.0.0-rev1";//Hardcode if in dev environment
    }

    private void registerTileEntities()
    {
        //Energy
        GameRegistry.registerTileEntity(TileEntityBattery.class, "battery");
        GameRegistry.registerTileEntity(TileEntityFurnaceGenerator.class, "furnacegenerator");
        //General
        GameRegistry.registerTileEntity(TileEntityMachineOutput.class, "machineoutput");
        GameRegistry.registerTileEntity(TileEntityMachineInput.class, "machineinput");
        //Crusher
        GameRegistry.registerTileEntity(TileEntityCrusher.class, "crushercore");
        GameRegistry.registerTileEntity(TileEntityCrusherGrinder.class, "crushergrinder");

    }
}
