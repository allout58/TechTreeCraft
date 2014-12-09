package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.TechNode;
import allout58.mods.techtree.tree.TechTree;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by James Hollowell on 12/7/2014.
 */
public class GuiTree extends GuiScreen
{
    public static final int WIDTH = 90;
    public static final int HEIGHT = 228;

    public static final int MIN_NODE_WIDTH = 40;
    public static final int MIN_NODE_HEIGHT = 20;

    public static final int MAX_NODE_WIDTH = 60;
    public static final int MAX_NODE_HEIGHT = 40;

    public static final int PAD_X = 10;
    public static final int PAD_Y = 10;

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

        for (HashSet<TechNode> list : tree.getList())
        {
            int treeWidth = list.size();
            int nodeHeight = (HEIGHT - PAD_Y * treeWidth) / treeWidth;
            nodeHeight = Math.max(nodeHeight, MIN_NODE_HEIGHT);
            nodeHeight = Math.min(nodeHeight, MAX_NODE_HEIGHT);

            List<Integer> yCoords = new ArrayList<Integer>(treeWidth);

            for (int i = 0; i < treeWidth; i++)
                yCoords.add(i, nodeHeight * i + PAD_Y * (i + 1) + 20);

            Iterator<TechNode> it = list.iterator();
            for (int i = 0; i < treeWidth; i++)
            {
                TechNode node = it.next();
                GuiButtonTechNode btn = new GuiButtonTechNode(node.getId(), xCoords.get(node.getDepth() - 1), yCoords.get(i), nodeWidth, nodeHeight, node);

                buttons[node.getId()] = btn;
                buttonList.add(btn);
            }
        }

        //buttonList.add(next = new GuiButtonPageChange(BOOK_BTN_NEXT, bookXStart + WIDTH - 26, 210, false));
        //buttonList.add(prev = new GuiButtonPageChange(BOOK_BTN_PREV, bookXStart + 10, 210, true));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        //        switch (button.id)
        //        {
        //            case BOOK_BTN_NEXT:
        //                pageIndex++;
        //                break;
        //            case BOOK_BTN_PREV:
        //                --pageIndex;
        //                break;
        //        }
        //        updateButtonState();
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
    }

    protected void drawBackground()
    {
        mc.renderEngine.bindTexture(Textures.TREE_BACKGROUND);
        drawTexturedModalRect(xStart, 5, 0, 0, WIDTH, HEIGHT);
    }

    protected void drawForeground()
    {

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
