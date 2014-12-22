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

package allout58.mods.techtree.client;

import net.minecraft.client.gui.Gui;

/**
 * Created by James Hollowell on 12/16/2014.
 */
public class GuiElementProgressBar extends GuiElement
{
    private static final float delta = .0001F;

    private float max = 0;
    private float current = 0;
    private long lastTime = 0;
    private int color = 0;
    private int colorBorder = 0;

    public GuiElementProgressBar(int width, int height, int xPosition, int yPosition, float max, int color, int colorBorder)
    {
        super(width, height, xPosition, yPosition);
        this.max = max;
        this.color = color;
        this.colorBorder = colorBorder;
    }

    public void setMax(float max)
    {
        this.max = max;
    }

    @Override
    public void doRender()
    {
        if (enabled && visible)
        {
            if (lastTime == 0)
                current = max;
            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, colorBorder);
            Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, color - 0x002A2A2A);
            update();
            lastTime = System.currentTimeMillis();
            Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + (int) (width * current), yPosition + height - 1, color);
        }
    }

    /*
    Taken with love from KJ4IPS:
    https://github.com/KJ4IPS/Tomfoolrey/blob/master/src/main/java/haun/guru/fooling/gui/elements/GraidentBar.java
    */
    private void update()
    {
        //This line disables the slewing for now...
        current = max;
        if (current != max)
        {
            float diff = delta * (System.currentTimeMillis() - lastTime);

            if (Math.abs(max - current) < diff)
            {
                current = max;
            }
            else
            {
                byte dir = (byte) ((max - current) > 0 ? 1 : -1);
                current += diff * dir;
            }
        }
    }
}
