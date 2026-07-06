/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_276
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 */
package sweetie.leonware.api.utils.render;

import lombok.Generated;
import net.minecraft.class_276;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class StencilUtil
implements QuickImports {
    public static void push() {
        class_276 framebuffer = mc.method_1522();
        if (framebuffer.field_1474 > -1) {
            mc.method_1522().method_1235(false);
            EXTFramebufferObject.glDeleteRenderbuffersEXT((int)framebuffer.field_1474);
            int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencilDepthBufferID);
            EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)mc.method_22683().method_4480(), (int)mc.method_22683().method_4507());
            EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencilDepthBufferID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencilDepthBufferID);
            framebuffer.field_1474 = -1;
        }
        GL11.glStencilMask((int)255);
        GL11.glClear((int)1024);
        GL11.glEnable((int)2960);
        GL11.glStencilFunc((int)519, (int)1, (int)1);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glDisable((int)2929);
        GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
    }

    public static void read(int ref) {
        GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GL11.glStencilFunc((int)514, (int)ref, (int)1);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
    }

    public static void pop() {
        GL11.glDisable((int)2960);
        GL11.glEnable((int)2929);
    }

    @Generated
    private StencilUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

