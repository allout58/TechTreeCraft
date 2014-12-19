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

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/10/2014.
 */
public class FakeNode implements INode
{
    private List<INode> parents = new ArrayList<INode>();
    private List<INode> children = new ArrayList<INode>();
    private List<Integer> pID = new ArrayList<Integer>();
    private int id = 0;
    private int depth = 0;
    private NodeMode mode = NodeMode.Locked;

    public FakeNode(INode parent, INode child, int id)
    {
        parents.add(parent);
        children.add(child);
        pID.add(parent.getId());
        this.id = id;
    }

    public FakeNode(FakeNode copy)
    {
        this(copy.getParents().get(0), copy.getChildren().get(0), copy.getId());
        this.setDepth(copy.getDepth());
    }

    @Override
    public List<INode> getParents()
    {
        return parents;
    }

    @Override
    public List<INode> getChildren()
    {
        return children;
    }

    @Override
    public List<Integer> getParentID()
    {
        return pID;
    }

    @Override
    public int getDepth()
    {
        return depth;
    }

    @Override
    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return "FakeNode" + id;
    }

    @Override
    public String getDescription()
    {
        return "What description?";
    }

    @Override
    public ItemStack[] getLockedItems()
    {
        return new ItemStack[] { };
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
    }

    @Override
    public int getScienceRequired()
    {
        return -1;
    }

}
