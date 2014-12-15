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
import net.minecraft.item.Item;

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
        Item[] items = new Item[] { Items.apple, Items.arrow, Items.bow };
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
