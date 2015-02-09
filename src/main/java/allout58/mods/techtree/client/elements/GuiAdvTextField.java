/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 allout58
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package allout58.mods.techtree.client.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 1/18/2015.
 */
public class GuiAdvTextField extends GuiTextField
{
    private List<IntegrityCallback> integrityCallbacks = new ArrayList<IntegrityCallback>(1);
    private List<String> errors = new ArrayList<String>();
    private FontRenderer fontRenderer;
    private String label;

    public GuiAdvTextField(FontRenderer fontRenderer, int x, int y, int w, int h, String label)
    {
        super(fontRenderer, x, y, w, h);
        this.fontRenderer = fontRenderer;
        this.label = label;
    }

    public void registerCallback(IntegrityCallback c)
    {
        integrityCallbacks.add(c);
    }

    @Override
    public void drawTextBox()
    {
        super.drawTextBox();
        if (getVisible())
        {
            if (errors.size() > 0)
                for (int i = 0; i < errors.size(); i++)
                    fontRenderer.drawString("-" + errors.get(i), xPosition + width + 3, yPosition + (fontRenderer.FONT_HEIGHT + 2) * i, 0xFFF01010, false);
            int labelWidth = fontRenderer.getStringWidth(label) + 1;
            fontRenderer.drawString(label, xPosition - labelWidth, yPosition, 0xFFFFFFFF, false);
        }
    }

    @Override
    public void mouseClicked(int btn, int x, int y)
    {
        boolean pre = isFocused();
        super.mouseClicked(btn, x, y);
        if (pre && !isFocused())
            doCallbacks();
    }

    @Override
    public boolean textboxKeyTyped(char c, int k)
    {
        boolean ret = super.textboxKeyTyped(c, k);
        if (ret)
            doCallbacks();
        return ret;
    }

    private void doCallbacks()
    {
        errors.clear();
        for (IntegrityCallback c : integrityCallbacks)
        {
            String s = c.call(this);
            if (!(s == null || s.trim().isEmpty()))
                errors.add(s);
        }
    }

    public boolean hasProblems()
    {
        return !errors.isEmpty();
    }

    public interface IntegrityCallback
    {
        String call(GuiAdvTextField field);
    }
}
