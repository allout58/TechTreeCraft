package allout58.mods.techtree.client;

import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

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

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        //super.drawButton(mc, mouseX, mouseY);
        if (this.visible)
        {
            FontRenderer fontRenderer = mc.fontRenderer;
            boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

            GL11.glPushMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (mouseOver)
            {
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xFFFFF000);
            }
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF0000FF);

            fontRenderer.drawString(node.getName(), xPosition + 2, yPosition + 2, 0xFFFFFFFF, true);

            GL11.glPopMatrix();

            this.mouseDragged(mc, mouseX, mouseY);
        }
        /*if (visible) {
            boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            glColor4f(1, 1, 1, 1);
            mc.renderEngine.bindTexture(Textures.Gui_FoodBook);
            int u = 175;
            int v = 0;

            if (mouseOver) {
                v += 17;
            }

            if (previous) {
                u += 17;
            }

            GL11.glPushMatrix();

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1, 1, 1, 1);

            drawTexturedModalRect(xPosition, yPosition, u, v, width, height);

            GL11.glEnable(GL11.GL_LIGHTING);

            GL11.glPopMatrix();
        }*/
    }

    public void setMode(ButtonMode mode)
    {
        this.mode = mode;
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
