package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.FakeNode;
import allout58.mods.techtree.tree.INode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

/**
 * Created by James Hollowell on 12/9/2014.
 */
public class GuiButtonTechNode extends GuiButton
{
    public enum ButtonMode
    {
        Locked(0),
        Unlocked(1),
        Researching(2),
        Completed(3);

        private static final HashMap<Integer, ButtonMode> lookup = new HashMap<Integer, ButtonMode>();

        private int order;

        private ButtonMode(int order)
        {
            this.order = order;
        }

        public static ButtonMode next(ButtonMode mode)
        {
            return ButtonMode.getByID((mode.order + 1) % values().length);
        }

        private static ButtonMode getByID(int id)
        {
            return lookup.get(id);
        }

        static
        {
            for (ButtonMode mode : values())
            {
                lookup.put(mode.order, mode);
            }
        }
    }

    private ButtonMode mode = ButtonMode.Locked;
    private INode node;

    public GuiButtonTechNode(int id, int x, int y, int width, int height, INode node)
    {
        super(id, x, y, width, height, "");

        if (node instanceof FakeNode)
        {
            this.visible = this.enabled = false;
            this.height = 2;
        }

        this.node = node;

    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontRenderer = mc.fontRenderer;
            boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

            GL11.glPushMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            switch (mode)
            {
                case Locked:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF777777, 0xFF333333);
                    break;
                case Unlocked:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFFAAAAAA, 0xFF656565);
                    break;
                case Researching:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF9999FF, 0xFF6565a5);
                    break;
                case Completed:
                    drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF2CC9B7, 0xFF3F998E);
                    break;
                default:
                    System.err.println("ERROR! Invalid button state! o.O" + mode);
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
            fontRenderer.drawString(mode.name(), xPosition + 2, yPosition + 10 + fontRenderer.FONT_HEIGHT, 0xFFFFFFFF, false);
            fontRenderer.drawSplitString(node.getDescription(), xPosition + 4, yPosition + 18 + fontRenderer.FONT_HEIGHT * 2, width * 2, 0xFFFFFFFF);

            GL11.glPopMatrix();

            GL11.glPopMatrix();

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    public void setMode(ButtonMode mode)
    {
        this.mode = mode;
    }

    public void nextMode()
    {
        this.mode = ButtonMode.next(this.mode);
    }

    public INode getNode()
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
