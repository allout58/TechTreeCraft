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

import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.NodeMode;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.tree.TreeManager;
import allout58.mods.techtree.util.LogHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by James Hollowell on 12/17/2014.
 */
public class ResearchServer implements IResearchHolder
{
    private static ResearchServer INSTANCE;

    private static final Pattern uuidPattern = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

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
        if (data.get(uuid) == null)
            data.put(uuid, new HashMap<Integer, ResearchData>());

        TechTree tree = TreeManager.instance().getTree();
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
                    setMode(uuid, child.getId(), child.onParentUpdate(getMode(uuid, child.getId()), uuid));
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
            TechTree tree = TreeManager.instance().getTree();
            TechNode node = tree.getNodeByID(d.getNodeID());
            if (d.getResearchAmount() == node.getScienceRequired())
            {
                setMode(d.getUuid(), d.getNodeID(), NodeMode.Completed);
                for (int i = 0; i < node.getChildren().size(); i++)
                {
                    TechNode child = node.getChildren().get(i);
                    while (child instanceof FakeNode)
                        child = child.getChildren().get(0);
                    setMode(d.getUuid(), child.getId(), child.onParentUpdate(getMode(d.getUuid(), child.getId()), d.getUuid()));
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
        try
        {
            File outFile = getSaveFile();
            BufferedReader br = new BufferedReader(new FileReader(outFile));
            String line;
            String currentUuid = "";
            while ((line = br.readLine()) != null)
            {
                if (uuidPattern.matcher(line).matches())
                {
                    currentUuid = line;
                    data.put(line, new HashMap<Integer, ResearchData>());
                }
                else
                {
                    ResearchData d = ResearchData.read(line, currentUuid);
                    data.get(currentUuid).put(d.getNodeID(), d);
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            LogHelper.logger.error("Error reading data file!", e);
        }

        //        try
        //        {
        //            probeModes();
        //        }
        //        catch (IllegalAccessException e)
        //        {
        //            LogHelper.logger.error(e);
        //        }
        //        catch (IllegalArgumentException e)
        //        {
        //            LogHelper.logger.error(e);
        //        }
    }

    public void save()
    {
        try
        {
            File outFile = getSaveFile();
            if (!outFile.exists())
                outFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (Map.Entry<String, HashMap<Integer, ResearchData>> player : data.entrySet())
            {
                bw.write(player.getKey());
                bw.newLine();
                for (Map.Entry<Integer, ResearchData> dat : player.getValue().entrySet())
                {
                    bw.write(dat.getValue().toString());
                    bw.newLine();
                }
            }
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

    public List<ResearchData> getClientData(String uuid)
    {
        return new ArrayList<ResearchData>(data.get(uuid).values());
    }

    public void makePlayerData(String uuid)
    {
        //Make a table for a new player
        //This will not run if the player has already logged on to the server or is loaded from disk
        if (data.get(uuid) == null)
        {
            data.put(uuid, new HashMap<Integer, ResearchData>());

            //Fill all with 0
            for (TechNode node : TreeManager.instance().getTree().getNodes())
                if (!(node instanceof FakeNode))
                    setResearch(uuid, node.getId(), 0);
        }
    }

    private File getSaveFile()
    {
        SaveHandler saveHandler = (SaveHandler) MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler();
        return new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/ttm.dat");
    }
}
