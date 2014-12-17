package allout58.mods.techtree.client;

/**
 * Created by James Hollowell on 12/16/2014.
 */
public abstract class GuiElement
{
    /**
     * Element width in pixels
     */
    protected int width;
    /**
     * Elements height in pixels
     */
    protected int height;
    /**
     * The x position of this control.
     */
    protected int xPosition;
    /**
     * The y position of this control.
     */
    protected int yPosition;
    /**
     * True if this control is enabled, false to disable.
     */
    protected boolean enabled;
    /**
     * Hides the button completely if false.
     */
    protected boolean visible;

    public GuiElement(int width, int height, int xPosition, int yPosition)
    {
        this.width = width;
        this.height = height;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public abstract void doRender();

    /*
        Getters and setters
     */
    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getxPosition()
    {
        return xPosition;
    }

    public void setxPosition(int xPosition)
    {
        this.xPosition = xPosition;
    }

    public int getyPosition()
    {
        return yPosition;
    }

    public void setyPosition(int yPosition)
    {
        this.yPosition = yPosition;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
