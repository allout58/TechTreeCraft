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

package allout58.mods.techtree.util;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by James Hollowell on 12/11/2014.
 */
public class RenderingHelper
{
    public static class Point
    {
        private double x, y;

        public Point(double x, double y)
        {
            setLocation(x, y);
        }

        public Point(Point p)
        {
            setLocation(p);
        }

        public void setLocation(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        public void setLocation(Point p)
        {
            setLocation(p.getX(), p.getY());
        }

        public double getX()
        {
            return x;
        }

        public double getY()
        {
            return y;
        }
    }

    public static int VERTEX_PER_CURVE = 5;

    private static final double[] dT = new double[] { Math.PI, Math.PI * 3 / 2, 0, Math.PI / 2 };
    private static final double deltaTheta = Math.PI / 2 / VERTEX_PER_CURVE;

    public static void draw2DLine(int x1, int y1, int x2, int y2, float width, int colorRGB)
    {
        float red = (float) (colorRGB >> 16 & 255) / 255.0F;
        float blue = (float) (colorRGB >> 8 & 255) / 255.0F;
        float green = (float) (colorRGB & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);

        GL11.glPushMatrix();
        GL11.glColor3f(red, green, blue);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x1, y1, 0.0D);
        GL11.glVertex3d(x2, y2, 0.0D);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color)
            throws IllegalArgumentException
    {
        drawRoundedRectangle(x, y, width, height, radius, color, color);
    }

    public static void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color, int borderColor)
            throws IllegalArgumentException
    {
        if (radius * 2 > Math.abs(width))
            throw new IllegalArgumentException("Error! Width not large enough for radius!");
        if (radius * 2 > Math.abs(height))
            throw new IllegalArgumentException("Error! Height not large enough for radius!");

        if (width < 0)
        {
            x += width;
            width = Math.abs(width);
        }
        if (height < 0)
        {
            y += height;
            height = Math.abs(height);
        }

        int x1Inner = x + radius;
        int y1Inner = y + radius;
        int x2Inner = x + width - radius;
        int y2Inner = y + height - radius;

        float red = (float) (borderColor >> 16 & 255) / 255.0F;
        float green = (float) (borderColor >> 8 & 255) / 255.0F;
        float blue = (float) (borderColor & 255) / 255.0F;
        float alpha = (float) (borderColor >> 24 & 255) / 255.0F;

        Gui.drawRect(x, y1Inner, x1Inner, y2Inner, borderColor);
        Gui.drawRect(x1Inner, y, x2Inner, y1Inner, borderColor);
        Gui.drawRect(x2Inner, y1Inner, x + width, y2Inner, borderColor);
        Gui.drawRect(x1Inner, y2Inner, x2Inner, y + height, borderColor);
        Gui.drawRect(x1Inner, y1Inner, x2Inner, y2Inner, color);

        ArrayList<Point> curves = new ArrayList<Point>();

        final double[][] start = new double[][] {
                {
                        x1Inner, y1Inner
                },
                {
                        x2Inner, y1Inner
                },
                {
                        x2Inner, y2Inner
                },
                {
                        x1Inner, y2Inner
                }
        };

        for (int corner = 0; corner < 4; corner++)
        {
            curves.clear();
            for (int i = 0; i < VERTEX_PER_CURVE + 1; i++)
            {
                double theta = deltaTheta * i + dT[corner];
                Point p = new Point(Math.cos(theta), Math.sin(theta));
                curves.add(p);
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glPushMatrix();
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            GL11.glVertex3d(start[corner][0], start[corner][1], 0);
            for (int i = curves.size() - 1; i >= 0; i--)
            {
                GL11.glVertex3d((curves.get(i).getX()) * radius + start[corner][0], (curves.get(i).getY()) * radius + start[corner][1], 0);
            }
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }
}
