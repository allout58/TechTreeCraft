package allout58.mods.techtree.tree;

import java.util.List;

/**
 * Created by James Hollowell on 12/10/2014.
 */
public interface INode
{
    List<INode> getParents();

    List<INode> getChildren();

    List<Integer> getParentID();

    int getDepth();

    void setDepth(int depth);

    int getId();

    String getName();

    String getDescription();
}
