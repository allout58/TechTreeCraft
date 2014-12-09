package allout58.mods.techtree.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by James Hollowell on 12/8/2014.
 */
public class TechTree
{
    public static Logger log = LogManager.getFormatterLogger("TechTreeMod-TechTree");

    private TechNode head;
    private int depth = 0;
    private int maxWidth = 0;
    private List<Integer> widthPerLevel = new ArrayList<Integer>();
    private HashSet<TechNode> nodes = new HashSet<TechNode>();

    public TechTree(TechNode head)
    {
        this.head = head;
        setup();
    }

    public void setup()
    {
        doMaxDepth(head, 0);
        doMaxWidth();
        log.info("Depth found: " + depth);
        log.info("Max Width found: " + maxWidth);
    }

    private void doMaxDepth(TechNode node, int currDepth)
    {
        assert node != null;
        node.setDepth(Math.max(currDepth + 1, node.getDepth()));
        nodes.add(node);
        if (node.getChildren().size() == 0)
        {
            depth = Math.max(currDepth + 1, depth);
            return;
        }
        for (TechNode child : node.getChildren())
            doMaxDepth(child, currDepth + 1);
    }

    private void doMaxWidth()
    {
        ArrayList<HashSet<TechNode>> list = new ArrayList<HashSet<TechNode>>(depth);
        for (int i = 0; i < depth; i++)
            list.add(new HashSet<TechNode>());
        for (TechNode node : nodes)
        {
            list.get(node.getDepth() - 1).add(node);
        }
        for (int i = 0; i < depth; i++)
            maxWidth = Math.max(list.get(i).size(), maxWidth);
    }
}
