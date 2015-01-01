/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2015 allout58                                                     *
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

package allout58.mods.techtree.asm;

import allout58.mods.techtree.lockdown.LockdownManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Created by James Hollowell on 12/31/2014.
 */
public class CraftingHook
{
    private static final Logger log = LogManager.getLogger();

    public static void craftingChangedHook(IInventory output, InventoryCrafting matrix, @SuppressWarnings("unused") World world, SlotCrafting crafting)
    {
        try
        {
            Field field = SlotCrafting.class.getDeclaredField(ASMHelper.getName("thePlayer", "field_75238_b"));
            field.setAccessible(true);
            EntityPlayer player = (EntityPlayer) field.get(crafting);
            craftingChangedHook_player(output, matrix, player);
        }
        catch (Exception e)
        {
            log.error("Error getting the player from SlotCrafting", e);
        }
    }

    public static void craftingChangedHook_player(IInventory output, InventoryCrafting matrix, EntityPlayer player)
    {
        ItemStack itemStack = CraftingManager.getInstance().findMatchingRecipe(matrix, player.worldObj);
        boolean canCraft = LockdownManager.getInstance().canCraft(itemStack, player.getUniqueID().toString());
        if (canCraft)
        {
            output.setInventorySlotContents(0, itemStack);
        }
        else if (player.worldObj.isRemote)
        {
            if (!itemStack.hasTagCompound())
                itemStack.setTagCompound(new NBTTagCompound());
            NBTTagList list = new NBTTagList();
            list.appendTag(new NBTTagString("This item needs to be"));
            list.appendTag(new NBTTagString("researched to be accessed"));
            NBTTagCompound lore = new NBTTagCompound();
            lore.setTag("Lore", list);
            itemStack.getTagCompound().setTag("display", lore);
            output.setInventorySlotContents(0, itemStack);
        }
        else
            output.setInventorySlotContents(0, null);
    }
}
