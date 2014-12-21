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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * Created by James Hollowell on 12/19/2014.
 */
public class TickHandler
{
    public static final TickHandler INSTANCE = new TickHandler();

    private long ticks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if (TickEvent.Phase.START.equals(event.getPhase())) return;
        for (ResearchData data : ResearchServer.getInstance().getAllData())
        {
            if (data.getMode() == NodeMode.Researching)
            {
                //if (data.getUuid().equals(event.player.getUniqueID()))
                data.setResearchAmount(data.getResearchAmount() + 1);
            }
        }

    }

}
