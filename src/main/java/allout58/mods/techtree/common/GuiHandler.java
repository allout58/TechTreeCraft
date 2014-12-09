package allout58.mods.techtree.common;

import allout58.mods.techtree.client.GuiTree;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by James Hollowell on 12/7/2014.
 */
public class GuiHandler implements IGuiHandler
{
    public static final int TREE_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case TREE_ID:
                return null;
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case TREE_ID:
                //return new GuiTree();
            default:
                return null;
        }
    }
}
