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
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.util.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James Hollowell on 12/17/2014.
 */
public class ResearchServer implements IResearchHolder
{
    private static ResearchServer INSTANCE;

    private Map<String, HashMap<Integer, ResearchData>> data = new HashMap<String, HashMap<Integer, ResearchData>>();

    public static ResearchServer getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new ResearchServer();
        return INSTANCE;
    }

    public int getResearch(String uuid, int nodeID)
    {
        if (data.get(uuid) == null)
            return -1;
        return data.get(uuid).get(nodeID).getResearchAmount();
    }

    public void setResearch(String uuid, int nodeID, int value)
    {
        //Todo: Ensure `value` is less than or equal to the science required for the node
        if (data.get(uuid) == null)
            data.put(uuid, new HashMap<Integer, ResearchData>());

        //FIXME: Find a different way to access the tree
        TechTree tree = TechTreeMod.tree;
        TechNode node = tree.getNodeByID(nodeID);
        if (value >= node.getScienceRequired())
            value = node.getScienceRequired();

        if (data.get(uuid).get(nodeID) == null)
            data.get(uuid).put(nodeID, new ResearchData(nodeID, value, NodeMode.Locked, uuid));
        else
            data.get(uuid).get(nodeID).setResearchAmount(value);

        try
        {
            if (value >= node.getScienceRequired())
            {
                setMode(uuid, nodeID, NodeMode.Completed);
                for (int i = 0; i < node.getChildren().size(); i++)
                {
                    TechNode child = node.getChildren().get(i);
                    while (child instanceof FakeNode)
                        child = child.getChildren().get(0);
                    setMode(uuid, child.getId(), child.onParentUpdate(getMode(uuid, child.getId())));
                }
                data.get(uuid).get(nodeID).forceUpdate();
            }
        }
        catch (IllegalAccessException ignored)
        {
        }
        catch (IllegalArgumentException ignored)
        {
        }
    }

    public void probeModes() throws IllegalAccessException
    {
        for (ResearchData d : getAllData())
        {
            //FIXME: Find a different way to access the tree
            TechTree tree = TechTreeMod.tree;
            TechNode node = tree.getNodeByID(d.getNodeID());
            if (d.getResearchAmount() == node.getScienceRequired())
            {
                setMode(d.getUuid(), d.getNodeID(), NodeMode.Completed);
                for (int i = 0; i < node.getChildren().size(); i++)
                {
                    TechNode child = node.getChildren().get(i);
                    while (child instanceof FakeNode)
                        child = child.getChildren().get(0);
                    setMode(d.getUuid(), child.getId(), child.onParentUpdate(getMode(d.getUuid(), child.getId())));
                }
            }
        }
    }

    public void setMode(String uuid, int nodeID, NodeMode mode)
            throws IllegalAccessException
    {
        if (data.get(uuid) == null || data.get(uuid).get(nodeID) == null)
            throw new IllegalArgumentException("Trying to change the mode of a node that doesn't exist or whose player doesn't exist: " + uuid + " " + nodeID + " " + mode.name());
        data.get(uuid).get(nodeID).setMode(mode);
    }

    public NodeMode getMode(String uuid, int nodeID)
    {
        if (data.get(uuid) == null || data.get(uuid).get(nodeID) == null)
            throw new IllegalArgumentException("Trying to change the mode of a node that doesn't exist or whose player doesn't exist: " + uuid + " " + nodeID);
        return data.get(uuid).get(nodeID).getMode();
    }

    public void load()
    {
        //For now...
        for (TechNode node : TechTreeMod.tree.getNodes())
            if (!(node instanceof FakeNode))
                setResearch("08338e60-fd0e-3489-b7d1-abc1d16f021c", node.getId(), 0);
        try
        {
            probeModes();
        }
        catch (IllegalAccessException e)
        {
            LogHelper.logger.error(e);
        }
        catch (IllegalArgumentException e)
        {
            LogHelper.logger.error(e);
        }
    }

    public void save()
    {
        for (ResearchData d : data.get("08338e60-fd0e-3489-b7d1-abc1d16f021c").values())
        {
            System.out.println(String.format("Server-side -- ID: %d, Science: %d/%d, Mode: %s", d.getNodeID(), d.getResearchAmount(), TechTreeMod.tree.getNodeByID(d.getNodeID()).getScienceRequired(), d.getMode().name()));
        }
        data.clear();
    }

    @Override
    public List<ResearchData> getAllData()
    {
        ArrayList<ResearchData> out = new ArrayList<ResearchData>();
        for (HashMap<Integer, ResearchData> sub : data.values())
            for (ResearchData d : sub.values())
                out.add(d);
        return out;
    }

}
