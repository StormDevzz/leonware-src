/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_10366
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4184
 *  net.minecraft.class_5944
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_10366;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4184;
import net.minecraft.class_5944;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.UIColors;

@ModuleRegister(name="Shader Fog", category=Category.RENDER)
public class ShaderFogModule
extends Module {
    private static final ShaderFogModule instance = new ShaderFogModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Water").values("Water", "Caustic", "Aurora", "Nebula");
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(0.5f);
    private final SliderSetting intensity = new SliderSetting("Intensity").value(Float.valueOf(0.01f)).range(0.001f, 0.05f).step(0.001f);
    private final SliderSetting alpha = new SliderSetting("Alpha").value(Float.valueOf(1.0f)).range(0.3f, 1.0f).step(0.05f);
    private static final class_10156 WATER_SHADER = new class_10156(FileUtil.getShader("post/sky/water"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 CAUSTIC_SHADER = new class_10156(FileUtil.getShader("post/sky/caustic"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 AURORA_SHADER = new class_10156(FileUtil.getShader("post/sky/aurora"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 NEBULA_SHADER = new class_10156(FileUtil.getShader("post/sky/nebula"), class_290.field_1592, class_10149.field_53930);
    private long startMillis = -1L;

    public ShaderFogModule() {
        this.addSettings(this.mode, this.speed, this.scale, this.intensity, this.alpha);
    }

    @Override
    public void onDisable() {
        this.startMillis = -1L;
    }

    @Override
    public void onEvent() {
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (ShaderFogModule.mc.field_1724 == null || ShaderFogModule.mc.field_1687 == null) {
                return;
            }
            this.renderSkyShader();
        }));
        this.addEvents(renderEvent);
    }

    public void renderSkyShader() {
        if (!this.isEnabled()) {
            return;
        }
        if (this.startMillis < 0L) {
            this.startMillis = System.currentTimeMillis();
        }
        float time = (float)(System.currentTimeMillis() - this.startMillis) / 1000.0f;
        float fw = mc.method_22683().method_4489();
        float fh = mc.method_22683().method_4506();
        Color themeColor = UIColors.currentTheme().getPrimaryColor();
        float cr = (float)themeColor.getRed() / 255.0f;
        float cg = (float)themeColor.getGreen() / 255.0f;
        float cb = (float)themeColor.getBlue() / 255.0f;
        class_10156 key = switch ((String)this.mode.getValue()) {
            case "Caustic" -> CAUSTIC_SHADER;
            case "Aurora" -> AURORA_SHADER;
            case "Nebula" -> NEBULA_SHADER;
            default -> WATER_SHADER;
        };
        class_5944 shader = RenderSystem.setShader((class_10156)key);
        if (shader == null) {
            return;
        }
        shader.method_34582("uTime").method_1251(time);
        shader.method_34582("uResolution").method_1255(fw, fh);
        shader.method_34582("uColor").method_1249(cr, cg, cb);
        shader.method_34582("uAlpha").method_1251(((Float)this.alpha.getValue()).floatValue());
        shader.method_34582("uSpeed").method_1251(((Float)this.speed.getValue()).floatValue());
        shader.method_34582("uScale").method_1251(((Float)this.scale.getValue()).floatValue());
        shader.method_34582("uIntensity").method_1251(((Float)this.intensity.getValue()).floatValue());
        class_4184 cam = ShaderFogModule.mc.field_1773.method_19418();
        float yawRad = (float)Math.toRadians(-cam.method_19330());
        float pitchRad = (float)Math.toRadians(cam.method_19329());
        shader.method_34582("uCameraDir").method_1255(yawRad, pitchRad);
        shader.method_34582("uFov").method_1251((float)((Integer)ShaderFogModule.mc.field_1690.method_41808().method_41753()).intValue());
        Matrix4f savedProj = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix());
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f(), (class_10366)class_10366.field_54954);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)514);
        RenderSystem.depthMask((boolean)false);
        RenderSystem.disableCull();
        Matrix4f identity = new Matrix4f();
        class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        buf.method_22918(identity, -1.0f, -1.0f, 1.0f);
        buf.method_22918(identity, 1.0f, -1.0f, 1.0f);
        buf.method_22918(identity, 1.0f, 1.0f, 1.0f);
        buf.method_22918(identity, -1.0f, 1.0f, 1.0f);
        class_286.method_43433((class_9801)buf.method_60800());
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.depthFunc((int)515);
        RenderSystem.setProjectionMatrix((Matrix4f)savedProj, (class_10366)class_10366.field_54953);
    }

    @Override
    public String getDisplayInfo() {
        return (String)this.mode.getValue();
    }

    @Generated
    public static ShaderFogModule getInstance() {
        return instance;
    }
}

