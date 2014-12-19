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

package allout58.mods.techtree.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 12/17/2014.
 */
@SideOnly(Side.CLIENT)
public class ResearchClient
{
    private static ResearchClient INSTANCE;
    /**
     * Client's UUID
     */
    private String clientID;
    private Map<Integer, Integer> researchList = new HashMap<Integer, Integer>();

    public ResearchClient(String clientID)
    {
        this.clientID = clientID;
    }

    public static ResearchClient getInstance() throws IllegalAccessException
    {
        if (INSTANCE == null)
            throw new IllegalAccessException("Tried to get the ResearchClient instance with no uuid and no previous instance");
        return INSTANCE;
    }

    public static ResearchClient getInstance(String uuid)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ResearchClient(uuid);
            return INSTANCE;
        }
        if (!INSTANCE.clientID.equals(uuid))
            throw new IllegalArgumentException("Tried to get the ResearchClient instance with a different UUID than is already set.");
        return INSTANCE;
    }

    public void setResearch(int nodeID, int newVal)
    {
        researchList.put(nodeID, newVal);
    }

    public int getResearch(int nodeID)
    {
        return researchList.get(nodeID);
    }

    public boolean isUpdated()
    {
        return researchList.size() != 0;
    }
}
