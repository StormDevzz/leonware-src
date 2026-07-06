// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.motionblur;

import org.joml.Matrix4fc;
import org.joml.Vector3f;
import net.minecraft.class_310;
import sweetie.leonware.api.utils.other.TextUtil;
import net.fabricmc.loader.api.FabricLoader;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import net.minecraft.class_2960;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.joml.Matrix4f;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;

public class ShaderMotionBlur
{
    private final MotionBlurModule config;
    private final ManagedShaderEffect motionBlurShader;
    private long lastNano;
    private float currentBlur;
    private float currentFPS;
    private final Matrix4f tempPrevModelView;
    private final Matrix4f tempPrevProjection;
    private final Matrix4f tempProjInverse;
    private final Matrix4f tempMvInverse;
    
    public ShaderMotionBlur(final MotionBlurModule config) {
        this.currentBlur = 0.0f;
        this.currentFPS = 0.0f;
        this.tempPrevModelView = new Matrix4f();
        this.tempPrevProjection = new Matrix4f();
        this.tempProjInverse = new Matrix4f();
        this.tempMvInverse = new Matrix4f();
        this.config = config;
        this.motionBlurShader = ShaderEffectManager.getInstance().manage(class_2960.method_60655("LeonWare".toLowerCase(), "motion_blur"), shader -> shader.setUniformValue("BlendFactor", (float)config.strength.getValue()));
    }
    
    public void registerShaderCallbacks() {
        Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(-1, event -> {
            final long now = System.nanoTime();
            final float deltaTime = (now - this.lastNano) / 1.0E9f;
            final float deltaTick = deltaTime * 20.0f;
            this.lastNano = now;
            if (deltaTime > 0.0f && deltaTime < 1.0f) {
                this.currentFPS = 1.0f / deltaTime;
            }
            else {
                this.currentFPS = 0.0f;
            }
            if (this.shouldRenderMotionBlur()) {
                this.applyMotionBlur(deltaTick);
            }
        }));
    }
    
    private boolean shouldRenderMotionBlur() {
        if (this.config.strength.getValue() == 0.0f || !this.config.isEnabled()) {
            return false;
        }
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            TextUtil.sendMessage("\u041d\u0435 \u043c\u043e\u0433\u0443 \u043d\u0430\u0445 \u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c, \u043f\u043e\u0442\u043e\u043c\u0443 \u0447\u0442\u043e Iris \u0441\u0442\u043e\u0438\u0442!");
            this.config.setEnabled(false);
            return false;
        }
        return true;
    }
    
    private void applyMotionBlur(final float deltaTick) {
        final class_310 client = class_310.method_1551();
        MonitorInfoProvider.updateDisplayInfo();
        final int displayRefreshRate = MonitorInfoProvider.getRefreshRate();
        float scaledStrength;
        final float baseStrength = scaledStrength = this.config.strength.getValue();
        if (this.config.useRRC.getValue()) {
            float fpsOverRefresh = (displayRefreshRate > 0) ? (this.currentFPS / displayRefreshRate) : 1.0f;
            if (fpsOverRefresh < 1.0f) {
                fpsOverRefresh = 1.0f;
            }
            scaledStrength = baseStrength * fpsOverRefresh;
        }
        if (this.currentBlur != scaledStrength) {
            this.motionBlurShader.setUniformValue("BlendFactor", scaledStrength);
            this.currentBlur = scaledStrength;
        }
        final int sampleAmount = this.getSampleAmountForFPS(this.currentFPS);
        final int halfSampleAmount = sampleAmount / 2;
        final float invSamples = 1.0f / sampleAmount;
        this.motionBlurShader.setUniformValue("view_res", (float)client.method_1522().field_1480, (float)client.method_1522().field_1477);
        this.motionBlurShader.setUniformValue("view_pixel_size", 1.0f / client.method_1522().field_1480, 1.0f / client.method_1522().field_1477);
        this.motionBlurShader.setUniformValue("motionBlurSamples", sampleAmount);
        this.motionBlurShader.setUniformValue("halfSamples", halfSampleAmount);
        this.motionBlurShader.setUniformValue("inverseSamples", invSamples);
        this.motionBlurShader.setUniformValue("blurAlgorithm", MotionBlurModule.BlurAlgorithm.BACKWARDS.ordinal());
        this.motionBlurShader.render(deltaTick);
    }
    
    private int getSampleAmountForFPS(final float fps) {
        if (fps > 360.0f) {
            return 8;
        }
        if (fps > 120.0f) {
            return 10;
        }
        if (fps > 60.0f) {
            return 12;
        }
        return 20;
    }
    
    public void setFrameMotionBlur(final Matrix4f modelView, final Matrix4f prevModelView, final Matrix4f projection, final Matrix4f prevProjection, final Vector3f cameraPos, final Vector3f prevCameraPos) {
        this.motionBlurShader.setUniformValue("mvInverse", this.tempMvInverse.set((Matrix4fc)modelView).invert());
        this.motionBlurShader.setUniformValue("projInverse", this.tempProjInverse.set((Matrix4fc)projection).invert());
        this.motionBlurShader.setUniformValue("prevModelView", this.tempPrevModelView.set((Matrix4fc)prevModelView));
        this.motionBlurShader.setUniformValue("prevProjection", this.tempPrevProjection.set((Matrix4fc)prevProjection));
        this.motionBlurShader.setUniformValue("cameraPos", cameraPos.x, cameraPos.y, cameraPos.z);
        this.motionBlurShader.setUniformValue("prevCameraPos", prevCameraPos.x, prevCameraPos.y, prevCameraPos.z);
    }
    
    public void updateBlurStrength(final float strength) {
        this.motionBlurShader.setUniformValue("BlendFactor", strength);
        this.currentBlur = strength;
    }
}
