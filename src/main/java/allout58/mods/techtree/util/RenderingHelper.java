package allout58.mods.techtree.util;

import org.lwjgl.opengl.GL11;

/**
 * Created by James Hollowell on 12/11/2014.
 */
public class RenderingHelper
{
    public static void draw2DLine(int x1, int y1, int x2, int y2, float width, int colorRGB)
    {
        float red = (float) (colorRGB >> 16 & 255) / 255.0F;
        float blue = (float) (colorRGB >> 8 & 255) / 255.0F;
        float green = (float) (colorRGB & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);

        GL11.glPushMatrix();
        GL11.glColor3f(red, green, blue);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x1, y1, 0.0D);
        GL11.glVertex3d(x2, y2, 0.0D);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

    }
}
