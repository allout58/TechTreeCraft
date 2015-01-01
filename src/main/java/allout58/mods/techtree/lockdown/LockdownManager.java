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

package allout58.mods.techtree.lockdown;

import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 12/29/2014.
 */
public class LockdownManager
{
    private static LockdownManager instance;
    private static Logger log = LogManager.getLogger();

    private Map<String, ArrayList<ItemStack>> data = new HashMap<String, ArrayList<ItemStack>>();

    public static LockdownManager getInstance()
    {
        if (instance == null)
            instance = new LockdownManager();
        return instance;
    }

    public void lockItem(ItemStack stack, String uuid)
    {
        if (data.get(uuid) == null)
            data.put(uuid, new ArrayList<ItemStack>());
        data.get(uuid).add(stack.copy());
    }

    public void unlockItem(ItemStack stack, String uuid)
    {
        ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
        if (data.get(uuid) == null)
            log.error("Tried unlocking an item from a player with no locked items");
        else
        {
            for (ItemStack s : data.get(uuid))
                if (s.getItem().equals(stack.getItem()) && s.getItemDamage() == stack.getItemDamage())
                    toRemove.add(s);
            data.get(uuid).removeAll(toRemove);
        }
    }

    public boolean canCraft(ItemStack stack, String uuid)
    {
        if (stack != null && data.get(uuid) != null)
            for (ItemStack s : data.get(uuid))
                if (s.getItem().equals(stack.getItem()) && s.getItemDamage() == stack.getItemDamage())
                    return false;
        return true;
    }
}
