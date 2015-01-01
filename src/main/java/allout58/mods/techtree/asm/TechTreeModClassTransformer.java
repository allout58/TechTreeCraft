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

import com.google.common.base.Preconditions;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Created by James Hollowell on 12/31/2014.
 */
public class TechTreeModClassTransformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (bytes == null) return null;

        if ("net.minecraft.inventory.ContainerWorkbench".equals(transformedName))
        {
            FMLRelaunchLog.info("[TechTreeMod] Trying to patch ContainerWorkbench.onCraftMatrixChanged (class : %s)", name);
            return apply(bytes, name, 0);
        }
        else if ("net.minecraft.inventory.ContainerPlayer".equals(transformedName))
        {
            FMLRelaunchLog.info("[TechTreeMod] Trying to patch ContainerPlayer.onCraftMatrixChanged (class : %s)", name);
            return apply(bytes, name, 1);
        }

        return bytes;
    }

    private byte[] apply(byte[] bytes, String name, int id)
    {
        Preconditions.checkNotNull(bytes);
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = null;

        switch (id)
        {
            case 0:
                visitor = new CraftingContainerVisitor(name, writer);
                break;
            case 1:
                visitor = new CraftingPlayerVisitor(name, writer);
                break;
        }
        try
        {
            if (visitor != null)
                reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        catch (Exception e)
        {
            FMLRelaunchLog.severe("Error transforming %s: %s", name, e);
            return bytes;
        }
    }
}
