/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2014 allout58                                                     *
 *                                                                                 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 *                                                                                 *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 *                                                                                 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 ***********************************************************************************/

package allout58.mods.techtree.research;

import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.network.message.SendResearch;
import allout58.mods.techtree.network.message.UpdateNodeMode;
import allout58.mods.techtree.tree.NodeMode;
import allout58.mods.techtree.util.PlayerHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by James Hollowell on 12/20/2014.
 */
public class ResearchData
{
    int nodeID = 0;
    int researchAmount = 0;
    NodeMode mode = NodeMode.Locked;
    String uuid = "";

    public ResearchData(int nodeID, int researchAmount, NodeMode mode, String uuid)
    {
        this.nodeID = nodeID;
        this.mode = mode;
        this.uuid = uuid;
        setResearchAmount(researchAmount);
    }

    public int getNodeID()
    {
        return nodeID;
    }

    public int getResearchAmount()
    {
        return researchAmount;
    }

    public NodeMode getMode()
    {
        return mode;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setResearchAmount(int researchAmount)
    {
        this.researchAmount = researchAmount;
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && researchAmount % 10 == 0)
        {
            EntityPlayerMP player = PlayerHelper.getPlayerFromUUID(getUuid(), FMLCommonHandler.instance().getMinecraftServerInstance());
            if (player != null)
                NetworkManager.INSTANCE.sendTo(new SendResearch(getNodeID(), getResearchAmount(), getUuid()), player);
        }
    }

    public void setMode(NodeMode mode)
    {
        this.mode = mode;
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            EntityPlayerMP player = PlayerHelper.getPlayerFromUUID(getUuid(), FMLCommonHandler.instance().getMinecraftServerInstance());
            if (player != null)
                NetworkManager.INSTANCE.sendTo(new UpdateNodeMode(getUuid(), getNodeID(), getMode()), player);
        }
    }

    public void forceUpdate()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            EntityPlayerMP player = PlayerHelper.getPlayerFromUUID(getUuid(), FMLCommonHandler.instance().getMinecraftServerInstance());
            if (player != null)
            {
                NetworkManager.INSTANCE.sendTo(new SendResearch(getNodeID(), getResearchAmount(), getUuid()), player);
                NetworkManager.INSTANCE.sendTo(new UpdateNodeMode(getUuid(), getNodeID(), getMode()), player);
            }
        }
    }

    public static IResearchHolder getSidedResearch()
    {
        try
        {
            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                return ResearchClient.getInstance();
            else
                return ResearchServer.getInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString()
    {
        return nodeID + ", " + researchAmount + ", " + mode.getId();
    }

    public static ResearchData read(String line, String uuid)
    {
        String[] split = line.split(", ");
        int nodeID = Integer.parseInt(split[0]);
        int amnt = Integer.parseInt(split[1]);
        NodeMode mode = NodeMode.getByID(Integer.parseInt(split[2]));
        return new ResearchData(nodeID, amnt, mode, uuid);
    }
}
