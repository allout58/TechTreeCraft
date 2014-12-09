package allout58.mods.techtree.util;

import allout58.mods.techtree.TechTreeMod;
import allout58.mods.techtree.lib.ModInfo;
import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by James Hollowell on 12/5/2014.
 */
public class VersionChecker implements Runnable
{
    public static final VersionChecker instance = new VersionChecker();

    private static final Marker VCMARKER = MarkerManager.getMarker("VersionChecker");
    private static boolean HasBeenNotified = false;

    private static final int CHECK_COUNT = 3;
    private boolean needsUpdate = false;
    private String changelog = "";
    private Version local = new Version(0, 0, 0, 0);
    private Version remote = null;

    class RemoteVersion
    {
        public int[] version = new int[] { 0, 0, 0, 0 };
        public String changelog = "";
    }

    //https://github.com/pahimar/Equivalent-Exchange-3/blob/1.6.4/src/main/java/com/pahimar/ee3/helper/VersionHelper.java
    @SubscribeEvent
    public void onClientConnectToServer(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer && event.world.isRemote)
        {
            if (!HasBeenNotified && needsUpdate)
            {
                EntityPlayer player = (EntityPlayer) event.entity;
                player.addChatComponentMessage(new ChatComponentText(ModInfo.MOD_NAME + " has updated! Latest version " + remote.toString()));
                player.addChatComponentMessage(new ChatComponentText("Changelog: " + changelog));
            }
        }
    }

    public void checkVersion()
    {
        local.readFromString(TechTreeMod.version);
        for (int i = 0; i < CHECK_COUNT; i++)
        {
            try
            {
                URL url = new URL(ModInfo.UPDATE_LOC + "version.json");
                URLConnection con = url.openConnection();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                Gson gson = new Gson();
                RemoteVersion v = gson.fromJson(isr, RemoteVersion.class);
                remote = new Version(v.version[0], v.version[1], v.version[2], v.version[3]);
                int comp = remote.compareTo(local);
                if (comp == 1)
                {
                    needsUpdate = true;
                    changelog = v.changelog;
                    return;
                }
                else
                {
                    return;
                }
            }
            catch (IOException e)
            {
                LogHelper.error("Error reading remote version (try %d/%d)", i + 1, CHECK_COUNT);
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie)
                {
                    e.printStackTrace();
                }
            }
        }
        LogHelper.error("BigFactories remote version check failed after %d attempts.", CHECK_COUNT);
    }

    private void logResult()
    {
        if (needsUpdate)
        {
            LogHelper.error("Needs update: local version-%s; remote version-%s", local.toString(), remote.toString());
        }
    }

    @Override
    public void run()
    {
        checkVersion();
        logResult();
    }

    public static void execute()
    {
        new Thread(instance).start();
    }
}
