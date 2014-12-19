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

import java.util.HashMap;

/**
 * Created by James Hollowell on 12/18/2014.
 */
public enum NodeMode
{
    Locked(0),
    Unlocked(1),
    Researching(2),
    Completed(3);

    private static final HashMap<Integer, NodeMode> lookup = new HashMap<Integer, NodeMode>();

    private int order;

    private NodeMode(int order)
    {
        this.order = order;
    }

    public static NodeMode next(NodeMode mode)
    {
        return NodeMode.getByID((mode.order + 1) % values().length);
    }

    private static NodeMode getByID(int id)
    {
        return lookup.get(id);
    }

    static
    {
        for (NodeMode mode : values())
        {
            lookup.put(mode.order, mode);
        }
    }
}
