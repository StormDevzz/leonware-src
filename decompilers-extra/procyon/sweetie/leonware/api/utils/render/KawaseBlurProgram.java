// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render;

import java.util.ArrayList;
import net.minecraft.class_10149;
import sweetie.leonware.api.system.files.FileUtil;
import lombok.Generated;
import net.minecraft.class_287;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import org.joml.Matrix4f;
import net.minecraft.class_5944;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.framelimiter.IFrameCall;
import net.minecraft.class_4587;
import net.minecraft.class_276;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.FramebufferResizeEvent;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import net.minecraft.class_6367;
import java.util.List;
import net.minecraft.class_10156;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class KawaseBlurProgram implements QuickImports
{
    private static final class_10156 upShader;
    private static final class_10156 downShader;
    private static final int blurPasses;
    public static final List<class_6367> fbos;
    private static boolean init;
    private static final FrameLimiter f;
    
    public static void load() {
        if (!KawaseBlurProgram.init) {
            for (int i = 0; i <= InterfaceModule.getPasses(); ++i) {
                KawaseBlurProgram.fbos.add(createFbo());
            }
            KawaseBlurProgram.init = true;
        }
        FramebufferResizeEvent.getInstance().subscribe(new Listener<FramebufferResizeEvent>(event -> recreate()));
    }
    
    public static void recreate() {
        KawaseBlurProgram.fbos.forEach(class_276::method_1238);
        KawaseBlurProgram.fbos.clear();
        for (int i = 0; i <= InterfaceModule.getPasses(); ++i) {
            KawaseBlurProgram.fbos.add(createFbo());
        }
    }
    
    public static void render(final class_4587 matrixStack) {
        if (InterfaceModule.getGlassy() != 1.0f) {
            KawaseBlurProgram.f.execute(40, () -> {
                final int actualPasses = Math.max(KawaseBlurProgram.fbos.size() - 1, 1);
                applyBlurPass(matrixStack, KawaseBlurProgram.downShader, KawaseBlurProgram.mc.method_1522(), (class_276)KawaseBlurProgram.fbos.getFirst(), 0);
                for (int i = 0; i < actualPasses; ++i) {
                    applyBlurPass(matrixStack, KawaseBlurProgram.downShader, (class_276)KawaseBlurProgram.fbos.get(i), (class_276)KawaseBlurProgram.fbos.get(i + 1), i + 1);
                }
                for (int j = actualPasses; j > 0; --j) {
                    applyBlurPass(matrixStack, KawaseBlurProgram.upShader, (class_276)KawaseBlurProgram.fbos.get(j), (class_276)KawaseBlurProgram.fbos.get(j - 1), j);
                }
                KawaseBlurProgram.mc.method_1522().method_1235(false);
            });
        }
    }
    
    private static void applyBlurPass(final class_4587 matrixStack, final class_10156 shaderKey, final class_276 source, final class_276 destination, final int pass) {
        destination.method_1235(false);
        RenderSystem.setShaderTexture(0, source.method_30277());
        final class_5944 shader = RenderSystem.setShader(shaderKey);
        shader.method_34582("uHalfTexelSize").method_1255(0.5f / source.field_1482, 0.5f / source.field_1481);
        shader.method_34582("uOffset").method_1251(InterfaceModule.getOffset() * (pass / (float)KawaseBlurProgram.blurPasses));
        drawFullQuad(matrixStack.method_23760().method_23761());
        destination.method_1240();
    }
    
    private static void drawFullQuad(final Matrix4f matrix4f) {
        final float width = (float)KawaseBlurProgram.mc.method_22683().method_4486();
        final float height = (float)KawaseBlurProgram.mc.method_22683().method_4502();
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        builder.method_22918(matrix4f, 0.0f, 0.0f, 0.0f);
        builder.method_22918(matrix4f, 0.0f, height, 0.0f);
        builder.method_22918(matrix4f, width, height, 0.0f);
        builder.method_22918(matrix4f, width, 0.0f, 0.0f);
        class_286.method_43433(builder.method_60800());
    }
    
    private static class_6367 createFbo() {
        return new class_6367(KawaseBlurProgram.mc.method_22683().method_4489(), KawaseBlurProgram.mc.method_22683().method_4506(), false);
    }
    
    @Generated
    private KawaseBlurProgram() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        upShader = new class_10156(FileUtil.getShader("post/blur/upscale"), class_290.field_1592, class_10149.field_53930);
        downShader = new class_10156(FileUtil.getShader("post/blur/downscale"), class_290.field_1592, class_10149.field_53930);
        blurPasses = InterfaceModule.getPasses();
        fbos = new ArrayList<class_6367>();
        KawaseBlurProgram.init = false;
        f = new FrameLimiter(false);
    }
}
