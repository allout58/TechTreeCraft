package allout58.mods.techtree.tree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

/**
 * Created by James Hollowell on 12/6/2014.
 */
public class TreeLoader
{
    public static Logger log = LogManager.getFormatterLogger("TechTreeMod-TreeLoader");

    public static TechNode readTree(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists()) return null;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TechNode.class, new TechNodeGSON());
        Gson gson = gsonBuilder.create();

        TechNode head = null;
        try
        {
            Reader reader = new FileReader(file);
            TechNode[] nodes = gson.fromJson(reader, TechNode[].class);

            for (int i = 0; i < nodes.length; i++)
            {
                if (nodes[i].getParentID().size() == 0)
                {
                    head = nodes[i];
                }
                else
                {
                    TechNode child = nodes[i];
                    for (int parentID : child.getParentID())
                    {
                        if (parentID == child.getId())
                            throw new RuntimeException("Tree node with a self-referencing parent: " + parentID);
                        TechNode parent = nodes[parentID];
                        child.addParentNode(parent);
                        parent.addChildNode(child);
                    }
                }
            }

        }
        catch (Exception e)
        {
            log.error("Error reading in tree from " + fileName, e);
        }
        return head;
    }
}
