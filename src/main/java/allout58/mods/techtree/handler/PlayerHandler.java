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

package allout58.mods.techtree.handler;

import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.network.message.SendResearch;
import allout58.mods.techtree.network.message.UpdateNodeMode;
import allout58.mods.techtree.research.ResearchClient;
import allout58.mods.techtree.research.ResearchData;
import allout58.mods.techtree.research.ResearchServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by James Hollowell on 12/22/2014.
 */
public class PlayerHandler
{
    public static final PlayerHandler INSTANCE = new PlayerHandler();

    @SubscribeEvent
    public void onJoinServer(PlayerEvent.PlayerLoggedInEvent event)
    {
        String uuid = event.player.getUniqueID().toString();
        assert event.player instanceof EntityPlayerMP;
        for (ResearchData d : ResearchServer.getInstance().getAllData())
        {
            if (uuid.equals(d.getUuid()))
            {
                NetworkManager.INSTANCE.sendTo(new SendResearch(d.getNodeID(), d.getResearchAmount(), uuid), (EntityPlayerMP) event.player);
                NetworkManager.INSTANCE.sendTo(new UpdateNodeMode(uuid, d.getNodeID(), d.getMode()), (EntityPlayerMP) event.player);
            }
        }
    }

    @SubscribeEvent
    public void onLeaveClient(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        ResearchClient.getInstance(FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString()).reset();
    }

    @SubscribeEvent
    public void onJoinClient(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        if (player != null)
            ResearchClient.getInstance(player.getUniqueID().toString());
    }
}
