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

package allout58.mods.techtree.config;

import allout58.mods.techtree.lib.ModInfo;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by James Hollowell on 1/1/2015.
 */
public class Config
{
    public static Config INSTANCE;

    public static final String CLIENT_CAT = "client";
    public static final String SERVER_CAT = "server";

    public Client client = new Client();
    public Server server = new Server();

    private final Configuration config;

    public class Client
    {
        public int colorProgressBarMain;
        public int colorProgressBarBackground;
        public int colorProgressBarBorder;

        public int colorBackground;

        public int colorBtnLocked1;
        public int colorBtnLocked2;
        public int colorBtnResearch1;
        public int colorBtnResearch2;
        public int colorBtnUnlocked1;
        public int colorBtnUnlocked2;
        public int colorBtnCompleted1;
        public int colorBtnCompleted2;
        public int colorBtnText;

        public int colorConnectors;

        public int colorOverlayBackground;
        public int colorOverlayText;
        public int colorOverlayOther;
    }

    public class Server
    {
        public int ticks;
    }

    public Config(Configuration config)
    {
        this.config = config;
        load();
    }

    public Configuration getConfig()
    {
        return config;
    }

    public void save()
    {

    }

    public void load()
    {
        config.getCategory(CLIENT_CAT).setComment("All colors are in ARGB hexdecimal format. If you don't know what that is, leave them ;)");

        client.colorProgressBarMain = config.getInt("colorProgressBarMain", CLIENT_CAT, 0xFF119911, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorProgressBarBackground = config.getInt("colorProgressBarBackground", CLIENT_CAT, 0xFF018901, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorProgressBarBorder = config.getInt("colorProgressBarBorder", CLIENT_CAT, 0xFF991111, Integer.MIN_VALUE, Integer.MAX_VALUE, "");

        client.colorBackground = config.getInt("colorBackground", CLIENT_CAT, 0xF0AAAAAA, Integer.MIN_VALUE, Integer.MAX_VALUE, "");

        client.colorBtnLocked1 = config.getInt("colorBtnLocked1", CLIENT_CAT, 0xFF777777, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnLocked2 = config.getInt("colorBtnLocked2", CLIENT_CAT, 0xFF333333, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnResearch1 = config.getInt("colorBtnResearch1", CLIENT_CAT, 0xFF9999FF, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnResearch2 = config.getInt("colorBtnResearch2", CLIENT_CAT, 0xFF6565a5, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnUnlocked1 = config.getInt("colorBtnUnlocked1", CLIENT_CAT, 0xFFAAAAAA, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnUnlocked2 = config.getInt("colorBtnUnlocked2", CLIENT_CAT, 0xFF656565, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnCompleted1 = config.getInt("colorBtnCompleted1", CLIENT_CAT, 0xFF2CC9B7, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnCompleted2 = config.getInt("colorBtnCompleted2", CLIENT_CAT, 0xFF3F998E, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorBtnText = config.getInt("colorBtnText", CLIENT_CAT, 0xFFFFFFFF, Integer.MIN_VALUE, Integer.MAX_VALUE, "");

        client.colorConnectors = config.getInt("colorConnectors", CLIENT_CAT, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "");

        client.colorOverlayBackground = config.getInt("colorOverlayBackground", CLIENT_CAT, 0xF08694E3, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorOverlayText = config.getInt("colorOverlayText", CLIENT_CAT, 0xFFFFFFFF, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        client.colorOverlayOther = config.getInt("colorOverlayOther", CLIENT_CAT, 0xFFDDDDDD, Integer.MIN_VALUE, Integer.MAX_VALUE, "");

        server.ticks = config.getInt("ticks", SERVER_CAT, 300, 10, Integer.MAX_VALUE, "Time between adding research points");

        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(ModInfo.MOD_ID))
        {
            load();
        }
    }
}
