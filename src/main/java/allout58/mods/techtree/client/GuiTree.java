package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by James Hollowell on 12/7/2014.
 */
public class GuiTree extends GuiScreen
{
    public static final int WIDTH = 175;
    public static final int HEIGHT = 228;

    public int xStart;
    TechNode node;

    public GuiTree(TechNode head)
    {
        super();
        node = head;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        xStart = (width - WIDTH) / 2;

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
