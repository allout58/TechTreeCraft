/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2015 allout58                                                     *
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

import allout58.mods.techtree.research.ResearchData;
import allout58.mods.techtree.research.ResearchServer;
import allout58.mods.techtree.util.CodeContextHelper;
import allout58.mods.techtree.util.LogHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/6/2014.
 */
public class TechNode implements Comparable<TechNode>
{
    //Basic tree structure
    protected ArrayList<TechNode> parents = new ArrayList<TechNode>();
    protected ArrayList<TechNode> children = new ArrayList<TechNode>();
    protected ArrayList<Integer> parentID = new ArrayList<Integer>();
    protected int id = -1;
    protected int depth = -1;
    //Tech Tree data
    protected String name;
    protected int scienceRequired = -1;
    protected String description;
    protected ItemStack[] lockedItems;

    /**
     * @param id ID to assign to this node. Used for (de)serialization.
     */
    public TechNode(int id)
    {
        this.id = id;
    }

    public TechNode(TechNode copy)
    {
        this(copy.getId());
        this.setup(copy.getName(), copy.getScienceRequired(), copy.getDescription(), copy.getLockedItems());
        for (int pID : copy.getParentID())
            this.addParentNode(pID);
        this.setDepth(copy.getDepth());
    }

    /**
     * Add a parent node's id to this node's list.
     *
     * @param parent The parent's ID
     */
    public void addParentNode(int parent)
    {
        if (!parentID.contains(parent))
            parentID.add(parent);
    }

    /**
     * Add a parent node to this node's list of parents
     *
     * @param parent The parent node
     */
    public void addParentNode(TechNode parent)
    {
        if (!parents.contains(parent))
            parents.add(parent);
    }

    /**
     * Add a child node to this node's list of children
     *
     * @param child The child node
     */
    public void addChildNode(TechNode child)
    {
        if (!children.contains(child))
            children.add(child);
    }

    /**
     * Setup this node
     *
     * @param name       The node's name. Used as a header in-game
     * @param scienceReq Science points required to unlock this node
     * @param desc       Description of the node.
     * @param lockItems  Items locked by this node. Unlocking the node allows them to be crafted again.
     */
    public void setup(String name, int scienceReq, String desc, ItemStack[] lockItems)
    {
        this.name = name;
        this.scienceRequired = scienceReq;
        this.description = desc;
        this.lockedItems = lockItems;
    }

    /**
     * @return The node's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The node's description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return The science required to unlock this node
     */
    public int getScienceRequired()
    {
        return scienceRequired;
    }

    /**
     * @return The node's ID used for initial linking and (de)serialization
     */
    public int getId()
    {
        return id;
    }

    /**
     * Should only be used during (de)serialization
     *
     * @return The node's list of parents (in ID form)
     */
    public List<Integer> getParentID()
    {
        return parentID;
    }

    /**
     * @return The node's list of parents (in Node form)
     */
    public List<TechNode> getParents()
    {
        return parents;
    }

    /**
     * @return The items locked by this node
     */
    public ItemStack[] getLockedItems()
    {
        return lockedItems;
    }

    public List<TechNode> getChildren()
    {
        return children;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        if (depth < 0)
            throw new RuntimeException("Setting depth to negative: " + depth + "--" + toString());
        this.depth = depth;
    }

    /**
     * @param previous The previous state of this node
     * @return The new state of this node
     */
    public NodeMode onParentUpdate(NodeMode previous, String uuid)
    {
        if (CodeContextHelper.getInstance().getEffectiveSide().isClient())
        {
            LogHelper.logger.error("Tried to update the mode of a node on the client!", new Exception());
        }
        int count = 0;
        for (int i = 0; i < parents.size(); i++)
        {
            TechNode parent = parents.get(i);
            while (parent instanceof FakeNode)
                parent = parent.getParents().get(0);
            for (ResearchData d : ResearchServer.getInstance().getClientData(uuid))
            {
                if (d.getNodeID() == parent.getId() && d.getMode() == NodeMode.Completed)
                    count++;
            }
        }
        return count == parents.size() ? NodeMode.Unlocked : previous;
    }

    @Override
    public int compareTo(TechNode o)
    {
        return Integer.compare(getId(), o.getId());
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s (%d)", super.toString(), this.name, this.id);
    }
}
