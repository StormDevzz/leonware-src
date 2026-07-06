/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_276
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4587
 *  net.minecraft.class_5944
 *  net.minecraft.class_6367
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package sweetie.leonware.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_276;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_6367;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.FramebufferResizeEvent;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import sweetie.leonware.client.features.modules.render.InterfaceModule;

public final class KawaseBlurProgram
implements QuickImports {
    private static final class_10156 upShader = new class_10156(FileUtil.getShader("post/blur/upscale"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 downShader = new class_10156(FileUtil.getShader("post/blur/downscale"), class_290.field_1592, class_10149.field_53930);
    private static final int blurPasses = InterfaceModule.getPasses();
    public static final List<class_6367> fbos = new ArrayList<class_6367>();
    private static boolean init = false;
    private static final FrameLimiter f = new FrameLimiter(false);

    public static void load() {
        if (!init) {
            for (int i = 0; i <= InterfaceModule.getPasses(); ++i) {
                fbos.add(KawaseBlurProgram.createFbo());
            }
            init = true;
        }
        FramebufferResizeEvent.getInstance().subscribe(new Listener<FramebufferResizeEvent>(event -> KawaseBlurProgram.recreate()));
    }

    public static void recreate() {
        fbos.forEach(class_276::method_1238);
        fbos.clear();
        for (int i = 0; i <= InterfaceModule.getPasses(); ++i) {
            fbos.add(KawaseBlurProgram.createFbo());
        }
    }

    public static void render(class_4587 matrixStack) {
        if (InterfaceModule.getGlassy() != 1.0f) {
            f.execute(40, () -> {
                int i;
                int actualPasses = Math.max(fbos.size() - 1, 1);
                KawaseBlurProgram.applyBlurPass(matrixStack, downShader, mc.method_1522(), (class_276)fbos.getFirst(), 0);
                for (i = 0; i < actualPasses; ++i) {
                    KawaseBlurProgram.applyBlurPass(matrixStack, downShader, (class_276)fbos.get(i), (class_276)fbos.get(i + 1), i + 1);
                }
                for (i = actualPasses; i > 0; --i) {
                    KawaseBlurProgram.applyBlurPass(matrixStack, upShader, (class_276)fbos.get(i), (class_276)fbos.get(i - 1), i);
                }
                mc.method_1522().method_1235(false);
            });
        }
    }

    private static void applyBlurPass(class_4587 matrixStack, class_10156 shaderKey, class_276 source, class_276 destination, int pass) {
        destination.method_1235(false);
        RenderSystem.setShaderTexture((int)0, (int)source.method_30277());
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
        shader.method_34582("uHalfTexelSize").method_1255(0.5f / (float)source.field_1482, 0.5f / (float)source.field_1481);
        shader.method_34582("uOffset").method_1251(InterfaceModule.getOffset() * ((float)pass / (float)blurPasses));
        KawaseBlurProgram.drawFullQuad(matrixStack.method_23760().method_23761());
        destination.method_1240();
    }

    private static void drawFullQuad(Matrix4f matrix4f) {
        float width = mc.method_22683().method_4486();
        float height = mc.method_22683().method_4502();
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        builder.method_22918(matrix4f, 0.0f, 0.0f, 0.0f);
        builder.method_22918(matrix4f, 0.0f, height, 0.0f);
        builder.method_22918(matrix4f, width, height, 0.0f);
        builder.method_22918(matrix4f, width, 0.0f, 0.0f);
        class_286.method_43433((class_9801)builder.method_60800());
    }

    private static class_6367 createFbo() {
        return new class_6367(mc.method_22683().method_4489(), mc.method_22683().method_4506(), false);
    }

    @Generated
    private KawaseBlurProgram() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

