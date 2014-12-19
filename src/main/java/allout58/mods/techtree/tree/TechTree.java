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

    private INode head;
    private int depth = 0;
    private int maxWidth = 0;
    private ArrayList<HashSet<INode>> list = new ArrayList<HashSet<INode>>(depth);
    private HashSet<INode> nodes = new HashSet<INode>();

    public TechTree(TechNode head)
    {
        this.head = head;
        setup();
    }

    public void setup()
    {
        doMaxDepth(head, 0);
        for (int i = 0; i < depth; i++)
            list.add(new HashSet<INode>());
        doMaxWidth();
        log.info("Depth found: " + depth);
        log.info("Max Width found: " + maxWidth);
        doMaxWidth2();
        log.info("Max widnt 2: " + maxWidth);
        doFakeNodes();
    }

    private void doMaxDepth(INode node, int currDepth)
    {
        assert node != null;
        node.setDepth(Math.max(currDepth + 1, node.getDepth()));
        nodes.add(node);
        if (node.getChildren().size() == 0)
        {
            depth = Math.max(currDepth + 1, depth);
            return;
        }
        for (INode child : node.getChildren())
            doMaxDepth(child, currDepth + 1);
    }

    private void doMaxWidth()
    {
        for (INode node : nodes)
        {
            list.get(node.getDepth() - 1).add(node);
        }
        for (int i = 0; i < depth; i++)
            maxWidth = Math.max(list.get(i).size(), maxWidth);
    }

    private void doMaxWidth2()
    {
        for (INode node : nodes)
            maxWidth = Math.max(maxWidth, node.getChildren().size());
    }

    private void doFakeNodes()
    {
        //TODO Handle more than one depth difference between parents and children
        for (INode node : nodes)
        {
            for (int i = 0; i < node.getParents().size(); i++)
            {
                INode parent = node.getParents().get(i);
                if (parent.getDepth() != node.getDepth() - 1)
                {
                    INode ne = new FakeNode(parent, node, TechNode.NEXT_ID++);

                    ne.setDepth(node.getDepth() - 1);
                    list.get(ne.getDepth() - 1).add(ne);

                    node.getParents().remove(parent);
                    node.getParents().add(ne);

                    node.getParentID().remove(parent.getId());
                    node.getParentID().add(ne.getId());

                    parent.getChildren().remove(node);
                    parent.getChildren().add(ne);
                }
            }
        }
    }

    public int getDepth()
    {
        return depth;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public List<HashSet<INode>> getList()
    {
        return list;
    }

    public HashSet<INode> getNodes()
    {
        return nodes;
    }
}
