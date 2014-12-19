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

package allout58.mods.techtree.tree;

import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.network.message.UpdateNodeMode;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/6/2014.
 */
public class TechNode implements INode
{
    public static int NEXT_ID = 0;

    //Basic tree structure
    private ArrayList<INode> parents = new ArrayList<INode>();
    private ArrayList<INode> children = new ArrayList<INode>();
    private ArrayList<Integer> parentID = new ArrayList<Integer>();
    private int id = -1;
    private int depth = -1;
    //Tech Tree data
    private String name;
    private int scienceRequired = -1;
    private String description;
    private ItemStack[] lockedItems;
    //Runtime data
    private NodeMode mode = NodeMode.Locked;

    /**
     * @param id ID to assign to this node. Used for (de)serialization.
     */
    public TechNode(int id)
    {
        NEXT_ID = Math.max(id, NEXT_ID) + 1;
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
        parentID.add(parent);
    }

    /**
     * Add a parent node to this node's list of parents
     *
     * @param parent The parent node
     */
    public void addParentNode(INode parent)
    {
        parents.add(parent);
    }

    /**
     * Add a child node to this node's list of children
     *
     * @param child The child node
     */
    public void addChildNode(INode child)
    {
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
    public List<INode> getParents()
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

    @Override
    public NodeMode getMode()
    {
        return mode;
    }

    @Override
    public void setMode(NodeMode mode)
    {
        this.mode = mode;
    }

    public void nextMode()
    {
        this.mode = NodeMode.next(this.mode);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            NetworkManager.INSTANCE.sendToServer(new UpdateNodeMode(FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString(), this.getId(), this.mode));
    }

    public List<INode> getChildren()
    {
        return children;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }
}
