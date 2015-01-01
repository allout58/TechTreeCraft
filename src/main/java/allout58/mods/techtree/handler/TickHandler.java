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

import allout58.mods.techtree.research.ResearchData;
import allout58.mods.techtree.research.ResearchServer;
import allout58.mods.techtree.tree.NodeMode;
import allout58.mods.techtree.util.CodeContextHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/19/2014.
 */
public class TickHandler
{
    public static final TickHandler INSTANCE = new TickHandler();

    private static final Logger log = LogManager.getLogger();

    private int ticks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if (CodeContextHelper.getInstance().getEffectiveSide().isClient())
            return;
        if (event.phase == TickEvent.Phase.START)
            return;
        if (ticks++ > 300)
        {
            try
            {
                //                log.info("PlayerTikTok");
                for (Object player : FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList)
                {
                    if (player instanceof EntityPlayer)
                    {
                        if (ResearchServer.getInstance().getResearchRate(((EntityPlayer) player).getUniqueID().toString()) == 0)
                            continue;
                        List<ResearchData> toupdate = new ArrayList<ResearchData>();
                        for (ResearchData data : ResearchServer.getInstance().getClientData(((EntityPlayer) player).getUniqueID().toString()))
                            if (data.getMode() == NodeMode.Researching)
                                toupdate.add(data);

                        for (int i = 0; i < toupdate.size() - 1; i++)
                        {
                            ResearchData data = toupdate.get(i);
                            int rate = ResearchServer.getInstance().getResearchRate(data.getUuid()) / toupdate.size();
                            ResearchServer.getInstance().setResearch(data.getUuid(), data.getNodeID(), data.getResearchAmount() + rate);
                        }
                        if (toupdate.size() > 0)
                        {
                            ResearchData data = toupdate.get(toupdate.size() - 1);
                            int rate = ResearchServer.getInstance().getResearchRate(data.getUuid()) / toupdate.size() + ResearchServer.getInstance().getResearchRate(data.getUuid()) % toupdate.size();
                            ResearchServer.getInstance().setResearch(data.getUuid(), data.getNodeID(), data.getResearchAmount() + rate);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                log.error(e);
            }
            ticks = 0;
        }

    }
}
