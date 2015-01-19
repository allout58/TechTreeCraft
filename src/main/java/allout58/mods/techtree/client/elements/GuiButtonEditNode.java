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

import allout58.mods.techtree.config.Config;
import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by James Hollowell on 1/1/2015.
 */
public class GuiButtonEditNode extends AbstractGuiButtonNode
{
    public boolean isSelected = false;

    public GuiButtonEditNode(int id, int x, int y, int width, int height, TechNode node)
    {
        super(id, x, y, width, height, node);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontRenderer = mc.fontRenderer;
            boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, Config.INSTANCE.client.colorBtnUnlocked1, Config.INSTANCE.client.colorBtnUnlocked2);

            if (isSelected)
            {
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0x99101010);
            }

            if (mouseOver)
            {
                drawRect(xPosition - 2, yPosition - 2, xPosition + width + 2, yPosition + height + 2, 0x30FFFFFF);
            }

            GL11.glPushMatrix();

            GL11.glScaled(0.75, 0.75, 0.75);
            GL11.glTranslated(xPosition * .33, yPosition * .33, 0);
            fontRenderer.drawString(node.getName(), xPosition + 2, yPosition + 2, Config.INSTANCE.client.colorBtnText, true);

            GL11.glPopMatrix();

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
