/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2014 allout58                                                     *
 *                                                                                 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 *                                                                                 *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 *                                                                                 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 ***********************************************************************************/

package allout58.mods.techtree.lockdown;

import allout58.mods.techtree.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by James Hollowell on 12/29/2014.
 */
public class LockdownShapedOreRecipe extends ShapedOreRecipe
        implements IEnableRecipe
{
    private boolean isDisabled = false;

    public LockdownShapedOreRecipe(ShapedOreRecipe recipe)
    {
        //pass in a fake recipe to make it happy, then override with the correct values
        super(recipe.getRecipeOutput(), "x x", " x ", "x x", 'x', Blocks.bedrock);
        try
        {
            Class<?> c = recipe.getClass();
            Field inputField = c.getDeclaredField("input");
            Field widthField = c.getDeclaredField("width");
            Field heightField = c.getDeclaredField("height");
            Field mirroredField = c.getDeclaredField("mirrored");

            inputField.setAccessible(true);
            widthField.setAccessible(true);
            heightField.setAccessible(true);
            mirroredField.setAccessible(true);

            Object[] input = recipe.getInput();
            int width = widthField.getInt(recipe);
            int height = heightField.getInt(recipe);
            boolean mirrored = mirroredField.getBoolean(recipe);

            inputField.set(this, input);
            widthField.setInt(this, width);
            heightField.setInt(this, height);
            this.setMirrored(mirrored);
        }
        catch (Exception e)
        {
            LogHelper.logger.error("Error changing shaped ore recipe", e);
        }

    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world)
    {
        return !isDisabled && super.matches(crafting, world);
    }

    @Override
    public void enable(UUID uuid)
    {
        isDisabled = false;
    }

    @Override
    public void disable(UUID uuid)
    {
        isDisabled = true;
    }
}
