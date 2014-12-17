package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.INode;
import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import allout58.mods.techtree.util.RenderingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by James Hollowell on 12/7/2014.
 */
public class GuiTree extends GuiScreen
{
    public static final int WIDTH = 300;
    public static final int HEIGHT = 240;

    public static final int MIN_NODE_WIDTH = 40;
    public static final int MIN_NODE_HEIGHT = 20;

    public static final int MAX_NODE_WIDTH = 60;
    public static final int MAX_NODE_HEIGHT = 40;

    public static final int PAD_X = 20;
    public static final int PAD_Y = 15;

    public int xStart;

    private TechTree tree;
    private GuiButtonTechNode[] buttons;

    public GuiTree(TechTree tree)
    {
        super();
        this.tree = tree;
        buttons = new GuiButtonTechNode[TechNode.NEXT_ID];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        xStart = (width - WIDTH) / 2;

        List<Integer> xCoords = new ArrayList<Integer>(tree.getDepth());

        int nodeWidth = (WIDTH - PAD_X * tree.getDepth()) / tree.getDepth();
        nodeWidth = Math.max(nodeWidth, MIN_NODE_WIDTH);
        nodeWidth = Math.min(nodeWidth, MAX_NODE_WIDTH);

        for (int i = 0; i < tree.getDepth(); i++)
            xCoords.add(i, nodeWidth * i + PAD_X * (i + 1) + xStart);

        int nodeHeight = (HEIGHT - PAD_Y * tree.getMaxWidth()) / tree.getMaxWidth();
        nodeHeight = Math.max(nodeHeight, MIN_NODE_HEIGHT);
        nodeHeight = Math.min(nodeHeight, MAX_NODE_HEIGHT);

        List<Integer> yCoords = new ArrayList<Integer>(tree.getMaxWidth());

        for (int i = 0; i < tree.getMaxWidth(); i++)
            yCoords.add(i, nodeHeight * i + PAD_Y * (i + 1));

        for (HashSet<INode> list : tree.getList())
        {
            int treeWidth = list.size();

            //int yStart = (height - HEIGHT) / 2 + ((tree.getMaxWidth() - treeWidth - 1) * nodeHeight + (treeWidth - tree.getMaxWidth()) * PAD_Y) / 2 + 15;
            int yLoc = (tree.getMaxWidth() - list.size()) / 2;

            Iterator<INode> it = list.iterator();
            for (int i = 0; i < treeWidth; i++)
            {
                INode node = it.next();
                GuiButtonTechNode btn = new GuiButtonTechNode(node.getId(), xCoords.get(node.getDepth() - 1), yCoords.get(i + yLoc) + 20, nodeWidth, nodeHeight, node);

                buttons[node.getId()] = btn;
                buttonList.add(btn);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button instanceof GuiButtonTechNode)
            ((GuiButtonTechNode) button).nextMode();
    }

    //    private void updateButtonState()
    //    {
    //        next.visible = pageIndex < pages.size();
    //        prev.visible = pageIndex > 0;
    //    }

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
        super.keyTyped(c, key);
        if (c == 'r')
        {
            for (GuiButton btn : buttons)
                btn.visible = !btn.visible;
        }
        if (key == Keyboard.KEY_LEFT)
        {
            for (GuiButton btn : buttons)
                ((GuiButtonTechNode) btn).bar.setMax(.4F);
        }
        if (key == Keyboard.KEY_RIGHT)
        {
            for (GuiButton btn : buttons)
                ((GuiButtonTechNode) btn).bar.setMax(.6F);
        }
    }

    protected void drawBackground()
    {
        //mc.renderEngine.bindTexture(Textures.TREE_BACKGROUND);
        //drawTexturedModalRect(xStart, 15, 0, 0, WIDTH, HEIGHT);
        drawRect(xStart, 15, xStart + WIDTH, 15 + HEIGHT, 0xF0AAAAAA);
    }

    protected void drawForeground()
    {
        drawTreeLines();
    }

    private void drawTreeLines()
    {

        for (GuiButtonTechNode btn : buttons)
        {
            for (int node : btn.getNode().getParentID())
            {
                RenderingHelper.draw2DLine(btn.getInX(), btn.getInY(), buttons[node].getOutX(), buttons[node].getOutY(), 2.5f, 0);
            }
            if (btn.getNode().getClass().equals(FakeNode.class))
            {
                RenderingHelper.draw2DLine(btn.getInX(), btn.getInY(), btn.getOutX(), btn.getOutY(), 2.5f, 0);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void drawOverlay(int mouseX, int mouseY)
    {
        for (GuiButtonTechNode btn : buttons)
        {
            if (btn.mousePressed(this.mc, mouseX, mouseY))
            {
                int w = Math.max(fontRendererObj.getStringWidth(btn.getNode().getName()) + 20, 100);
                int h = (int) (fontRendererObj.listFormattedStringToWidth(btn.getNode().getDescription(), w).size() * fontRendererObj.FONT_HEIGHT * 0.5 + fontRendererObj.FONT_HEIGHT + 30);
                if (mouseX < width / 2)
                {
                    mouseX -= w;
                }

                try
                {
                    RenderingHelper.drawRoundedRectangle(mouseX + 2, mouseY + 2, w, h, 7, 0xF08694E3);
                    fontRendererObj.drawString(btn.getNode().getName(), mouseX + 2 + 6, mouseY + 2 + 6, 0xFFFFFFFF, true);
                    drawHorizontalLine(mouseX + 7, mouseX + w - 7, mouseY + 10 + fontRendererObj.FONT_HEIGHT, 0xFFDDDDDD);

                    GL11.glPushMatrix();
                    GL11.glScaled(0.5, 0.5, 0);
                    GL11.glTranslated(mouseX, mouseY, 0);
                    fontRendererObj.drawSplitString(btn.getNode().getDescription(), mouseX + 14, mouseY + 17 + fontRendererObj.FONT_HEIGHT * 3, w * 2 - 10, 0xFFFFFFFF);
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
