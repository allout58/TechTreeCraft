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

package allout58.mods.techtree.client;

import allout58.mods.techtree.config.Config;
import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.util.RenderingHelper;

import java.util.Map;

/**
 * Created by James Hollowell on 1/1/2015.
 */
public class TreeRenderingHelper
{
    public static void renderConnectorLines(Map<Integer, AbstractGuiButtonNode> buttons)
    {
        for (AbstractGuiButtonNode btn : buttons.values())
        {
            TechNode btnNode = btn.getNode();
            for (int node : btnNode.getParentID())
            {
                RenderingHelper.draw2DLine(btn.getInX(), btn.getInY(), buttons.get(node).getOutX(), buttons.get(node).getOutY(), .75f, Config.INSTANCE.client.colorConnectors);
            }
            if (btnNode.getClass().equals(FakeNode.class))
            {
                RenderingHelper.draw2DLine(btn.getInX(), btn.getInY(), btn.getOutX(), btn.getOutY(), .75f, Config.INSTANCE.client.colorConnectors);
            }
        }
    }
}
