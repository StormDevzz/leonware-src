package sweetie.leonware.client.features.modules.render.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10142;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/CubeParticles.class */
public class CubeParticles extends ParticlesModule.BaseSettings implements QuickImports {
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
        this.glow = new BooleanSetting(this.prefix + "Glow").value((Boolean) true);
        this.glowSize = new SliderSetting(this.prefix + "Glow Size").value(Float.valueOf(2.0f)).range(1.0f, 4.0f).step(0.5f);
        this.particles = new ArrayList();
        this.spawnTimer = new TimerUtil();
        addSettings(this.distance, this.riseSpeed, this.rotateSpeed, this.glow, this.glowSize);
    }

    public void toggle() {
        this.particles.clear();
        this.spawnTimer.reset();
    }

    @Override // sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.particles.removeIf((v0) -> {
                return v0.update();
            });
            int diff = count().getValue().intValue() - this.particles.size();
            if (diff > 0) {
                float d = this.distance.getValue().floatValue();
                int toSpawn = Math.min(diff, 5);
                for (int i = 0; i < toSpawn; i++) {
                    double spawnX = mc.field_1724.method_23317() + ((double) MathUtil.randomInRange(-d, d));
                    double spawnZ = mc.field_1724.method_23321() + ((double) MathUtil.randomInRange(-d, d));
                    double spawnY = findGroundY(spawnX, spawnZ);
                    this.particles.add(new CubeParticle((float) spawnX, (float) spawnY, (float) spawnZ, MathUtil.randomInRange(-0.005f, 0.005f), this.riseSpeed.getValue().floatValue(), MathUtil.randomInRange(-0.005f, 0.005f), this.particles.size(), size().getValue().floatValue(), lifeTime().getValue().intValue(), spawnDuration().getValue().floatValue(), dyingDuration().getValue().floatValue(), this.rotateSpeed.getValue().floatValue()));
                }
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            class_4587 matrixStack = event2.matrixStack();
            for (CubeParticle particle : this.particles) {
                particle.updateAlpha();
                RenderUtil.WORLD.startRender(matrixStack);
                particle.render(matrixStack, this.glow.getValue().booleanValue(), this.glowSize.getValue().floatValue());
                RenderUtil.WORLD.endRender(matrixStack);
            }
        }));
        addEvents(renderEvent, updateEvent);
    }

    private double findGroundY(double x, double z) {
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return mc.field_1724.method_23318() - 1.0d;
        }
        int playerY = (int) mc.field_1724.method_23318();
        for (int y = playerY + 2; y > playerY - 15; y--) {
            class_2338 pos = new class_2338((int) Math.floor(x), y, (int) Math.floor(z));
            class_2338 above = pos.method_10084();
            boolean blockSolid = !mc.field_1687.method_8320(pos).method_26215();
            boolean aboveAir = mc.field_1687.method_8320(above).method_26215();
            if (blockSolid && aboveAir) {
                return ((double) y) + 1.0d;
            }
        }
        return mc.field_1724.method_23318();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/CubeParticles$CubeParticle.class */
    private static class CubeParticle implements QuickImports {
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
        private int index;
        private float rotSpeedX;
        private float rotSpeedY;
        private float rotSpeedZ;
        private float spawnDuration;
        private float dyingDuration;
        private float prevSize = 0.0f;
        private final TimerUtil timerUtil = new TimerUtil();
        private final AnimationUtil alphaAnimation = new AnimationUtil();
        private float rotX = MathUtil.randomInRange(-180.0f, 180.0f);
        private float rotY = MathUtil.randomInRange(-180.0f, 180.0f);
        private float rotZ = MathUtil.randomInRange(-180.0f, 180.0f);
        private float prevRotX = this.rotX;
        private float prevRotY = this.rotY;
        private float prevRotZ = this.rotZ;

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
            return mc.field_1724.method_19538().method_1022(new class_243((double) this.x, (double) this.y, (double) this.z)) >= 80.0d || (this.alphaAnimation.getValue() <= 0.0d && this.timerUtil.finished(((this.spawnDuration + this.dyingDuration) + ((float) this.maxLife)) * 50.0f));
        }

        public void updateAlpha() {
            this.alphaAnimation.update();
            float alphaAnim = (float) this.alphaAnimation.getValue();
            if (alphaAnim <= 0.0d && !this.timerUtil.finished(this.spawnDuration * 50.0f)) {
                this.alphaAnimation.run(1.0d, (long) (this.spawnDuration * 50.0f), Easing.QUINT_OUT);
            }
            if (alphaAnim >= 1.0d && this.timerUtil.finished((this.spawnDuration + this.maxLife) * 50.0f)) {
                this.alphaAnimation.run(0.0d, (long) (this.dyingDuration * 50.0f), Easing.QUINT_OUT);
            }
        }

        public void render(class_4587 matrixStack, boolean glow, float glowSize) {
            class_243 cam = mc.method_1561().field_4686.method_19326();
            float alpha = (float) this.alphaAnimation.getValue();
            double interpX = ((double) MathUtil.interpolate(this.prevX, this.x)) - cam.field_1352;
            double interpY = ((double) MathUtil.interpolate(this.prevY, this.y)) - cam.field_1351;
            double interpZ = ((double) MathUtil.interpolate(this.prevZ, this.z)) - cam.field_1350;
            float interpRotX = MathUtil.interpolate(this.prevRotX, this.rotX);
            float interpRotY = MathUtil.interpolate(this.prevRotY, this.rotY);
            float interpRotZ = MathUtil.interpolate(this.prevRotZ, this.rotZ);
            float halfSize = MathUtil.interpolate(this.prevSize, this.size * alpha);
            this.prevSize = halfSize;
            Color color = ColorUtil.setAlpha(UIColors.gradient(this.index * 90), (int) (255.0f * alpha));
            matrixStack.method_22903();
            matrixStack.method_22904(interpX, interpY, interpZ);
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(interpRotX));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(interpRotY));
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(interpRotZ));
            if (glow) {
                renderGlow(matrixStack, halfSize * glowSize, color, alpha);
            }
            renderCube(matrixStack, halfSize, color);
            matrixStack.method_22909();
        }

        private void renderGlow(class_4587 matrixStack, float size, Color color, float alpha) {
            class_4184 camera = mc.field_1773.method_19418();
            matrixStack.method_22903();
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(-MathUtil.interpolate(this.prevRotZ, this.rotZ)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-MathUtil.interpolate(this.prevRotY, this.rotY)));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(-MathUtil.interpolate(this.prevRotX, this.rotX)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            Matrix4f glowMatrix = matrixStack.method_23760().method_23761();
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            int centerAlpha = (int) (80.0f * alpha);
            buf.method_22918(glowMatrix, 0.0f, 0.0f, 0.0f).method_1336(r, g, b, centerAlpha);
            for (int i = 0; i <= 16; i++) {
                float angle = (float) ((((double) (i * 2)) * 3.141592653589793d) / ((double) 16));
                float px = ((float) Math.cos(angle)) * size;
                float py = ((float) Math.sin(angle)) * size;
                buf.method_22918(glowMatrix, px, py, 0.0f).method_1336(r, g, b, 0);
            }
            class_286.method_43433(buf.method_60800());
            RenderSystem.enableCull();
            matrixStack.method_22909();
        }

        private void renderCube(class_4587 matrixStack, float size, Color color) {
            Matrix4f matrix = matrixStack.method_23760().method_23761();
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderSystem.lineWidth(2.0f);
            class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            int a = color.getAlpha();
            buf.method_22918(matrix, -size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, -size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, size, size).method_1336(r, g, b, a);
            buf.method_22918(matrix, -size, -size, -size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, size, size, size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, size, -size, -size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -size, size, size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -size, -size, size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, size, size, -size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, size, -size, size).method_1336(r, g, b, a / 2);
            buf.method_22918(matrix, -size, size, -size).method_1336(r, g, b, a / 2);
            class_286.method_43433(buf.method_60800());
            GL11.glDisable(2848);
            RenderSystem.enableCull();
        }
    }
}
