/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2014 allout58                                                     *
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

package allout58.mods.techtree.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

//Taken from OpenModsLib
public class MethodMatcher
{
    private final String clsName;
    private final String description;
    private final String srgName;
    private final String mcpName;

    public MethodMatcher(String clsName, String description, String mcpName, String srgName)
    {
        this.clsName = clsName;
        this.description = description;
        this.srgName = srgName;
        this.mcpName = mcpName;
    }

    public boolean match(String methodName, String methodDesc)
    {
        if (!methodDesc.equals(description)) return false;
        if (methodName.equals(mcpName)) return true;
        if (!ASMHelper.useSrgNames()) return false;
        String mapped = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(clsName, methodName, methodDesc);
        return mapped.equals(srgName);
    }

    @Override
    public String toString()
    {
        return String.format("Matcher: %s.[%s,%s] %s", clsName, srgName, mcpName, description);
    }

}

