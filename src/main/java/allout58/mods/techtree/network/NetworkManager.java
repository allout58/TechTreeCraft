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

package allout58.mods.techtree.network;

import allout58.mods.techtree.lib.ModInfo;
import allout58.mods.techtree.network.message.ChangeNodeMode;
import allout58.mods.techtree.network.message.RequestAll;
import allout58.mods.techtree.network.message.RequestMode;
import allout58.mods.techtree.network.message.RequestResearch;
import allout58.mods.techtree.network.message.SendResearch;
import allout58.mods.techtree.network.message.SendTree;
import allout58.mods.techtree.network.message.UpdateNodeMode;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by James Hollowell on 12/17/2014.
 */
public class NetworkManager
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);

    private static int discriminator = 0;

    public static void init()
    {
        INSTANCE.registerMessage(RequestResearch.Handler.class, RequestResearch.class, discriminator++, Side.SERVER);
        INSTANCE.registerMessage(SendResearch.Handler.class, SendResearch.class, discriminator++, Side.CLIENT);

        INSTANCE.registerMessage(RequestMode.Handler.class, RequestMode.class, discriminator++, Side.SERVER);
        INSTANCE.registerMessage(UpdateNodeMode.Handler.class, UpdateNodeMode.class, discriminator++, Side.CLIENT);

        INSTANCE.registerMessage(ChangeNodeMode.Handler.class, ChangeNodeMode.class, discriminator++, Side.SERVER);

        INSTANCE.registerMessage(SendTree.Handler.class, SendTree.class, discriminator++, Side.CLIENT);
        INSTANCE.registerMessage(RequestAll.Handler.class, RequestAll.class, discriminator++, Side.SERVER);
    }
}
