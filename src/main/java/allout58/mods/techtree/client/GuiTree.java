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

import allout58.mods.techtree.client.elements.AbstractGuiButtonNode;
import allout58.mods.techtree.client.elements.GuiButtonTechNode;
import allout58.mods.techtree.config.Config;
import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.network.message.ChangeNodeMode;
import allout58.mods.techtree.network.message.RequestAll;
import allout58.mods.techtree.network.message.RequestResearch;
import allout58.mods.techtree.research.ResearchClient;
import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.NodeMode;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.util.RenderingHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by James Hollowell on 12/7/2014.
 */
@SideOnly(Side.CLIENT)
public class GuiTree extends GuiScreen
{

    public static final int MIN_NODE_WIDTH = 40;
    public static final int MIN_NODE_HEIGHT = 20;

    public static final int MAX_NODE_WIDTH = 60;
    public static final int MAX_NODE_HEIGHT = 40;

    public static final int PAD_X = 20;
    public static final int PAD_Y = 15;

    public static final int X_START = 15;
    public static final int Y_START = 15;

    public int renderWidth = 300;
    public int renderHeight = 240;

    private TechTree tree;
    private Map<Integer, AbstractGuiButtonNode> buttons = new HashMap<Integer, AbstractGuiButtonNode>();
    private String uuid = "";

    public GuiTree(TechTree tree, UUID player)
    {
        super();
        this.tree = tree;
        uuid = player.toString();

        if (!ResearchClient.getInstance(uuid).isUpdated())
            for (TechNode node : tree.getNodes())
                if (node.getClass().equals(TechNode.class))
                    NetworkManager.INSTANCE.sendToServer(new RequestResearch(uuid, node.getId()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        NetworkManager.INSTANCE.sendToServer(new RequestAll(FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString()));

        renderWidth = width - X_START * 2;
        renderHeight = height - Y_START * 2;

        List<Integer> xCoords = new ArrayList<Integer>(tree.getDepth());

        int nodeWidth = (renderWidth - PAD_X * tree.getDepth()) / tree.getDepth();
        nodeWidth = Math.max(nodeWidth, MIN_NODE_WIDTH);
        nodeWidth = Math.min(nodeWidth, MAX_NODE_WIDTH);

        for (int i = 0; i < tree.getDepth(); i++)
            xCoords.add(i, nodeWidth * i + PAD_X * (i + 1) + X_START);

        int nodeHeight = (renderHeight - PAD_Y * tree.getMaxWidth()) / tree.getMaxWidth();
        nodeHeight = Math.max(nodeHeight, MIN_NODE_HEIGHT);
        nodeHeight = Math.min(nodeHeight, MAX_NODE_HEIGHT);

        List<Integer> yCoords = new ArrayList<Integer>(tree.getMaxWidth());

        for (int i = 0; i < tree.getMaxWidth(); i++)
            yCoords.add(i, nodeHeight * i + PAD_Y * (i + 1));

        for (TreeSet<TechNode> list : tree.getList())
        {
            int treeWidth = list.size();

            //int yStart = (height - HEIGHT) / 2 + ((tree.getMaxWidth() - treeWidth - 1) * nodeHeight + (treeWidth - tree.getMaxWidth()) * PAD_Y) / 2 + 15;
            int yLoc = (tree.getMaxWidth() - list.size()) / 2;

            Iterator<TechNode> it = list.iterator();
            for (int i = 0; i < treeWidth; i++)
            {
                TechNode node = it.next();
                GuiButtonTechNode btn = new GuiButtonTechNode(node.getId(), xCoords.get(node.getDepth() - 1), yCoords.get(i + yLoc) + 20, nodeWidth, nodeHeight, node);

                buttons.put(node.getId(), btn);
                buttonList.add(btn);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button instanceof GuiButtonTechNode)
        {
            TechNode node = ((GuiButtonTechNode) button).getNode();
            String playerUuid = FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString();
            NodeMode mode = ResearchClient.getInstance(playerUuid).getMode(node.getId());
            if (mode == NodeMode.Unlocked)
            {
                mode = NodeMode.next(mode);
                ResearchClient.getInstance(playerUuid).setMode(node.getId(), mode);
                NetworkManager.INSTANCE.sendToServer(new ChangeNodeMode(playerUuid, node.getId(), mode));
            }
            else if (mode == NodeMode.Researching)
            {
                mode = NodeMode.Unlocked;
                ResearchClient.getInstance(playerUuid).setMode(node.getId(), mode);
                NetworkManager.INSTANCE.sendToServer(new ChangeNodeMode(playerUuid, node.getId(), mode));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials)
    {
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
        //TODO REMOVE AFTER DEBUG
        super.keyTyped(c, key);
        if (c == 'r')
        {
            NetworkManager.INSTANCE.sendToServer(new RequestAll(FMLClientHandler.instance().getClient().thePlayer.getUniqueID().toString()));
        }
        if (c == 'f')
        {
            for (GuiButton btn : buttons.values())
                if (((GuiButtonTechNode) btn).getNode() instanceof FakeNode)
                    btn.visible = !btn.visible;
        }
    }

    protected void drawBackground()
    {
        drawRect(X_START, Y_START, X_START + renderWidth, Y_START + renderHeight, Config.INSTANCE.client.colorBackground);
    }

    protected void drawForeground()
    {
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
                if (mouseX < width / 2)
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
}
