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

/**
 * Created by James Hollowell on 12/16/2014.
 */
public abstract class GuiElement
{
    /**
     * Element width in pixels
     */
    protected int width;
    /**
     * Elements height in pixels
     */
    protected int height;
    /**
     * The x position of this control.
     */
    protected int xPosition;
    /**
     * The y position of this control.
     */
    protected int yPosition;
    /**
     * True if this control is enabled, false to disable.
     */
    protected boolean enabled;
    /**
     * Hides the button completely if false.
     */
    protected boolean visible;

    public GuiElement(int width, int height, int xPosition, int yPosition)
    {
        this.width = width;
        this.height = height;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public abstract void doRender();

    /*
        Getters and setters
     */
    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getxPosition()
    {
        return xPosition;
    }

    public void setxPosition(int xPosition)
    {
        this.xPosition = xPosition;
    }

    public int getyPosition()
    {
        return yPosition;
    }

    public void setyPosition(int yPosition)
    {
        this.yPosition = yPosition;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
