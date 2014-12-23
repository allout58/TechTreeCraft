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

package allout58.mods.techtree.tree;

import allout58.mods.techtree.util.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by James Hollowell on 12/22/2014.
 */
public class TreeManager
{
    private static TreeManager INSTANCE;
    private TechTree currentTree;
    private String treeString = "";

    public static TreeManager instance()
    {
        if (INSTANCE == null)
            INSTANCE = new TreeManager();
        return INSTANCE;
    }

    public TechTree getTree()
    {
        return currentTree;
    }

    public void readTree(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists()) return;

        try
        {
            Reader reader = new FileReader(file);
            makeFromReader(reader);
            reader.close();

            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null)
            {
                treeString += line;
            }
        }
        catch (IOException e)
        {
            LogHelper.logger.error("Error reading tree from file " + file, e);
        }
    }

    public void readFromString(String jsonTree)
    {
        StringReader reader = new StringReader(jsonTree);
        makeFromReader(reader);
        reader.close();
    }

    public String getTreeAsString()
    {

        //        GsonBuilder gsonBuilder = new GsonBuilder();
        //        gsonBuilder.registerTypeAdapter(TechNode.class, new TechNodeGSON());
        //        Gson gson = gsonBuilder.create();
        //
        //        TechNode[] nodes = currentTree.getRealNodes().toArray(new TechNode[currentTree.getNodes().size()]);
        //        return gson.toJson(nodes);
        return treeString;
    }

    public void makeFromReader(Reader reader)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TechNode.class, new TechNodeGSON());
        Gson gson = gsonBuilder.create();

        TechNode[] nodes = gson.fromJson(reader, TechNode[].class);
        TechNode head = null;

        for (TechNode node : nodes)
        {
            if (node.getParentID().size() == 0)
            {
                head = node;
            }
            else
            {
                TechNode child = node;
                for (int parentID : child.getParentID())
                {
                    if (parentID == child.getId())
                        throw new RuntimeException("Tree node with a self-referencing parent: " + parentID);
                    TechNode parent = nodes[parentID];
                    child.addParentNode(parent);
                    parent.addChildNode(child);
                }
            }
        }
        currentTree = new TechTree(head);
    }
}

