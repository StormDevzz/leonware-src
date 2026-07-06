/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_2338
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.lwjgl.opengl.GL11
 */
package sweetie.leonware.client.features.modules.render.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.particles.ParticlesModule;

public class CubeParticles
extends ParticlesModule.BaseSettings
implements QuickImports {
    private final SliderSetting distance;
    private final SliderSetting riseSpeed;
    private final SliderSetting rotateSpeed;
    private final BooleanSetting glow;
    private final SliderSetting glowSize;
    private final List<CubeParticle> particles;
    private final TimerUtil spawnTimer;

    public CubeParticles() {
        super("Cubes");
        this.distance = new SliderSetting(this.prefix + "Distance").value(Float.valueOf(20.0f)).range(5.0f, 50.0f).step(1.0f);
        this.riseSpeed = new SliderSetting(this.prefix + "Rise Speed").value(Float.valueOf(0.03f)).range(0.01f, 0.1f).step(0.01f);
        this.rotateSpeed = new SliderSetting(this.prefix + "Rotate Speed").value(Float.valueOf(2.0f)).range(0.5f, 5.0f).step(0.5f);
        this.glow = new BooleanSetting(this.prefix + "Glow").value(true);
        this.glowSize = new SliderSetting(this.prefix + "Glow Size").value(Float.valueOf(2.0f)).range(1.0f, 4.0f).step(0.5f);
        this.particles = new ArrayList<CubeParticle>();
        this.spawnTimer = new TimerUtil();
        this.addSettings(this.distance, this.riseSpeed, this.rotateSpeed, this.glow, this.glowSize);
    }

    public void toggle() {
        this.particles.clear();
        this.spawnTimer.reset();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            this.particles.removeIf(CubeParticle::update);
            int diff = ((Float)this.count().getValue()).intValue() - this.particles.size();
            if (diff > 0) {
                float d = ((Float)this.distance.getValue()).floatValue();
                int toSpawn = Math.min(diff, 5);
                for (int i = 0; i < toSpawn; ++i) {
                    double spawnX = CubeParticles.mc.field_1724.method_23317() + (double)MathUtil.randomInRange(-d, d);
                    double spawnZ = CubeParticles.mc.field_1724.method_23321() + (double)MathUtil.randomInRange(-d, d);
                    double spawnY = this.findGroundY(spawnX, spawnZ);
                    this.particles.add(new CubeParticle((float)spawnX, (float)spawnY, (float)spawnZ, MathUtil.randomInRange(-0.005f, 0.005f), ((Float)this.riseSpeed.getValue()).floatValue(), MathUtil.randomInRange(-0.005f, 0.005f), this.particles.size(), ((Float)this.size().getValue()).floatValue(), ((Float)this.lifeTime().getValue()).intValue(), ((Float)this.spawnDuration().getValue()).floatValue(), ((Float)this.dyingDuration().getValue()).floatValue(), ((Float)this.rotateSpeed.getValue()).floatValue()));
                }
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            class_4587 matrixStack = event.matrixStack();
            for (CubeParticle particle : this.particles) {
                particle.updateAlpha();
                RenderUtil.WORLD.startRender(matrixStack);
                particle.render(matrixStack, (Boolean)this.glow.getValue(), ((Float)this.glowSize.getValue()).floatValue());
                RenderUtil.WORLD.endRender(matrixStack);
            }
        }));
        this.addEvents(renderEvent, updateEvent);
    }

    private double findGroundY(double x, double z) {
        if (CubeParticles.mc.field_1687 == null || CubeParticles.mc.field_1724 == null) {
            return CubeParticles.mc.field_1724.method_23318() - 1.0;
        }
        int playerY = (int)CubeParticles.mc.field_1724.method_23318();
        for (int y = playerY + 2; y > playerY - 15; --y) {
            class_2338 pos = new class_2338((int)Math.floor(x), y, (int)Math.floor(z));
            class_2338 above = pos.method_10084();
            boolean blockSolid = !CubeParticles.mc.field_1687.method_8320(pos).method_26215();
            boolean aboveAir = CubeParticles.mc.field_1687.method_8320(above).method_26215();
            if (!blockSolid || !aboveAir) continue;
            return (double)y + 1.0;
        }
        return CubeParticles.mc.field_1724.method_23318();
    }

    private static class CubeParticle
    implements QuickImports {
        private float prevX;
        private float prevY;
        private float prevZ;
        private float x;
        private float y;
        private float z;
        private float motionX;
        private float motionY;
        private float motionZ;
        private int maxLife;
        private float size;
        private float prevSize = 0.0f;
        private int index;
        private float rotX;
        private float rotY;
        private float rotZ;
        private float prevRotX;
        private float prevRotY;
        private float prevRotZ;
        private float rotSpeedX;
        private float rotSpeedY;
        private float rotSpeedZ;
        private float spawnDuration;
        private float dyingDuration;
        private final TimerUtil timerUtil = new TimerUtil();
        private final AnimationUtil alphaAnimation = new AnimationUtil();

        public CubeParticle(float x, float y, float z, float motionX, float motionY, float motionZ, int index, float size, int lifetime, float spawnDuration, float dyingDuration, float rotateSpeed) {
            this.prevX = x;
            this.prevY = y;
            this.prevZ = z;
            this.x = x;
            this.y = y;
            this.z = z;
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.index = index;
            this.size = size;
            this.maxLife = MathUtil.randomInRange(Math.max(lifetime / 2, 0), lifetime);
            this.spawnDuration = spawnDuration;
            this.dyingDuration = dyingDuration;
            this.rotX = MathUtil.randomInRange(-180.0f, 180.0f);
            this.rotY = MathUtil.randomInRange(-180.0f, 180.0f);
            this.rotZ = MathUtil.randomInRange(-180.0f, 180.0f);
            this.prevRotX = this.rotX;
            this.prevRotY = this.rotY;
            this.prevRotZ = this.rotZ;
            this.rotSpeedX = MathUtil.randomInRange(-rotateSpeed, rotateSpeed);
            this.rotSpeedY = MathUtil.randomInRange(-rotateSpeed, rotateSpeed);
            this.rotSpeedZ = MathUtil.randomInRange(-rotateSpeed, rotateSpeed);
        }

        public boolean update() {
            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;
            this.prevRotX = this.rotX;
            this.prevRotY = this.rotY;
            this.prevRotZ = this.rotZ;
            this.x += this.motionX;
            this.y += this.motionY;
            this.z += this.motionZ;
            this.rotX += this.rotSpeedX;
            this.rotY += this.rotSpeedY;
            this.rotZ += this.rotSpeedZ;
            return CubeParticle.mc.field_1724.method_19538().method_1022(new class_243((double)this.x, (double)this.y, (double)this.z)) >= 80.0 || this.alphaAnimation.getValue() <= 0.0 && this.timerUtil.finished((this.spawnDuration + this.dyingDuration + (float)this.maxLife) * 50.0f);
        }

        public void updateAlpha() {
            this.alphaAnimation.update();
            float alphaAnim = (float)this.alphaAnimation.getValue();
            if ((double)alphaAnim <= 0.0 && !this.timerUtil.finished(this.spawnDuration * 50.0f)) {
                this.alphaAnimation.run(1.0, (long)(this.spawnDuration * 50.0f), Easing.QUINT_OUT);
            }
            if ((double)alphaAnim >= 1.0 && this.timerUtil.finished((this.spawnDuration + (float)this.maxLife) * 50.0f)) {
                this.alphaAnimation.run(0.0, (long)(this.dyingDuration * 50.0f), Easing.QUINT_OUT);
            }
        }

        public void render(class_4587 matrixStack, boolean glow, float glowSize) {
            float halfSize;
            class_243 cam = CubeParticle.mc.method_1561().field_4686.method_19326();
            float alpha = (float)this.alphaAnimation.getValue();
            double interpX = (double)MathUtil.interpolate(this.prevX, this.x) - cam.field_1352;
            double interpY = (double)MathUtil.interpolate(this.prevY, this.y) - cam.field_1351;
            double interpZ = (double)MathUtil.interpolate(this.prevZ, this.z) - cam.field_1350;
            float interpRotX = MathUtil.interpolate(this.prevRotX, this.rotX);
            float interpRotY = MathUtil.interpolate(this.prevRotY, this.rotY);
            float interpRotZ = MathUtil.interpolate(this.prevRotZ, this.rotZ);
            this.prevSize = halfSize = MathUtil.interpolate(this.prevSize, this.size * alpha);
            Color color = ColorUtil.setAlpha(UIColors.gradient(this.index * 90), (int)(255.0f * alpha));
            matrixStack.method_22903();
            matrixStack.method_22904(interpX, interpY, interpZ);
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(interpRotX));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(interpRotY));
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(interpRotZ));
            if (glow) {
                this.renderGlow(matrixStack, halfSize * glowSize, color, alpha);
            }
            this.renderCube(matrixStack, halfSize, color);
            matrixStack.method_22909();
        }

        private void renderGlow(class_4587 matrixStack, float size, Color color, float alpha) {
            class_4184 camera = CubeParticle.mc.field_1773.method_19418();
            matrixStack.method_22903();
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(-MathUtil.interpolate(this.prevRotZ, this.rotZ)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-MathUtil.interpolate(this.prevRotY, this.rotY)));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(-MathUtil.interpolate(this.prevRotX, this.rotX)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            Matrix4f glowMatrix = matrixStack.method_23760().method_23761();
            RenderSystem.setShader((class_10156)class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            int centerAlpha = (int)(80.0f * alpha);
            int edgeAlpha = 0;
            buf.method_22918(glowMatrix, 0.0f, 0.0f, 0.0f).method_1336(r, g, b, centerAlpha);
            int segments = 16;
            for (int i = 0; i <= segments; ++i) {
                float angle = (float)((double)(i * 2) * Math.PI / (double)segments);
                float px = (float)Math.cos(angle) * size;
                float py = (float)Math.sin(angle) * size;
                buf.method_22918(glowMatrix, px, py, 0.0f).method_1336(r, g, b, edgeAlpha);
            }
            class_286.method_43433((class_9801)buf.method_60800());
            RenderSystem.enableCull();
            matrixStack.method_22909();
        }

        private void renderCube(class_4587 matrixStack, float size, Color color) {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            float s = size;
            RenderSystem.setShader((class_10156)class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderSystem.lineWidth((float)2.0f);
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            int a = color.getAlpha();
            buf.method_22918(matrix, -s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, -s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, s, s).method_1336(r, g, b, a);
            buf.method_22918(matrix, -s, -s, -s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, s, s, s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, s, -s, -s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -s, s, s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -s, -s, s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, s, s, -s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, s, -s, s).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -s, s, -s).method_1336(r, g, b, a / 2);
            class_286.method_43433((class_9801)buf.method_60800());
            GL11.glDisable((int)2848);
            RenderSystem.enableCull();
        }
    }
}

