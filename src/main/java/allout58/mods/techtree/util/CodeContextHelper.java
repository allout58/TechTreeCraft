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

package allout58.mods.techtree.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 12/27/2014.
 */
public class CodeContextHelper
{
    private static CodeContextHelper instance;

    private List<String> registerServer = new ArrayList<String>();

    public static CodeContextHelper getInstance()
    {
        if (instance == null)
            instance = new CodeContextHelper();
        return instance;
    }

    public void registerThreadAsServer(Thread thread)
    {
        registerThreadAsServer(thread.getName());
    }

    public void registerThreadAsServer(String thread)
    {
        registerServer.add(thread);
    }

    public Side getEffectiveSide()
    {
        Thread cur = Thread.currentThread();
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() || registerServer.contains(cur.getName()))
            return Side.SERVER;
        return Side.CLIENT;
    }
}
