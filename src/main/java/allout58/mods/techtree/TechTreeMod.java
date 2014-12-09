package allout58.mods.techtree;

import allout58.mods.techtree.common.block.BlockRegistry;
import allout58.mods.techtree.lib.ModInfo;
import allout58.mods.techtree.proxy.ISidedProxy;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.tree.TreeLoader;
import allout58.mods.techtree.util.LogHelper;
import allout58.mods.techtree.util.VersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by James Hollowell on 12/5/2014.
 */

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = "@VERSION@")
public class TechTreeMod
{
    public static String version = "@VERSION@";
    public static TechNode node;

    @SidedProxy(clientSide = "allout58.mods.techtree.proxy.ClientProxy", serverSide = "allout58.mods.techtree.proxy.ServerProxy")
    public static ISidedProxy proxy;

    @Mod.Instance(ModInfo.MOD_ID)
    public static TechTreeMod instance;

    public static CreativeTabs creativeTab = new CreativeTabs(ModInfo.MOD_ID)
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.crafting_table);
        }
    };

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        LogHelper.init(event.getModLog());
        LogHelper.logger.info(LogHelper.PREINIT, "TechTree " + version + " pre-initializing");
        //VersionChecker.execute();
        node = TreeLoader.readTree("./tree.json");
        TechTree tree = new TechTree(node);

        /*--------------- Register Events ---------------- */
        MinecraftForge.EVENT_BUS.register(VersionChecker.instance);

        /*--------------- Register blocks/items/TEs ------------------*/
        BlockRegistry.register();
        registerTileEntities();

        //CraftingManager.getInstance().getRecipeList().clear();

        LogHelper.logger.info(LogHelper.PREINIT, "TechTree pre-initialization complete");
    }

    static
    {
        if (version.contains("VERSION"))
            version = "1.0.0-rev1"; //Hardcode if in dev environment
    }

    private void registerTileEntities()
    {
    }
}
