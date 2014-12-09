package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by James Hollowell on 12/9/2014.
 */
public class GuiButtonTechNode extends GuiButton
{
    public enum ButtonMode
    {
        Locked,
        Researching,
        Unlocked
    }

    private ButtonMode mode = ButtonMode.Locked;
    private TechNode node;

    public GuiButtonTechNode(int id, int x, int y, int width, int height, TechNode node)
    {
        super(id, x, y, width, height, "");
        this.node = node;
    }

    public void setMode(ButtonMode mode)
    {
        this.mode = mode;
    }
}
