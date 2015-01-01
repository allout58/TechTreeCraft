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

import allout58.mods.techtree.research.ResearchClient;
import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.TechNode;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

/**
 * Created by James Hollowell on 12/9/2014.
 */
public class GuiButtonTechNode extends GuiButton
{
    public GuiElementProgressBar bar;

    private TechNode node;

    public GuiButtonTechNode(int id, int x, int y, int width, int height, TechNode node)
    {
        super(id, x, y, width, height, "");

        try
        {
            bar = new GuiElementProgressBar(width - 10, 5, x + 5, y + height - 8, (float) (ResearchClient.getInstance().getResearch(node.getId())) / (float) (node.getScienceRequired()), 0xFF119911, 0xFF991111);
            bar.setVisible(true);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if (node instanceof FakeNode)
        {
            this.visible = this.enabled = false;
            this.height = 2;
            bar.setVisible(false);
        }

        this.node = node;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            try
            {
                bar.setMax((float) (ResearchClient.getInstance().getResearch(node.getId())) / (float) (node.getScienceRequired()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            FontRenderer fontRenderer = mc.fontRenderer;
            boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            String playerUUID = FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString();

            switch (ResearchClient.getInstance(playerUUID).getMode(node.getId()))
            {
                case Locked:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF777777, 0xFF333333);
                    break;
                case Unlocked:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFFAAAAAA, 0xFF656565);
                    try
                    {
                        bar.setEnabled(ResearchClient.getInstance().getResearch(node.getId()) > 0);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case Researching:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF9999FF, 0xFF6565a5);
                    bar.setEnabled(true);
                    break;
                case Completed:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF2CC9B7, 0xFF3F998E);
                    bar.setEnabled(false);
                    break;
                default:
                    System.err.println("ERROR! Invalid button state! o.O");
            }

            if (mouseOver)
            {
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0x30FFFFFF);
            }

            GL11.glPushMatrix();

            GL11.glScaled(0.75, 0.75, 0.75);
            GL11.glTranslated(xPosition * .33, yPosition * .33, 0);
            fontRenderer.drawString(node.getName(), xPosition + 2, yPosition + 2, 0xFFFFFFFF, true);

            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScaled(.5, .5, .5);
            GL11.glTranslated(xPosition, yPosition, 0);
            fontRenderer.drawString(ResearchClient.getInstance(playerUUID).getMode(node.getId()).name(), xPosition + 2, yPosition + 10 + fontRenderer.FONT_HEIGHT, 0xFFFFFFFF, false);
            //fontRenderer.drawSplitString(node.getDescription(), xPosition + 4, yPosition + 18 + fontRenderer.FONT_HEIGHT * 2, width * 2, 0xFFFFFFFF);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScaled(1, 1, 1);
            bar.doRender();
            GL11.glPopMatrix();

            this.mouseDragged(mc, mouseX, mouseY);
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
