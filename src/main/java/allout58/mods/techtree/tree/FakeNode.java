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
}
