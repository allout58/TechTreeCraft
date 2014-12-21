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

package allout58.mods.techtree;

import allout58.mods.techtree.commands.ResearchCommand;
import allout58.mods.techtree.common.block.BlockRegistry;
import allout58.mods.techtree.handler.TickHandler;
import allout58.mods.techtree.lib.ModInfo;
import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.proxy.ISidedProxy;
import allout58.mods.techtree.research.ResearchServer;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.tree.TreeLoader;
import allout58.mods.techtree.util.LogHelper;
import allout58.mods.techtree.util.VersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
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
    public static TechTree tree;

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
        //Todo: Ensure tree on client and server is the same
        LogHelper.init(event.getModLog());
        LogHelper.logger.info(LogHelper.PREINIT, "TechTree " + version + " pre-initializing");
        NetworkManager.init();
        //VersionChecker.execute();
        node = TreeLoader.readTree("./tree.json");
        tree = new TechTree(node);

        /*--------------- Register Events ---------------- */
        MinecraftForge.EVENT_BUS.register(VersionChecker.instance);
        FMLCommonHandler.instance().bus().register(TickHandler.INSTANCE);

        /*--------------- Register blocks/items/TEs ------------------*/
        BlockRegistry.register();
        registerTileEntities();

        //if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        //    ResearchClient.getInstance(FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString());

        //CraftingManager.getInstance().getRecipeList().clear();

        LogHelper.logger.info(LogHelper.PREINIT, "TechTree pre-initialization complete");
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ResearchCommand());
        ResearchServer.getInstance().load();
        //Todo: load tree(s) and the research progress from disk
    }

    @Mod.EventHandler
    public void serverUnload(FMLServerStoppingEvent event)
    {
        ResearchServer.getInstance().save();
        //Todo: save the research progress to disk
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
