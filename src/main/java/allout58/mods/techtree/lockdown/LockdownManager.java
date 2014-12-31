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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by James Hollowell on 12/29/2014.
 */
public class LockdownManager
{
    private static LockdownManager instance;
    private static Logger log = LogManager.getLogger();

    private Map<String, IEnableRecipe> changedRecipeLookup = new HashMap<String, IEnableRecipe>();

    public static LockdownManager getInstance()
    {
        if (instance == null)
            instance = new LockdownManager();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void makeRecipe(Item item, int meta) throws IllegalArgumentException
    {
        List craftingList = CraftingManager.getInstance().getRecipeList();
        IRecipe recipe = null;
        for (Object r : craftingList)
        {
            recipe = (IRecipe) r;
            if (recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem().equals(item) && (recipe.getRecipeOutput().getItemDamage() == meta || meta == -1))
                break;
            else
                recipe = null;
        }
        if (recipe == null)
            throw new IllegalArgumentException("Unable to find recipe with " + item.toString() + ":" + meta + " as an output.");
        craftingList.remove(recipe);
        if (recipe instanceof ShapedRecipes)
        {
            LockdownShapedRecipe r = new LockdownShapedRecipe((ShapedRecipes) recipe);
            craftingList.add(r);
            changedRecipeLookup.put(item.getUnlocalizedName() + "@" + meta, r);
        }
        else if (recipe instanceof ShapedOreRecipe)
        {
            LockdownShapedOreRecipe r = new LockdownShapedOreRecipe((ShapedOreRecipe) recipe);
            craftingList.add(r);
            changedRecipeLookup.put(item.getUnlocalizedName() + "@" + meta, r);
        }
        else
            log.error("Unrecognized recipe type, unable to override. " + recipe.getClass().toString());
    }

    public void lockItem(Item item, int meta, UUID uuid)
    {
        if (findRecipe(item, meta) == null)
            makeRecipe(item, meta);
        findRecipe(item, meta).disable(uuid);
    }

    public void lockItem(Item item, UUID uuid)
    {
        if (findRecipe(item, OreDictionary.WILDCARD_VALUE) == null)
            makeRecipe(item, OreDictionary.WILDCARD_VALUE);
        findRecipe(item, OreDictionary.WILDCARD_VALUE).enable(uuid);
    }

    public void unlockItem(Item item, int meta, UUID uuid)
    {
        if (findRecipe(item, meta) == null)
            throw new IllegalArgumentException("Unable to find the recipe to unlock.");
        changedRecipeLookup.get(item.getUnlocalizedName() + "@" + meta).enable(uuid);
    }

    public void unlockItem(Item item, UUID uuid)
    {
        if (findRecipe(item, OreDictionary.WILDCARD_VALUE) == null)
            throw new IllegalArgumentException("Unable to find the recipe to unlock.");
        changedRecipeLookup.get(item.getUnlocalizedName() + "@" + OreDictionary.WILDCARD_VALUE).enable(uuid);
    }

    private IEnableRecipe findRecipe(Item item, int meta)
    {
        return changedRecipeLookup.get(item.getUnlocalizedName() + "@" + meta);
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = event.craftMatrix.getStackInSlot(i);
            if (stack != null)
                event.player.dropItem(stack.getItem(), stack.getItemDamage());
            event.craftMatrix.setInventorySlotContents(i, null);
        }
        event.crafting.setItemDamage(-1);
        event.player.addChatMessage(new ChatComponentText("Sorry, you cant do that! " + FMLCommonHandler.instance().getEffectiveSide().toString()));
    }
}
