// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import net.minecraft.class_10149;
import sweetie.leonware.api.system.files.FileUtil;
import lombok.Generated;
import net.minecraft.class_287;
import net.minecraft.class_4184;
import net.minecraft.class_5944;
import java.awt.Color;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_10366;
import org.joml.Matrix4fc;
import org.joml.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_10156;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Shader Fog", category = Category.RENDER)
public class ShaderFogModule extends Module
{
    private static final ShaderFogModule instance;
    private final ModeSetting mode;
    private final SliderSetting speed;
    private final SliderSetting scale;
    private final SliderSetting intensity;
    private final SliderSetting alpha;
    private static final class_10156 WATER_SHADER;
    private static final class_10156 CAUSTIC_SHADER;
    private static final class_10156 AURORA_SHADER;
    private static final class_10156 NEBULA_SHADER;
    private long startMillis;
    
    public ShaderFogModule() {
        this.mode = new ModeSetting("Mode").value("Water").values("Water", "Caustic", "Aurora", "Nebula");
        this.speed = new SliderSetting("Speed").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        this.scale = new SliderSetting("Scale").value(5.0f).range(1.0f, 20.0f).step(0.5f);
        this.intensity = new SliderSetting("Intensity").value(0.01f).range(0.001f, 0.05f).step(0.001f);
        this.alpha = new SliderSetting("Alpha").value(1.0f).range(0.3f, 1.0f).step(0.05f);
        this.startMillis = -1L;
        this.addSettings(this.mode, this.speed, this.scale, this.intensity, this.alpha);
    }
    
    @Override
    public void onDisable() {
        this.startMillis = -1L;
    }
    
    @Override
    public void onEvent() {
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (ShaderFogModule.mc.field_1724 == null || ShaderFogModule.mc.field_1687 == null) {
                return;
            }
            else {
                this.renderSkyShader();
                return;
            }
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
        final float time = (System.currentTimeMillis() - this.startMillis) / 1000.0f;
        final float fw = (float)ShaderFogModule.mc.method_22683().method_4489();
        final float fh = (float)ShaderFogModule.mc.method_22683().method_4506();
        final Color themeColor = UIColors.currentTheme().getPrimaryColor();
        final float cr = themeColor.getRed() / 255.0f;
        final float cg = themeColor.getGreen() / 255.0f;
        final float cb = themeColor.getBlue() / 255.0f;
        final String s = this.mode.getValue();
        final class_10156 key = switch (s) {
            case "Caustic" -> ShaderFogModule.CAUSTIC_SHADER;
            case "Aurora" -> ShaderFogModule.AURORA_SHADER;
            case "Nebula" -> ShaderFogModule.NEBULA_SHADER;
            default -> ShaderFogModule.WATER_SHADER;
        };
        final class_5944 shader = RenderSystem.setShader(key);
        if (shader == null) {
            return;
        }
        shader.method_34582("uTime").method_1251(time);
        shader.method_34582("uResolution").method_1255(fw, fh);
        shader.method_34582("uColor").method_1249(cr, cg, cb);
        shader.method_34582("uAlpha").method_1251((float)this.alpha.getValue());
        shader.method_34582("uSpeed").method_1251((float)this.speed.getValue());
        shader.method_34582("uScale").method_1251((float)this.scale.getValue());
        shader.method_34582("uIntensity").method_1251((float)this.intensity.getValue());
        final class_4184 cam = ShaderFogModule.mc.field_1773.method_19418();
        final float yawRad = (float)Math.toRadians(-cam.method_19330());
        final float pitchRad = (float)Math.toRadians(cam.method_19329());
        shader.method_34582("uCameraDir").method_1255(yawRad, pitchRad);
        shader.method_34582("uFov").method_1251((float)(int)ShaderFogModule.mc.field_1690.method_41808().method_41753());
        final Matrix4f savedProj = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix());
        RenderSystem.setProjectionMatrix(new Matrix4f(), class_10366.field_54954);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(514);
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        final Matrix4f identity = new Matrix4f();
        final class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        buf.method_22918(identity, -1.0f, -1.0f, 1.0f);
        buf.method_22918(identity, 1.0f, -1.0f, 1.0f);
        buf.method_22918(identity, 1.0f, 1.0f, 1.0f);
        buf.method_22918(identity, -1.0f, 1.0f, 1.0f);
        class_286.method_43433(buf.method_60800());
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthFunc(515);
        RenderSystem.setProjectionMatrix(savedProj, class_10366.field_54953);
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.getValue();
    }
    
    @Generated
    public static ShaderFogModule getInstance() {
        return ShaderFogModule.instance;
    }
    
    static {
        instance = new ShaderFogModule();
        WATER_SHADER = new class_10156(FileUtil.getShader("post/sky/water"), class_290.field_1592, class_10149.field_53930);
        CAUSTIC_SHADER = new class_10156(FileUtil.getShader("post/sky/caustic"), class_290.field_1592, class_10149.field_53930);
        AURORA_SHADER = new class_10156(FileUtil.getShader("post/sky/aurora"), class_290.field_1592, class_10149.field_53930);
        NEBULA_SHADER = new class_10156(FileUtil.getShader("post/sky/nebula"), class_290.field_1592, class_10149.field_53930);
    }
}
