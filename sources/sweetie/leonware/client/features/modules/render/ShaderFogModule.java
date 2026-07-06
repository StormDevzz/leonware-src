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
import org.joml.Matrix4f;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ShaderFogModule.class */
@ModuleRegister(name = "Shader Fog", category = Category.RENDER)
public class ShaderFogModule extends Module {
    private final ModeSetting mode = new ModeSetting("Mode").value("Water").values("Water", "Caustic", "Aurora", "Nebula");
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(0.5f);
    private final SliderSetting intensity = new SliderSetting("Intensity").value(Float.valueOf(0.01f)).range(0.001f, 0.05f).step(0.001f);
    private final SliderSetting alpha = new SliderSetting("Alpha").value(Float.valueOf(1.0f)).range(0.3f, 1.0f).step(0.05f);
    private long startMillis = -1;
    private static final ShaderFogModule instance = new ShaderFogModule();
    private static final class_10156 WATER_SHADER = new class_10156(FileUtil.getShader("post/sky/water"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 CAUSTIC_SHADER = new class_10156(FileUtil.getShader("post/sky/caustic"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 AURORA_SHADER = new class_10156(FileUtil.getShader("post/sky/aurora"), class_290.field_1592, class_10149.field_53930);
    private static final class_10156 NEBULA_SHADER = new class_10156(FileUtil.getShader("post/sky/nebula"), class_290.field_1592, class_10149.field_53930);

    @Generated
    public static ShaderFogModule getInstance() {
        return instance;
    }

    public ShaderFogModule() {
        addSettings(this.mode, this.speed, this.scale, this.intensity, this.alpha);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.startMillis = -1L;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            renderSkyShader();
        }));
        addEvents(renderEvent);
    }

    public void renderSkyShader() {
        class_10156 class_10156Var;
        if (isEnabled()) {
            if (this.startMillis < 0) {
                this.startMillis = System.currentTimeMillis();
            }
            float time = (System.currentTimeMillis() - this.startMillis) / 1000.0f;
            float fw = mc.method_22683().method_4489();
            float fh = mc.method_22683().method_4506();
            Color themeColor = UIColors.currentTheme().getPrimaryColor();
            float cr = themeColor.getRed() / 255.0f;
            float cg = themeColor.getGreen() / 255.0f;
            float cb = themeColor.getBlue() / 255.0f;
            switch (this.mode.getValue()) {
                case "Caustic":
                    class_10156Var = CAUSTIC_SHADER;
                    break;
                case "Aurora":
                    class_10156Var = AURORA_SHADER;
                    break;
                case "Nebula":
                    class_10156Var = NEBULA_SHADER;
                    break;
                default:
                    class_10156Var = WATER_SHADER;
                    break;
            }
            class_10156 key = class_10156Var;
            class_5944 shader = RenderSystem.setShader(key);
            if (shader == null) {
                return;
            }
            shader.method_34582("uTime").method_1251(time);
            shader.method_34582("uResolution").method_1255(fw, fh);
            shader.method_34582("uColor").method_1249(cr, cg, cb);
            shader.method_34582("uAlpha").method_1251(this.alpha.getValue().floatValue());
            shader.method_34582("uSpeed").method_1251(this.speed.getValue().floatValue());
            shader.method_34582("uScale").method_1251(this.scale.getValue().floatValue());
            shader.method_34582("uIntensity").method_1251(this.intensity.getValue().floatValue());
            class_4184 cam = mc.field_1773.method_19418();
            float yawRad = (float) Math.toRadians(-cam.method_19330());
            float pitchRad = (float) Math.toRadians(cam.method_19329());
            shader.method_34582("uCameraDir").method_1255(yawRad, pitchRad);
            shader.method_34582("uFov").method_1251(((Integer) mc.field_1690.method_41808().method_41753()).intValue());
            Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
            RenderSystem.setProjectionMatrix(new Matrix4f(), class_10366.field_54954);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(514);
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            Matrix4f identity = new Matrix4f();
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
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
    }

    @Override // sweetie.leonware.api.module.Module
    public String getDisplayInfo() {
        return this.mode.getValue();
    }
}
