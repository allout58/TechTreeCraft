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

package allout58.mods.techtree.client;

import allout58.mods.techtree.client.elements.GuiAdvTextField;
import allout58.mods.techtree.config.Config;
import allout58.mods.techtree.tree.TechNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by James Hollowell on 1/21/2015.
 */
public class GuiNodeEdit extends GuiScreen
{
    private static final Logger log = LogManager.getLogger();

    //TODO Better locked item editing
    protected int renderWidth = 300;
    protected int renderHeight = 240;

    //Editing Elements
    protected GuiButton doneButton;
    protected GuiAdvTextField nameField;
    protected GuiAdvTextField scienceField;
    protected GuiAdvTextField descriptionField;
    protected GuiAdvTextField itemNameField;
    protected GuiAdvTextField itemMetaField;
    protected List<ItemStack> editItems = new ArrayList<ItemStack>();
    protected List<String> findItems = new ArrayList<String>();

    protected List<GuiAdvTextField> fieldList = new ArrayList<GuiAdvTextField>();

    protected TechNode editingNode = null;
    protected GuiScreen parent = null;
    private GuiButton cancelButton;

    public GuiNodeEdit(TechNode editingNode, GuiScreen parent)
    {
        super();
        this.parent = parent;
        this.editingNode = editingNode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        int centerX = width / 2;
        renderWidth = width - GuiTree.X_START * 2;
        renderHeight = height - GuiTree.Y_START * 2;

        doneButton = new GuiButton(0, (width - GuiTree.X_START) / 2 - 25, height - GuiTree.Y_START - 25, 40, 20, "Save");
        doneButton.enabled = true;
        cancelButton = new GuiButton(1, (width - GuiTree.X_START) / 2 + 25, height - GuiTree.Y_START - 25, 40, 20, "Cancel");
        cancelButton.enabled = true;
        buttonList.add(doneButton);
        buttonList.add(cancelButton);

        nameField = new GuiAdvTextField(fontRendererObj, centerX - 50, GuiTree.Y_START + 20, 100, 13, "Name:");
        scienceField = new GuiAdvTextField(fontRendererObj, centerX - 50, GuiTree.Y_START + 40, 100, 13, "Required Science:");
        scienceField.registerCallback((field) ->
            {
                try
                {
                    int i = Integer.parseInt(field.getText());
                    return i > 0 ? "" : "Number must be > 0";
                }
                catch (NumberFormatException e)
                {
                    return "Input must be a number";
                }
            });
        descriptionField = new GuiAdvTextField(fontRendererObj, centerX - 50, GuiTree.Y_START + 60, 100, 13, "Description:");
        itemNameField = new GuiAdvTextField(fontRendererObj, centerX - 50, GuiTree.Y_START + 80, 100, 13, "Find Item:");
        itemNameField.registerCallback((field) ->
        {
            findItems.clear();
            for (Object search : Item.itemRegistry.getKeys())
            {
                if (((String) search).startsWith(field.getText()))
                    findItems.add((String) search);
            }
            if (!findItems.isEmpty())
                field.setText(findItems.get(0));
            return null;
        });
        //TODO Use callback for for auto-complete?
        itemMetaField = new GuiAdvTextField(fontRendererObj, centerX - 50, GuiTree.Y_START + 100, 100, 13, "Item Meta:");
        itemMetaField.registerCallback((field) ->
        {
            try
            {
                int i = Integer.parseInt(field.getText());
                return i >= 0 ? "" : "Number must be >= 0";
            }
            catch (NumberFormatException e)
            {
                return "Input must be a number";
            }
        });

        fieldList.clear();
        fieldList.add(nameField);
        fieldList.add(scienceField);
        fieldList.add(descriptionField);
        fieldList.add(itemNameField);
        fieldList.add(itemMetaField);

        for (GuiTextField field : fieldList)
        {
            field.setCanLoseFocus(true);
            field.setEnabled(true);
            field.setEnableBackgroundDrawing(true);
            field.setMaxStringLength(40);
            field.setText("");
            field.setVisible(true);
        }
        descriptionField.setMaxStringLength(1000);

        nameField.setText(editingNode.getName());
        descriptionField.setText(editingNode.getDescription());
        scienceField.setText(String.valueOf(editingNode.getScienceRequired()));

        editItems.clear();
        editItems.addAll(Arrays.asList(editingNode.getLockedItems()));
    }

    @Override
    public void mouseClicked(int btn, int x, int y)
    {
        super.mouseClicked(btn, x, y);
        for (GuiTextField field : fieldList)
            field.mouseClicked(btn, x, y);
    }

    @Override
    protected void keyTyped(char c, int key)
    {
        if (key == Keyboard.KEY_ESCAPE)
            exit();
        if (key == Keyboard.KEY_TAB && itemNameField.isFocused() && !findItems.isEmpty())
        {
            itemNameField.setText(findItems.get(0));
        }
        if (key == Keyboard.KEY_RETURN && (itemNameField.isFocused() || itemMetaField.isFocused()))
        {
            Item it = (Item) Item.itemRegistry.getObject(itemNameField.getText());
            if (it == null)
            {
                //Make a mess nicely
                log.error("Bad item name");
            }
            else
            {
                if (!itemMetaField.hasProblems())
                {
                    int meta = Integer.parseInt(itemMetaField.getText());
                    editItems.add(new ItemStack(it, 1, meta));
                }
            }
        }
        for (GuiTextField field : fieldList)
        {
            field.textboxKeyTyped(c, key);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                if (scienceField.hasProblems())
                    log.info("Error with fields, can't complete.");
                else
                {
                    editingNode.setup(nameField.getText(), Integer.parseInt(scienceField.getText()), descriptionField.getText(), editItems.toArray(new ItemStack[editItems.size()]));
                    exit();
                }
                break;
            case 1:
                exit();
                break;
            default:
                log.error("Unknown button: " + button.id);
        }
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials)
    {
        drawBackground();

        for (GuiTextField field : fieldList)
            field.drawTextBox();

        super.drawScreen(mouseX, mouseY, renderPartials);

        for (int i = 0; i < editItems.size(); i++)
        {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawRect(GuiTree.X_START + 34 + 18 * i, GuiTree.Y_START + 120, GuiTree.X_START + 50 + 18 * i, GuiTree.Y_START + 136, 0xFFB0B0B0);
            itemRender.renderItemIntoGUI(fontRendererObj, Minecraft.getMinecraft().renderEngine, editItems.get(i), GuiTree.X_START + 34 + 18 * i, GuiTree.Y_START + 120);
        }
    }

    protected void drawBackground()
    {
        drawRect(GuiTree.X_START, GuiTree.Y_START, GuiTree.X_START + renderWidth, GuiTree.Y_START + renderHeight, Config.INSTANCE.client.colorBackground);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void exit()
    {
        this.mc.displayGuiScreen(parent);
    }
}
