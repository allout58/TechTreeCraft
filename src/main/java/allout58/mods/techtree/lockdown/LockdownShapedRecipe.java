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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Created by James Hollowell on 12/29/2014.
 */
public class LockdownShapedRecipe extends ShapedRecipes implements IEnableRecipe
{
    private boolean isDisabled = false;

    public LockdownShapedRecipe(ShapedRecipes recipe)
    {
        super(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
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
