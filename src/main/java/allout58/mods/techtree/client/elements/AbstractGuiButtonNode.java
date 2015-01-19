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

package allout58.mods.techtree.client.elements;

import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by James Hollowell on 1/2/2015.
 */
public abstract class AbstractGuiButtonNode extends GuiButton
{
    protected TechNode node;

    public AbstractGuiButtonNode(int id, int x, int y, int width, int height, TechNode node)
    {
        super(id, x, y, width, height, "");
        this.node = node;
        if (node instanceof FakeNode)
        {
            this.visible = this.enabled = false;
            this.height = 2;
        }
    }

    public TechNode getNode()
    {
        return node;
    }

    public int getInX()
    {
        return xPosition;
    }

    public int getInY()
    {
        return (yPosition * 2 + height) / 2;
    }

    public int getOutX()
    {
        return xPosition + width;
    }

    public int getOutY()
    {
        return (yPosition * 2 + height) / 2;
    }
}
