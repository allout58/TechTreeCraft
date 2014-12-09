package allout58.mods.techtree.tree;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/6/2014.
 */
public class TechNode
{
    public static int NEXT_ID = 0;

    //Basic tree structure
    private ArrayList<TechNode> parents = new ArrayList<TechNode>();
    private ArrayList<TechNode> children = new ArrayList<TechNode>();
    private ArrayList<Integer> parentID = new ArrayList<Integer>();
    private int id = -1;
    private int depth = -1;
    //Tech Tree data
    private String name;
    private int scienceRequired = -1;
    private String description;
    private Item[] lockedItems;

    /**
     * @param id ID to assign to this node. Used for (de)serialization.
     */
    public TechNode(int id)
    {
        NEXT_ID = Math.max(id, NEXT_ID) + 1;
        this.id = id;
    }

    /**
     * Add a parent node's id to this node's list. Used only on deserialization
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
    public void addParentNode(TechNode parent)
    {
        parents.add(parent);
    }

    /**
     * Add a child node to this node's list of children
     *
     * @param child The child node
     */
    public void addChildNode(TechNode child)
    {
        children.add(child);
    }

    /** Setup this node
     *
     * @param name The node's name. Used as a header in-game
     * @param scienceReq Science points required to unlock this node
     * @param desc Description of the node.
     * @param lockItems Items locked by this node. Unlocking the node allows them to be crafted again.
     */
    public void setup(String name, int scienceReq, String desc, Item[] lockItems)
    {
        this.name = name;
        this.scienceRequired = scienceReq;
        this.description = desc;
        this.lockedItems = lockItems;
    }

    /**
     *
     * @return The node's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @return The node's description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @return The science required to unlock this node
     */
    public int getScienceRequired()
    {
        return scienceRequired;
    }

    /**
     *
     * @return The node's ID used for initial linking and (de)serialization
     */
    public int getId()
    {
        return id;
    }

    /**Should only be used during (de)serialization
     *
     * @return The node's list of parents (in ID form)
     */
    public List<Integer> getParentID()
    {
        return parentID;
    }

    /**
     *
     * @return The node's list of parents (in Node form)
     */
    public List<TechNode> getParents()
    {
        return parents;
    }

    /**
     *
     * @return The items locked by this node
     */
    public Item[] getLockedItems()
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
        this.depth = depth;
    }
}
