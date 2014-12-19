/******************************************************************************
 * The MIT License (MIT)                                                      *
 *                                                                            *
 * Copyright (c) 2014 allout58                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package allout58.mods.techtree.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

/**
 * Created by James Hollowell on 12/7/2014.
 */
//TODO This has to be registered with the GsonBuilder
//EXAMPLE:     gsonBuilder.registerTypeAdapter(TechNode.class, new TechNodeGSON());

public class TechNodeGSON
        implements JsonSerializer<TechNode>, JsonDeserializer<TechNode>
{
    @Override
    public TechNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        final JsonObject obj = json.getAsJsonObject();
        final int id = obj.get("id").getAsInt();
        final String name = obj.get("name").getAsString();
        final int science = obj.get("scienceRequired").getAsInt();
        final String description = obj.get("description").getAsString();

        final TechNode node = new TechNode(id);
        ItemStack[] items = new ItemStack[] { new ItemStack(Items.apple), new ItemStack(Items.arrow), new ItemStack(Items.bow) };
        node.setup(name, science, description, items);

        final JsonArray jsonParentArray = obj.getAsJsonArray("parents");
        for (int i = 0; i < jsonParentArray.size(); i++)
        {
            node.addParentNode(jsonParentArray.get(i).getAsInt());
        }

        return node;
    }

    @Override
    public JsonElement serialize(TechNode src, Type typeOfSrc, JsonSerializationContext context)
    {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("scienceRequired", src.getScienceRequired());
        jsonObject.addProperty("description", src.getDescription());

        final JsonArray jsonParents = new JsonArray();
        for (final INode parent : src.getParents())
        {
            if (parent instanceof TechNode)
            {
                final JsonPrimitive jsonParent = new JsonPrimitive(parent.getId());
                jsonParents.add(jsonParent);
            }
        }

        jsonObject.add("parents", jsonParents);

        return jsonObject;
    }
}
