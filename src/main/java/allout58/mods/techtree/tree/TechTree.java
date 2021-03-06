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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by James Hollowell on 12/8/2014.
 */
public class TechTree
{
    public static Logger log = LogManager.getFormatterLogger("TechTreeMod-TechTree");

    private TechNode head;
    private int depth = 0;
    private int maxWidth = 0;
    private ArrayList<TreeSet<TechNode>> list = new ArrayList<TreeSet<TechNode>>();
    private Set<TechNode> nodes = new TreeSet<TechNode>();
    private Map<Integer, TechNode> nodeMap = new HashMap<Integer, TechNode>();

    private int nextID = 0;

    public TechTree(TechNode head)
    {
        this.head = head;
        setup();
    }

    public void setup()
    {
        list.clear();
        nodes.clear();
        nodeMap.clear();
        depth = 0;
        maxWidth = 0;
        doMaxDepth(head, 0);
        for (int i = 0; i < depth; i++)
            list.add(new TreeSet<TechNode>());
        //        doMaxWidth();
        log.info("Depth found: " + depth);
        //        log.info("Max Width found: " + maxWidth);
        doMaxWidth2();
        nextID = nodes.size() + 1;
        log.info("Max width 2: " + maxWidth);
        doFakeNodes();
    }

    public int getNextID()
    {
        return nextID++;
    }

    private void doMaxDepth(TechNode node, int currDepth)
    {
        assert node != null;
        node.setDepth(Math.max(currDepth + 1, node.getDepth()));
        nodes.add(node);
        nodeMap.put(node.getId(), node);
        if (node.getChildren().size() == 0)
        {
            depth = Math.max(currDepth + 1, depth);
            return;
        }
        for (TechNode child : node.getChildren())
        {
            if (child == node)
                continue;
            if (child.getChildren().contains(node))
            {
                child.getChildren().remove(node);
                continue;
            }
            doMaxDepth(child, currDepth + 1);
        }
    }

    //    private void doMaxWidth()
    //    {
    //        for (TechNode node : nodes)
    //        {
    //            list.get(node.getDepth() - 1).add(node);
    //        }
    //        for (int i = 0; i < depth; i++)
    //            maxWidth = Math.max(list.get(i).size(), maxWidth);
    //    }

    private void doMaxWidth2()
    {
        maxWidth = 0;
        for (TechNode node : nodes)
        {
            list.get(node.getDepth() - 1).add(node);
        }
        for (TechNode node : nodes)
            maxWidth = Math.max(maxWidth, node.getChildren().size());
    }

    public void undoFakeNodes()
    {
        //Todo Test this method ;)
        List<TechNode> toRemove = new ArrayList<TechNode>();
        for (TechNode node : nodes)
        {
            if (node instanceof FakeNode)
            {
                toRemove.add(node);
                list.get(node.getDepth() - 1).remove(node);

                TechNode parent = node.getParents().get(0);
                TechNode child = node.getChildren().get(0);

                parent.getChildren().remove(node);
                parent.getChildren().add(child);

                child.getParents().remove(node);
                child.getParents().add(parent);

                child.getParentID().remove((Integer) node.getId());
                child.getParentID().add(parent.getId());
            }
        }
        nodes.removeAll(toRemove);
    }

    private void doFakeNodes()
    {
        //TODO TEST: Handle more than one depth difference between parents and children
        List<TechNode> toAdd = new ArrayList<TechNode>();
        int fakeNodeID = -1;
        do
        {
            toAdd.clear();
            for (TechNode node : nodes)
            {
                for (int i = 0; i < node.getParents().size(); i++)
                {
                    TechNode parent = node.getParents().get(i);
                    if (parent.getDepth() != node.getDepth() - 1)
                    {
                        TechNode ne = new FakeNode(parent, node, fakeNodeID--);

                        ne.setDepth(node.getDepth() - 1);
                        list.get(ne.getDepth() - 1).add(ne);
                        toAdd.add(ne);

                        node.getParents().remove(parent);
                        node.getParents().add(ne);

                        node.getParentID().remove((Integer) parent.getId());
                        node.getParentID().add(ne.getId());

                        parent.getChildren().remove(node);
                        parent.getChildren().add(ne);
                    }
                }
            }
            nodes.addAll(toAdd);
        } while (toAdd.size() > 0);
    }

    public int getDepth()
    {
        return depth;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public List<TreeSet<TechNode>> getList()
    {
        return list;
    }

    public Set<TechNode> getNodes()
    {
        return nodes;
    }

    public Set<TechNode> getRealNodes()
    {
        Set<TechNode> out = new TreeSet<TechNode>();
        for (TechNode n : nodes)
        {
            if (n instanceof FakeNode) continue;
            out.add(n);
        }
        return out;
    }

    public TechNode getNodeByID(int id)
    {
        return nodeMap.get(id);
    }

    public void add(TechNode node)
    {
        log.info("Adding new node...");
        head.addChildNode(node);
        node.addParentNode(head);
        node.addParentNode(head.getId());
        nodeMap.put(node.getId(), node);
        nodes.add(node);
        for (int d = depth; d < node.getDepth(); d++)
            list.add(new TreeSet<TechNode>());
        list.get(node.getDepth() - 1).add(node);
        depth = list.size();
        log.info("New depth: " + depth);
        doMaxWidth2();
        log.info("New max width: " + maxWidth);
        //        setup();
    }

    public void remove(TechNode node)
    {
        log.info("Removing node...");
        for (TechNode parent : node.getParents())
        {
            for (TechNode child : node.getChildren())
            {
                child.addParentNode(parent);
                child.addParentNode(parent.getId());

                parent.addChildNode(child);

                child.getParents().remove(node);
            }
            parent.getChildren().remove(node);
        }
        nodes.remove(node);
        nodeMap.remove(node.getId());
        int d = node.getDepth() - 1;
        list.get(d).remove(node);
        if (list.get(d).size() == 0)
        {
            list.remove(node.getDepth() - 1);
        }

        depth = list.size();
        log.info("New depth: " + depth);
        doMaxWidth2();
        log.info("New max width: " + maxWidth);
    }
}
