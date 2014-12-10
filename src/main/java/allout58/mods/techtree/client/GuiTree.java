package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
        buttons = new GuiButtonTechNode[tree.getNodes().size()];
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

        for (HashSet<TechNode> list : tree.getList())
        {
            int treeWidth = list.size();

            List<Integer> yCoords = new ArrayList<Integer>(treeWidth);

            for (int i = 0; i < treeWidth; i++)
                yCoords.add(i, nodeHeight * i + PAD_Y * (i + 1));

            int yStart = (height - HEIGHT) / 2 + ((tree.getMaxWidth() - treeWidth - 1) * nodeHeight + (treeWidth - tree.getMaxWidth()) * PAD_Y) / 2 + 15;

            Iterator<TechNode> it = list.iterator();
            for (int i = 0; i < treeWidth; i++)
            {
                TechNode node = it.next();
                GuiButtonTechNode btn = new GuiButtonTechNode(node.getId(), xCoords.get(node.getDepth() - 1), yCoords.get(i) + yStart, nodeWidth, nodeHeight, node);

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
    }

    protected void drawBackground()
    {
        //mc.renderEngine.bindTexture(Textures.TREE_BACKGROUND);
        //drawTexturedModalRect(xStart, 15, 0, 0, WIDTH, HEIGHT);
        drawRect(xStart, 15, xStart + WIDTH, 15 + HEIGHT, 0xF0999999);
    }

    protected void drawForeground()
    {
        drawTreeLines();
    }

    private void drawTreeLines()
    {
        //        GL11.glPushMatrix();
        //        GL11.glColor3f(1, 0, 0);
        //
        //        GL11.glBegin(GL11.GL_LINE_STRIP);
        //        GL11.glVertex3d(xStart, 15, 0.0D);
        //        GL11.glVertex3d(xStart + WIDTH, 15 + HEIGHT, 0.0D);
        //        GL11.glEnd();
        //        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2.5f);

        for (GuiButtonTechNode btn : buttons)
        {
            for (int node : btn.getNode().getParentID())
            {
                GL11.glPushMatrix();
                GL11.glColor3f(.81f, 0.01F, 0);
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(btn.getInX(), btn.getInY(), 0.0D);
                GL11.glVertex3d(buttons[node].getOutX(), buttons[node].getOutY(), 0.0D);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    protected void drawStartScreen()
    {
        //fontRendererObj.drawString(StatCollector.translateToLocal("gui.FoodBook.Title"), bookXStart + 45, 20, 0x000000);
        //fontRendererObj.drawSplitString(StatCollector.translateToLocal("gui.FoodBook.MainDesc"), bookXStart + 20, 60, WIDTH - 40, 0x000000);
    }

    protected void drawPages()
    {
        //fontRendererObj.drawString((pageIndex + 1) + "/" + (pages.size() + 1), bookXStart + 82, 215, 0x000000);
    }

}
