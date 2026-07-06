/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_2189
 *  net.minecraft.class_2231
 *  net.minecraft.class_2248
 *  net.minecraft.class_2269
 *  net.minecraft.class_2338
 *  net.minecraft.class_2401
 *  net.minecraft.class_2404
 *  net.minecraft.class_243
 *  net.minecraft.class_2527
 *  net.minecraft.class_2577
 *  net.minecraft.class_2680
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_5498
 *  net.minecraft.class_7833
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2189;
import net.minecraft.class_2231;
import net.minecraft.class_2248;
import net.minecraft.class_2269;
import net.minecraft.class_2338;
import net.minecraft.class_2401;
import net.minecraft.class_2404;
import net.minecraft.class_243;
import net.minecraft.class_2527;
import net.minecraft.class_2577;
import net.minecraft.class_2680;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_5498;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;

@ModuleRegister(name="Trails", category=Category.RENDER)
public class TrailsModule
extends Module {
    private static final TrailsModule instance = new TrailsModule();
    private final SliderSetting length = new SliderSetting("Length").value(Float.valueOf(1500.0f)).range(500.0f, 3000.0f).step(100.0f);
    private final SliderSetting size = new SliderSetting("Size").value(Float.valueOf(0.2f)).range(0.05f, 0.3f).step(0.01f);
    private final BooleanSetting renderInFirstPerson = new BooleanSetting("In first person").value(false);
    private final BooleanSetting physics = new BooleanSetting("Physics").value(true);
    private final SliderSetting fadeTime = new SliderSetting("Fade Time").value(Float.valueOf(250.0f)).range(100.0f, 1000.0f).step(50.0f);
    private final List<TrailParticle> particles = new ArrayList<TrailParticle>();
    private final class_2960 bloomTexture = FileUtil.getImage("particles/glow");

    public TrailsModule() {
        this.addSettings(this.length, this.size, this.renderInFirstPerson, this.physics, this.fadeTime);
    }

    @Override
    public void onEnable() {
        this.particles.clear();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            boolean isFirstPerson;
            this.particles.removeIf(particle -> particle.shouldRemove(((Float)this.length.getValue()).intValue()));
            boolean bl = isFirstPerson = TrailsModule.mc.field_1690.method_31044() == class_5498.field_26664;
            if (isFirstPerson && !((Boolean)this.renderInFirstPerson.getValue()).booleanValue()) {
                return;
            }
            class_243 playerPos = TrailsModule.mc.field_1724.method_19538();
            this.particles.add(new TrailParticle(new class_243(playerPos.field_1352, playerPos.field_1351 + (double)TrailsModule.mc.field_1724.method_17682() * (isFirstPerson ? 0.2 : 0.5), playerPos.field_1350), this.particles.size()));
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            class_4587 matrixStack = event.matrixStack();
            this.drawTrail(matrixStack, (Boolean)this.physics.getValue(), ((Float)this.size.getValue()).floatValue(), ((Float)this.fadeTime.getValue()).intValue(), ((Float)this.length.getValue()).intValue(), this.particles);
        }));
        this.addEvents(updateEvent, renderEvent);
    }

    private void drawTrail(class_4587 matrixStack, boolean physics, float size, int fadeTime, int length, List<TrailParticle> particles) {
        int index = 0;
        for (TrailParticle particle : particles) {
            particle.update(physics);
            if (index > 0) {
                TrailParticle prevParticle = particles.get(index - 1);
                class_243 prevPos = prevParticle.getPosition();
                class_243 currentPos = particle.getPosition();
                float smoothFactor = 0.2f;
                class_243 smoothedPos = new class_243(MathUtil.interpolate(prevPos.field_1352, currentPos.field_1352, (double)smoothFactor), MathUtil.interpolate(prevPos.field_1351, currentPos.field_1351, (double)smoothFactor), MathUtil.interpolate(prevPos.field_1350, currentPos.field_1350, (double)smoothFactor));
                prevParticle.setPosition(smoothedPos);
            }
            RenderUtil.WORLD.startRender(matrixStack);
            this.renderParticle(matrixStack, particle, size, fadeTime, length, particles);
            RenderUtil.WORLD.endRender(matrixStack);
            ++index;
        }
    }

    private void renderParticle(class_4587 matrixStack, TrailParticle particle, float size, int fadeTime, int length, List<TrailParticle> particles) {
        particle.handleAlphaTransitions(fadeTime, length);
        Color color = ColorUtil.setAlpha(UIColors.gradient(particle.getIndex() * 30), (int)particle.getAlpha());
        class_243 pos = particle.getPosition();
        float bloomSize = size;
        if (particles.indexOf(particle) > 0) {
            TrailParticle prev = particles.get(particles.indexOf(particle) - 1);
            double distance = pos.method_1022(prev.getPosition());
            bloomSize = (float)Math.max((double)size, Math.min((double)size * 3.3, distance * 4.0));
        }
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        class_4184 gameRendererCamera = TrailsModule.mc.field_1773.method_19418();
        class_243 renderCamera = TrailsModule.mc.method_1561().field_4686.method_19326();
        RenderSystem.setShaderTexture((int)0, (class_2960)this.bloomTexture);
        matrixStack.method_22904(pos.field_1352 - renderCamera.field_1352, pos.field_1351 - renderCamera.field_1351, pos.field_1350 - renderCamera.field_1350);
        matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-gameRendererCamera.method_19330()));
        matrixStack.method_22907(class_7833.field_40714.rotationDegrees(gameRendererCamera.method_19329()));
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        bufferBuilder.method_22918(matrix, bloomSize, -bloomSize, 0.0f).method_22913(0.0f, 1.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, -bloomSize, -bloomSize, 0.0f).method_22913(1.0f, 1.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, -bloomSize, bloomSize, 0.0f).method_22913(1.0f, 0.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, bloomSize, bloomSize, 0.0f).method_22913(0.0f, 0.0f).method_39415(color.getRGB());
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
    }

    @Generated
    public static TrailsModule getInstance() {
        return instance;
    }

    public static class TrailParticle {
        private class_243 position;
        private class_243 velocity;
        private final int index;
        private final TimerUtil timer = new TimerUtil();
        private final AnimationUtil alphaAnimation = new AnimationUtil();
        private float alpha = 255.0f;

        public TrailParticle(class_243 position, int index) {
            this.position = position;
            this.velocity = new class_243(MathUtil.randomInRange(-0.01, 0.01), MathUtil.randomInRange(-0.01, 0.01), MathUtil.randomInRange(-0.01, 0.01));
            this.index = index;
        }

        public void handleAlphaTransitions(int fadeTime, int maxLife) {
            this.alphaAnimation.update();
            float currentAlpha = (float)this.alphaAnimation.getValue();
            if ((double)currentAlpha <= 0.0 && !this.timer.finished(fadeTime)) {
                this.alphaAnimation.run(255.0, (long)fadeTime, Easing.LINEAR);
            }
            if ((double)currentAlpha >= 255.0 && this.timer.finished(maxLife - fadeTime)) {
                this.alphaAnimation.run(0.0, (long)fadeTime, Easing.LINEAR);
            }
            this.alpha = (float)this.alphaAnimation.getValue();
        }

        public boolean shouldRemove(int maxLife) {
            double distance = this.position.method_1022(QuickImports.mc.field_1724.method_19538());
            boolean expired = this.timer.finished(maxLife) && (double)this.alpha <= 0.0;
            return distance >= 80.0 || expired;
        }

        public void update(boolean enablePhysics) {
            if (enablePhysics) {
                this.applyPhysics();
            } else {
                this.updateWithoutPhysics();
            }
        }

        private void applyPhysics() {
            if (this.isSolidBlock(this.position.field_1352, this.position.field_1351, this.position.field_1350 + this.velocity.field_1350)) {
                this.velocity = new class_243(this.velocity.field_1352, this.velocity.field_1351, -this.velocity.field_1350 * 0.8);
            }
            if (this.isSolidBlock(this.position.field_1352, this.position.field_1351 + this.velocity.field_1351, this.position.field_1350)) {
                this.velocity = new class_243(this.velocity.field_1352 * 0.999, -this.velocity.field_1351 * 0.7, this.velocity.field_1350 * 0.999);
            }
            if (this.isSolidBlock(this.position.field_1352 + this.velocity.field_1352, this.position.field_1351, this.position.field_1350)) {
                this.velocity = new class_243(-this.velocity.field_1352 * 0.8, this.velocity.field_1351, this.velocity.field_1350);
            }
            this.updateWithoutPhysics();
        }

        private void updateWithoutPhysics() {
            this.position = this.position.method_1019(this.velocity);
            this.velocity = this.velocity.method_1021(0.999);
        }

        private boolean isSolidBlock(double x, double y, double z) {
            class_2338 pos = class_2338.method_49637((double)x, (double)y, (double)z);
            class_2680 state = QuickImports.mc.field_1687.method_8320(pos);
            class_2248 block = state.method_26204();
            return this.isValidBlock(block);
        }

        private boolean isValidBlock(class_2248 block) {
            return !(block instanceof class_2189) && !(block instanceof class_2269) && !(block instanceof class_2527) && !(block instanceof class_2401) && !(block instanceof class_2231) && !(block instanceof class_2577) && !(block instanceof class_2404);
        }

        @Generated
        public class_243 getPosition() {
            return this.position;
        }

        @Generated
        public class_243 getVelocity() {
            return this.velocity;
        }

        @Generated
        public int getIndex() {
            return this.index;
        }

        @Generated
        public TimerUtil getTimer() {
            return this.timer;
        }

        @Generated
        public AnimationUtil getAlphaAnimation() {
            return this.alphaAnimation;
        }

        @Generated
        public float getAlpha() {
            return this.alpha;
        }

        @Generated
        public void setPosition(class_243 position) {
            this.position = position;
        }

        @Generated
        public void setVelocity(class_243 velocity) {
            this.velocity = velocity;
        }

        @Generated
        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }
    }
}

