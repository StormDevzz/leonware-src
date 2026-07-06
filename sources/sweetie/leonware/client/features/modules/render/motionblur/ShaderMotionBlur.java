package sweetie.leonware.client.features.modules.render.motionblur;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.client.features.modules.render.motionblur.MotionBlurModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/motionblur/ShaderMotionBlur.class */
public class ShaderMotionBlur {
    private final MotionBlurModule config;
    private final ManagedShaderEffect motionBlurShader;
    private long lastNano;
    private float currentBlur = 0.0f;
    private float currentFPS = 0.0f;
    private final Matrix4f tempPrevModelView = new Matrix4f();
    private final Matrix4f tempPrevProjection = new Matrix4f();
    private final Matrix4f tempProjInverse = new Matrix4f();
    private final Matrix4f tempMvInverse = new Matrix4f();

    public ShaderMotionBlur(MotionBlurModule config) {
        this.config = config;
        this.motionBlurShader = ShaderEffectManager.getInstance().manage(class_2960.method_60655(ClientInfo.NAME.toLowerCase(), "motion_blur"), shader -> {
            shader.setUniformValue("BlendFactor", config.strength.getValue().floatValue());
        });
    }

    public void registerShaderCallbacks() {
        Render3DEvent.getInstance().subscribe(new Listener(-1, event -> {
            long now = System.nanoTime();
            float deltaTime = (now - this.lastNano) / 1.0E9f;
            float deltaTick = deltaTime * 20.0f;
            this.lastNano = now;
            if (deltaTime > 0.0f && deltaTime < 1.0f) {
                this.currentFPS = 1.0f / deltaTime;
            } else {
                this.currentFPS = 0.0f;
            }
            if (shouldRenderMotionBlur()) {
                applyMotionBlur(deltaTick);
            }
        }));
    }

    private boolean shouldRenderMotionBlur() {
        if (this.config.strength.getValue().floatValue() == 0.0f || !this.config.isEnabled()) {
            return false;
        }
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            TextUtil.sendMessage("Не могу нах включить, потому что Iris стоит!");
            this.config.setEnabled(false);
            return false;
        }
        return true;
    }

    private void applyMotionBlur(float deltaTick) {
        class_310 client = class_310.method_1551();
        MonitorInfoProvider.updateDisplayInfo();
        int displayRefreshRate = MonitorInfoProvider.getRefreshRate();
        float baseStrength = this.config.strength.getValue().floatValue();
        float scaledStrength = baseStrength;
        if (this.config.useRRC.getValue().booleanValue()) {
            float fpsOverRefresh = displayRefreshRate > 0 ? this.currentFPS / displayRefreshRate : 1.0f;
            if (fpsOverRefresh < 1.0f) {
                fpsOverRefresh = 1.0f;
            }
            scaledStrength = baseStrength * fpsOverRefresh;
        }
        if (this.currentBlur != scaledStrength) {
            this.motionBlurShader.setUniformValue("BlendFactor", scaledStrength);
            this.currentBlur = scaledStrength;
        }
        int sampleAmount = getSampleAmountForFPS(this.currentFPS);
        int halfSampleAmount = sampleAmount / 2;
        float invSamples = 1.0f / sampleAmount;
        this.motionBlurShader.setUniformValue("view_res", client.method_1522().field_1480, client.method_1522().field_1477);
        this.motionBlurShader.setUniformValue("view_pixel_size", 1.0f / client.method_1522().field_1480, 1.0f / client.method_1522().field_1477);
        this.motionBlurShader.setUniformValue("motionBlurSamples", sampleAmount);
        this.motionBlurShader.setUniformValue("halfSamples", halfSampleAmount);
        this.motionBlurShader.setUniformValue("inverseSamples", invSamples);
        this.motionBlurShader.setUniformValue("blurAlgorithm", MotionBlurModule.BlurAlgorithm.BACKWARDS.ordinal());
        this.motionBlurShader.render(deltaTick);
    }

    private int getSampleAmountForFPS(float fps) {
        if (fps > 360.0f) {
            return 8;
        }
        if (fps > 120.0f) {
            return 10;
        }
        return fps > 60.0f ? 12 : 20;
    }

    public void setFrameMotionBlur(Matrix4f modelView, Matrix4f prevModelView, Matrix4f projection, Matrix4f prevProjection, Vector3f cameraPos, Vector3f prevCameraPos) {
        this.motionBlurShader.setUniformValue("mvInverse", this.tempMvInverse.set(modelView).invert());
        this.motionBlurShader.setUniformValue("projInverse", this.tempProjInverse.set(projection).invert());
        this.motionBlurShader.setUniformValue("prevModelView", this.tempPrevModelView.set(prevModelView));
        this.motionBlurShader.setUniformValue("prevProjection", this.tempPrevProjection.set(prevProjection));
        this.motionBlurShader.setUniformValue("cameraPos", cameraPos.x, cameraPos.y, cameraPos.z);
        this.motionBlurShader.setUniformValue("prevCameraPos", prevCameraPos.x, prevCameraPos.y, prevCameraPos.z);
    }

    public void updateBlurStrength(float strength) {
        this.motionBlurShader.setUniformValue("BlendFactor", strength);
        this.currentBlur = strength;
    }
}
