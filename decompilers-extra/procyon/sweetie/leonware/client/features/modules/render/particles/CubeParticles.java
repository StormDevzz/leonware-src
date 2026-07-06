// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.particles;

import org.lwjgl.opengl.GL11;
import net.minecraft.class_287;
import org.joml.Matrix4f;
import net.minecraft.class_4184;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import java.awt.Color;
import net.minecraft.class_7833;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import net.minecraft.class_2338;
import java.util.Iterator;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import sweetie.leonware.api.utils.math.TimerUtil;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class CubeParticles extends ParticlesModule.BaseSettings implements QuickImports
{
    private final SliderSetting distance;
    private final SliderSetting riseSpeed;
    private final SliderSetting rotateSpeed;
    private final BooleanSetting glow;
    private final SliderSetting glowSize;
    private final List<CubeParticle> particles;
    private final TimerUtil spawnTimer;
    
    public CubeParticles() {
        super("Cubes");
        this.distance = new SliderSetting(this.prefix + "Distance").value(20.0f).range(5.0f, 50.0f).step(1.0f);
        this.riseSpeed = new SliderSetting(this.prefix + "Rise Speed").value(0.03f).range(0.01f, 0.1f).step(0.01f);
        this.rotateSpeed = new SliderSetting(this.prefix + "Rotate Speed").value(2.0f).range(0.5f, 5.0f).step(0.5f);
        this.glow = new BooleanSetting(this.prefix + "Glow").value(true);
        this.glowSize = new SliderSetting(this.prefix + "Glow Size").value(2.0f).range(1.0f, 4.0f).step(0.5f);
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            this.particles.removeIf(CubeParticle::update);
            final int diff = this.count().getValue().intValue() - this.particles.size();
            if (diff > 0) {
                final float d = this.distance.getValue();
                for (int toSpawn = Math.min(diff, 5), i = 0; i < toSpawn; ++i) {
                    final double spawnX = CubeParticles.mc.field_1724.method_23317() + MathUtil.randomInRange(-d, d);
                    final double spawnZ = CubeParticles.mc.field_1724.method_23321() + MathUtil.randomInRange(-d, d);
                    final double spawnY = this.findGroundY(spawnX, spawnZ);
                    this.particles.add(new CubeParticle((float)spawnX, (float)spawnY, (float)spawnZ, MathUtil.randomInRange(-0.005f, 0.005f), this.riseSpeed.getValue(), MathUtil.randomInRange(-0.005f, 0.005f), this.particles.size(), this.size().getValue(), this.lifeTime().getValue().intValue(), this.spawnDuration().getValue(), this.dyingDuration().getValue(), this.rotateSpeed.getValue()));
                }
            }
            return;
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            final class_4587 matrixStack = event.matrixStack();
            this.particles.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final CubeParticle particle = iterator.next();
                particle.updateAlpha();
                RenderUtil.WORLD.startRender(matrixStack);
                particle.render(matrixStack, this.glow.getValue(), this.glowSize.getValue());
                RenderUtil.WORLD.endRender(matrixStack);
            }
            return;
        }));
        this.addEvents(renderEvent, updateEvent);
    }
    
    private double findGroundY(final double x, final double z) {
        if (CubeParticles.mc.field_1687 == null || CubeParticles.mc.field_1724 == null) {
            return CubeParticles.mc.field_1724.method_23318() - 1.0;
        }
        for (int playerY = (int)CubeParticles.mc.field_1724.method_23318(), y = playerY + 2; y > playerY - 15; --y) {
            final class_2338 pos = new class_2338((int)Math.floor(x), y, (int)Math.floor(z));
            final class_2338 above = pos.method_10084();
            final boolean blockSolid = !CubeParticles.mc.field_1687.method_8320(pos).method_26215();
            final boolean aboveAir = CubeParticles.mc.field_1687.method_8320(above).method_26215();
            if (blockSolid && aboveAir) {
                return y + 1.0;
            }
        }
        return CubeParticles.mc.field_1724.method_23318();
    }
    
    private static class CubeParticle implements QuickImports
    {
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
        private float prevSize;
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
        private final TimerUtil timerUtil;
        private final AnimationUtil alphaAnimation;
        
        public CubeParticle(final float x, final float y, final float z, final float motionX, final float motionY, final float motionZ, final int index, final float size, final int lifetime, final float spawnDuration, final float dyingDuration, final float rotateSpeed) {
            this.prevSize = 0.0f;
            this.timerUtil = new TimerUtil();
            this.alphaAnimation = new AnimationUtil();
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
            return CubeParticle.mc.field_1724.method_19538().method_1022(new class_243((double)this.x, (double)this.y, (double)this.z)) >= 80.0 || (this.alphaAnimation.getValue() <= 0.0 && this.timerUtil.finished((this.spawnDuration + this.dyingDuration + this.maxLife) * 50.0f));
        }
        
        public void updateAlpha() {
            this.alphaAnimation.update();
            final float alphaAnim = (float)this.alphaAnimation.getValue();
            if (alphaAnim <= 0.0 && !this.timerUtil.finished(this.spawnDuration * 50.0f)) {
                this.alphaAnimation.run(1.0, (long)(this.spawnDuration * 50.0f), Easing.QUINT_OUT);
            }
            if (alphaAnim >= 1.0 && this.timerUtil.finished((this.spawnDuration + this.maxLife) * 50.0f)) {
                this.alphaAnimation.run(0.0, (long)(this.dyingDuration * 50.0f), Easing.QUINT_OUT);
            }
        }
        
        public void render(final class_4587 matrixStack, final boolean glow, final float glowSize) {
            final class_243 cam = CubeParticle.mc.method_1561().field_4686.method_19326();
            final float alpha = (float)this.alphaAnimation.getValue();
            final double interpX = MathUtil.interpolate(this.prevX, this.x) - cam.field_1352;
            final double interpY = MathUtil.interpolate(this.prevY, this.y) - cam.field_1351;
            final double interpZ = MathUtil.interpolate(this.prevZ, this.z) - cam.field_1350;
            final float interpRotX = MathUtil.interpolate(this.prevRotX, this.rotX);
            final float interpRotY = MathUtil.interpolate(this.prevRotY, this.rotY);
            final float interpRotZ = MathUtil.interpolate(this.prevRotZ, this.rotZ);
            final float halfSize = MathUtil.interpolate(this.prevSize, this.size * alpha);
            this.prevSize = halfSize;
            final Color color = ColorUtil.setAlpha(UIColors.gradient(this.index * 90), (int)(255.0f * alpha));
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
        
        private void renderGlow(final class_4587 matrixStack, final float size, final Color color, final float alpha) {
            final class_4184 camera = CubeParticle.mc.field_1773.method_19418();
            matrixStack.method_22903();
            matrixStack.method_22907(class_7833.field_40718.rotationDegrees(-MathUtil.interpolate(this.prevRotZ, this.rotZ)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-MathUtil.interpolate(this.prevRotY, this.rotY)));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(-MathUtil.interpolate(this.prevRotX, this.rotX)));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            final Matrix4f glowMatrix = matrixStack.method_23760().method_23761();
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            final class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
            final int r = color.getRed();
            final int g = color.getGreen();
            final int b = color.getBlue();
            final int centerAlpha = (int)(80.0f * alpha);
            final int edgeAlpha = 0;
            buf.method_22918(glowMatrix, 0.0f, 0.0f, 0.0f).method_1336(r, g, b, centerAlpha);
            for (int segments = 16, i = 0; i <= segments; ++i) {
                final float angle = (float)(i * 2 * 3.141592653589793 / segments);
                final float px = (float)Math.cos(angle) * size;
                final float py = (float)Math.sin(angle) * size;
                buf.method_22918(glowMatrix, px, py, 0.0f).method_1336(r, g, b, edgeAlpha);
            }
            class_286.method_43433(buf.method_60800());
            RenderSystem.enableCull();
            matrixStack.method_22909();
        }
        
        private void renderCube(final class_4587 matrixStack, final float size, final Color color) {
            final Matrix4f matrix = matrixStack.method_23760().method_23761();
            final float s = size;
            RenderSystem.setShader(class_10142.field_53876);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderSystem.lineWidth(2.0f);
            final class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            final int r = color.getRed();
            final int g = color.getGreen();
            final int b = color.getBlue();
            final int a = color.getAlpha();
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
            class_286.method_43433(buf.method_60800());
            GL11.glDisable(2848);
            RenderSystem.enableCull();
        }
    }
}
