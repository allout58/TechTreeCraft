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
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.util.RenderingHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by James Hollowell on 1/2/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiEditTree2 extends GuiScreen
{
    private static final Logger log = LogManager.getLogger();

    public int renderWidth = 300;
    public int renderHeight = 240;

    protected TechTree tree;
    protected Map<Integer, AbstractGuiButtonNode> buttons = new HashMap<Integer, AbstractGuiButtonNode>();
    protected GuiButtonEditNode selectedButton;

    protected GuiButton deleteButton;
    protected GuiButton editButton;
    protected GuiButton newButton;
    protected boolean isEditing = false;

    protected List<Integer> xCoords;
    protected List<Integer> yCoords;
    protected int nodeHeight;
    protected int nodeWidth;

    public GuiEditTree2(TechTree tree)
    {
        super();
        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        deleteButton = new GuiButton(-1, GuiTree.X_START + 5, GuiTree.Y_START + 5, 40, 20, "Delete");
        deleteButton.enabled = false;
        editButton = new GuiButton(-2, GuiTree.X_START + 50, GuiTree.Y_START + 5, 40, 20, "Edit");
        editButton.enabled = false;
        newButton = new GuiButton(-3, GuiTree.X_START + 100, GuiTree.Y_START + 5, 40, 20, "New");
        newButton.visible = true;

        buttonList.add(deleteButton);
        buttonList.add(editButton);
        buttonList.add(newButton);

        buttons.clear();

        renderWidth = width - GuiTree.X_START * 2;
        renderHeight = height - GuiTree.Y_START * 2;

        xCoords = new ArrayList<Integer>(tree.getDepth());

        nodeWidth = (renderWidth - GuiTree.PAD_X * tree.getDepth()) / tree.getDepth();
        nodeWidth = Math.max(nodeWidth, GuiTree.MIN_NODE_WIDTH);
        nodeWidth = Math.min(nodeWidth, GuiTree.MAX_NODE_WIDTH);

        for (int i = 0; i < tree.getDepth() + 1; i++)
            xCoords.add(i, nodeWidth * i + GuiTree.PAD_X * (i + 1) + GuiTree.X_START);

        nodeHeight = (renderHeight - GuiTree.PAD_Y * tree.getMaxWidth()) / tree.getMaxWidth();
        nodeHeight = Math.max(nodeHeight, GuiTree.MIN_NODE_HEIGHT);
        nodeHeight = Math.min(nodeHeight, GuiTree.MAX_NODE_HEIGHT);

        yCoords = new ArrayList<Integer>(tree.getMaxWidth());

        for (int i = 0; i < tree.getMaxWidth() + 1; i++)
            yCoords.add(i, nodeHeight * i + GuiTree.PAD_Y * (i + 1) + 40);

        for (TreeSet<TechNode> list : tree.getList())
        {
            int treeWidth = list.size();

            //int yStart = (height - HEIGHT) / 2 + ((tree.getMaxWidth() - treeWidth - 1) * nodeHeight + (treeWidth - tree.getMaxWidth()) * GuiTree.PAD_Y) / 2 + 15;
            int yLoc = (tree.getMaxWidth() - list.size()) / 2;

            Iterator<TechNode> it = list.iterator();
            for (int i = 0; i < treeWidth; i++)
            {
                TechNode node = it.next();
                GuiButtonEditNode btn = new GuiButtonEditNode(node.getId(), xCoords.get(node.getDepth() - 1), yCoords.get(i + yLoc), nodeWidth, nodeHeight, node);

                buttons.put(node.getId(), btn);
                buttonList.add(btn);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button instanceof GuiButtonEditNode)
        {
            if (selectedButton == button)
            {
                selectedButton.isSelected = false;
                selectedButton = null;
            }
            else
            {
                if (selectedButton != null)
                    selectedButton.isSelected = false;
                selectedButton = (GuiButtonEditNode) button;
                selectedButton.isSelected = true;
            }
        }
        else
        {
            switch (button.id)
            {
                case -1:
                    if (selectedButton != null)
                        deleteNode(selectedButton.getNode());
                    else
                        log.error("Trying to delete with no selected node");
                    break;
                case -2:
                    if (selectedButton != null)
                        editNode(selectedButton.getNode());
                    else
                        log.error("Trying to edit with no selected node");
                    break;
                case -3:
                    newNode();
                    break;
                default:
                    log.error("Unknown button: " + button.id);
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int button)
    {
        if (selectedButton != null && button != -1)
        {
            for (AbstractGuiButtonNode btn : buttons.values())
            {
                if (!(btn instanceof GuiButtonEditNode))
                {
                    log.error("Not GuiButtonEditNode in GuiEditTree's button list!");
                    return;
                }
                if (btn != selectedButton && btn.mousePressed(this.mc, mouseX, mouseY))
                {
                    if (button == 1)
                    {
                        if (selectedButton.getNode().getParents().contains(btn.getNode()))
                        {
                            selectedButton.getNode().getParents().remove(btn.getNode());
                            selectedButton.getNode().getParentID().remove(Integer.valueOf(btn.getNode().getId()));

                            btn.getNode().getChildren().remove(selectedButton.getNode());
                        }
                        else
                        {
                            selectedButton.getNode().addParentNode(btn.getNode());
                            selectedButton.getNode().addParentNode(btn.getNode().getId());

                            btn.getNode().addChildNode(selectedButton.getNode());
                        }
                    }
                    else if (button == 0)
                    {
                        if (btn.getNode().getParents().contains(selectedButton.getNode()))
                        {
                            btn.getNode().getParents().remove(selectedButton.getNode());
                            btn.getNode().getParentID().remove(Integer.valueOf(selectedButton.getNode().getId()));

                            selectedButton.getNode().getChildren().remove(btn.getNode());
                        }
                        else
                        {
                            btn.getNode().addParentNode(selectedButton.getNode());
                            btn.getNode().addParentNode(selectedButton.getNode().getId());

                            selectedButton.getNode().addChildNode(btn.getNode());
                        }
                    }
                    selectedButton.isSelected = false;
                    selectedButton = null;
                    tree.undoFakeNodes();
                    for (TechNode node : tree.getNodes())
                        node.setDepth(0);
                    tree.setup();
                    buttonList.clear();
                    this.initGui();
                    return;
                }
            }
        }

        super.mouseMovedOrUp(mouseX, mouseX, button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials)
    {
        deleteButton.enabled = editButton.enabled = selectedButton != null;

        drawBackground();
        drawForeground();

        super.drawScreen(mouseX, mouseY, renderPartials);

        drawOverlay(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key)
    {
        super.keyTyped(c, key);
        if (Keyboard.KEY_DELETE == key || Keyboard.KEY_BACK == key)
        {
            if (selectedButton != null)
                deleteNode(selectedButton.getNode());
        }
        else if (c == 'f')
        {
            tree.undoFakeNodes();
            initGui();
        }
    }

    protected void drawBackground()
    {
        drawRect(GuiTree.X_START, GuiTree.Y_START, GuiTree.X_START + renderWidth, GuiTree.Y_START + renderHeight, Config.INSTANCE.client.colorBackground);
    }

    protected void drawForeground()
    {
        final int xDiff = GuiTree.PAD_X / 2;
        final int yDiff = GuiTree.PAD_Y / 2;
        int x1 = xCoords.get(0) - xDiff;
        int x2 = xCoords.get(xCoords.size() - 1) - xDiff;
        int y1 = yCoords.get(0) - yDiff;
        int y2 = yCoords.get(yCoords.size() - 1) - yDiff;
        for (int y : yCoords)
        {
            drawHorizontalLine(x1, x2, y - yDiff, 0xFF000000);
        }
        for (int x : xCoords)
        {
            drawVerticalLine(x - xDiff, y1, y2, 0xFF000000);
        }

        TreeRenderingHelper.renderConnectorLines(buttons);
    }

    @SuppressWarnings("unchecked")
    protected void drawOverlay(int mouseX, int mouseY)
    {
        for (AbstractGuiButtonNode btn : buttons.values())
        {
            if (btn.mousePressed(this.mc, mouseX, mouseY))
            {
                int w = Math.max(fontRendererObj.getStringWidth(btn.getNode().getName()) + 20, 100);
                int h = (int) ((fontRendererObj.listFormattedStringToWidth(btn.getNode().getDescription(), w).size() + 1) * fontRendererObj.FONT_HEIGHT * 0.5 + fontRendererObj.FONT_HEIGHT + 35);
                if (mouseX > width / 2)
                {
                    mouseX -= w;
                }

                try
                {
                    RenderingHelper.drawRoundedRectangle(mouseX + 2, mouseY + 2, w, h, 7, Config.INSTANCE.client.colorOverlayBackground);
                    fontRendererObj.drawString(btn.getNode().getName(), mouseX + 2 + 6, mouseY + 2 + 6, Config.INSTANCE.client.colorOverlayText, true);
                    drawHorizontalLine(mouseX + 7, mouseX + w - 7, mouseY + 10 + fontRendererObj.FONT_HEIGHT, Config.INSTANCE.client.colorOverlayOther);

                    GL11.glPushMatrix();
                    GL11.glScaled(0.5, 0.5, 0);
                    GL11.glTranslated(mouseX, mouseY, 0);
                    fontRendererObj.drawString(StatCollector.translateToLocalFormatted("gui.scienceRequired", btn.getNode().getScienceRequired()), mouseX + 14, mouseY + 17 + fontRendererObj.FONT_HEIGHT * 3, Config.INSTANCE.client.colorOverlayText);
                    fontRendererObj.drawSplitString(btn.getNode().getDescription(), mouseX + 14, mouseY + 22 + fontRendererObj.FONT_HEIGHT * 4, w * 2 - 10, Config.INSTANCE.client.colorOverlayText);
                    GL11.glPopMatrix();

                    for (int i = 0; i < btn.getNode().getLockedItems().length; i++)
                    {
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        drawRect(mouseX + 14 + 18 * i, mouseY + h - 18, mouseX + 30 + 18 * i, mouseY + h - 2, 0xFFB0B0B0);
                        itemRender.renderItemIntoGUI(fontRendererObj, Minecraft.getMinecraft().renderEngine, btn.getNode().getLockedItems()[i], mouseX + 14 + 18 * i, mouseY + h - 18);
                    }

                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void deleteNode(TechNode node)
    {
        if (node != null)
            for (TechNode child : node.getChildren())
            {
                for (TechNode parent : node.getParents())
                {
                    child.addParentNode(parent);
                    child.addParentNode(parent.getId());

                    parent.addChildNode(child);

                    parent.getChildren().remove(node);
                }
                child.getParents().remove(node);
            }
    }

    protected void newNode()
    {
        //editNode(new TechNode());
    }

    protected void editNode(TechNode node)
    {
        isEditing = true;
    }
}
