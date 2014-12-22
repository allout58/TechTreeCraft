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

package allout58.mods.techtree.research;

import allout58.mods.techtree.TechTreeMod;
import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.NodeMode;
import allout58.mods.techtree.tree.TechNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James Hollowell on 12/17/2014.
 */
@SideOnly(Side.CLIENT)
public class ResearchClient implements IResearchHolder
{
    private static ResearchClient INSTANCE;
    /**
     * Client's UUID
     */
    private String clientID;
    private Map<Integer, ResearchData> researchList = new HashMap<Integer, ResearchData>();

    public ResearchClient(String clientID)
    {
        this.clientID = clientID;
        for (TechNode node : TechTreeMod.tree.getNodes())
        {
            if (node instanceof FakeNode) continue;
            setResearch(node.getId(), 0);
            setMode(node.getId(), NodeMode.Locked);
        }
    }

    public static ResearchClient getInstance() throws IllegalAccessException
    {
        if (INSTANCE == null)
            throw new IllegalAccessException("Tried to get the ResearchClient instance with no uuid and no previous instance");
        return INSTANCE;
    }

    public static ResearchClient getInstance(String uuid)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ResearchClient(uuid);
            return INSTANCE;
        }
        if (!INSTANCE.clientID.equals(uuid))
            throw new IllegalArgumentException("Tried to get the ResearchClient instance with a different UUID than is already set.");
        return INSTANCE;
    }

    public void setResearch(int nodeID, int newVal)
    {
        if (researchList.get(nodeID) == null)
            researchList.put(nodeID, new ResearchData(nodeID, newVal, NodeMode.Locked, clientID));
        else
            researchList.get(nodeID).setResearchAmount(newVal);
    }

    public int getResearch(int nodeID)
    {
        return researchList.containsKey(nodeID) ? researchList.get(nodeID).getResearchAmount() : 0;
    }

    public void setMode(int nodeID, NodeMode mode)
    {
        researchList.get(nodeID).setMode(mode);
    }

    public NodeMode getMode(int nodeID)
    {
        return researchList.get(nodeID) != null ? researchList.get(nodeID).getMode() : NodeMode.Locked;
    }

    @Override
    public List<ResearchData> getAllData()
    {
        return new ArrayList<ResearchData>(researchList.values());
    }

    public boolean isUpdated()
    {
        return researchList.size() != 0;
    }

    public void reset()
    {
        for (ResearchData d : researchList.values())
        {
            System.out.println(String.format("Client-side -- ID: %d, Science: %d/%d, Mode: %s", d.getNodeID(), d.getResearchAmount(), TechTreeMod.tree.getNodeByID(d.getNodeID()).getScienceRequired(), d.getMode().name()));
        }
        researchList.clear();
        for (TechNode node : TechTreeMod.tree.getNodes())
        {
            if (node instanceof FakeNode) continue;
            setResearch(node.getId(), 0);
            setMode(node.getId(), NodeMode.Locked);
        }
    }
}
