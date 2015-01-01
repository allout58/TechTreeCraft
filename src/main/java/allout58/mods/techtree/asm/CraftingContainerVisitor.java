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

package allout58.mods.techtree.asm;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by James Hollowell on 12/31/2014.
 */
public class CraftingContainerVisitor extends ClassVisitor
{
    public static boolean craftingHookSuccess = false;
    private final MethodMatcher matcher;

    public CraftingContainerVisitor(String name, ClassVisitor cv)
    {
        super(Opcodes.ASM4, cv);
        matcher = new MethodMatcher(name, "(Lnet/minecraft/inventory/IInventory;)V", "onCraftMatrixChanged", "func_75130_a");
    }

    private static class HookMethodVisitor extends MethodVisitor
    {

        public HookMethodVisitor(MethodVisitor mv)
        {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode()
        {
            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0); //this
            mv.visitFieldInsn(Opcodes.GETFIELD, ASMHelper.getMappedName("net/minecraft/inventory/ContainerWorkbench"), ASMHelper.getName("craftResult", "field_75160_f"), "Lnet/minecraft/inventory/IInventory;");
            mv.visitVarInsn(Opcodes.ALOAD, 0); //this
            mv.visitFieldInsn(Opcodes.GETFIELD, ASMHelper.getMappedName("net/minecraft/inventory/ContainerWorkbench"), ASMHelper.getName("craftMatrix", "field_75162_e"), "Lnet/minecraft/inventory/InventoryCrafting;");
            mv.visitVarInsn(Opcodes.ALOAD, 0); //this
            mv.visitFieldInsn(Opcodes.GETFIELD, ASMHelper.getMappedName("net/minecraft/inventory/ContainerWorkbench"), ASMHelper.getName("worldObj", "field_75161_g"), "Lnet/minecraft/world/World;");
            mv.visitVarInsn(Opcodes.ALOAD, 0); //this
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMHelper.getMappedName("net/minecraft/inventory/ContainerWorkbench"), ASMHelper.getName("getSlot", "func_75139_a"), "(I)Lnet/minecraft/inventory/Slot;", false);
            mv.visitTypeInsn(Opcodes.CHECKCAST, ASMHelper.getMappedName("net/minecraft/inventory/SlotCrafting"));
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "allout58/mods/techtree/asm/CraftingHook", "craftingChangedHook", "(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;Lnet/minecraft/inventory/SlotCrafting;)V", false);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitEnd();
            craftingHookSuccess = true;
            FMLRelaunchLog.info("[TechTreeMod] onCraftingMatrixChanged patch (2/2) applied.");
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
        return matcher.match(name, desc) ? new HookMethodVisitor(parent) : parent;
    }
}
