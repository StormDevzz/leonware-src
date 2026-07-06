// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import net.minecraft.class_2404;
import net.minecraft.class_2577;
import net.minecraft.class_2231;
import net.minecraft.class_2401;
import net.minecraft.class_2527;
import net.minecraft.class_2269;
import net.minecraft.class_2189;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2338;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import lombok.Generated;
import net.minecraft.class_287;
import net.minecraft.class_4184;
import org.joml.Matrix4f;
import java.awt.Color;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_7833;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import java.util.Iterator;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_243;
import net.minecraft.class_5498;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.files.FileUtil;
import java.util.ArrayList;
import net.minecraft.class_2960;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Trails", category = Category.RENDER)
public class TrailsModule extends Module
{
    private static final TrailsModule instance;
    private final SliderSetting length;
    private final SliderSetting size;
    private final BooleanSetting renderInFirstPerson;
    private final BooleanSetting physics;
    private final SliderSetting fadeTime;
    private final List<TrailParticle> particles;
    private final class_2960 bloomTexture;
    
    public TrailsModule() {
        this.length = new SliderSetting("Length").value(1500.0f).range(500.0f, 3000.0f).step(100.0f);
        this.size = new SliderSetting("Size").value(0.2f).range(0.05f, 0.3f).step(0.01f);
        this.renderInFirstPerson = new BooleanSetting("In first person").value(false);
        this.physics = new BooleanSetting("Physics").value(true);
        this.fadeTime = new SliderSetting("Fade Time").value(250.0f).range(100.0f, 1000.0f).step(50.0f);
        this.particles = new ArrayList<TrailParticle>();
        this.bloomTexture = FileUtil.getImage("particles/glow");
        this.addSettings(this.length, this.size, this.renderInFirstPerson, this.physics, this.fadeTime);
    }
    
    @Override
    public void onEnable() {
        this.particles.clear();
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            this.particles.removeIf(particle -> particle.shouldRemove(this.length.getValue().intValue()));
            final boolean isFirstPerson = TrailsModule.mc.field_1690.method_31044() == class_5498.field_26664;
            if (isFirstPerson && !this.renderInFirstPerson.getValue()) {
                return;
            }
            else {
                final class_243 playerPos = TrailsModule.mc.field_1724.method_19538();
                final List<TrailParticle> particles = this.particles;
                new(sweetie.leonware.client.features.modules.render.TrailsModule.TrailParticle.class)();
                new class_243(playerPos.field_1352, playerPos.field_1351 + TrailsModule.mc.field_1724.method_17682() * (isFirstPerson ? 0.2 : 0.5), playerPos.field_1350);
                final class_243 position;
                new TrailParticle(position, this.particles.size());
                final TrailParticle trailParticle;
                particles.add(trailParticle);
                return;
            }
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            final class_4587 matrixStack = event.matrixStack();
            this.drawTrail(matrixStack, this.physics.getValue(), this.size.getValue(), this.fadeTime.getValue().intValue(), this.length.getValue().intValue(), this.particles);
            return;
        }));
        this.addEvents(updateEvent, renderEvent);
    }
    
    private void drawTrail(final class_4587 matrixStack, final boolean physics, final float size, final int fadeTime, final int length, final List<TrailParticle> particles) {
        int index = 0;
        for (final TrailParticle particle : particles) {
            particle.update(physics);
            if (index > 0) {
                final TrailParticle prevParticle = particles.get(index - 1);
                final class_243 prevPos = prevParticle.getPosition();
                final class_243 currentPos = particle.getPosition();
                final float smoothFactor = 0.2f;
                final class_243 smoothedPos = new class_243(MathUtil.interpolate(prevPos.field_1352, currentPos.field_1352, smoothFactor), MathUtil.interpolate(prevPos.field_1351, currentPos.field_1351, smoothFactor), MathUtil.interpolate(prevPos.field_1350, currentPos.field_1350, smoothFactor));
                prevParticle.setPosition(smoothedPos);
            }
            RenderUtil.WORLD.startRender(matrixStack);
            this.renderParticle(matrixStack, particle, size, fadeTime, length, particles);
            RenderUtil.WORLD.endRender(matrixStack);
            ++index;
        }
    }
    
    private void renderParticle(final class_4587 matrixStack, final TrailParticle particle, final float size, final int fadeTime, final int length, final List<TrailParticle> particles) {
        particle.handleAlphaTransitions(fadeTime, length);
        final Color color = ColorUtil.setAlpha(UIColors.gradient(particle.getIndex() * 30), (int)particle.getAlpha());
        final class_243 pos = particle.getPosition();
        float bloomSize = size;
        if (particles.indexOf(particle) > 0) {
            final TrailParticle prev = particles.get(particles.indexOf(particle) - 1);
            final double distance = pos.method_1022(prev.getPosition());
            bloomSize = (float)Math.max(size, Math.min(size * 3.3, distance * 4.0));
        }
        final Matrix4f matrix = matrixStack.method_23760().method_23761();
        final class_4184 gameRendererCamera = TrailsModule.mc.field_1773.method_19418();
        final class_243 renderCamera = TrailsModule.mc.method_1561().field_4686.method_19326();
        RenderSystem.setShaderTexture(0, this.bloomTexture);
        matrixStack.method_22904(pos.field_1352 - renderCamera.field_1352, pos.field_1351 - renderCamera.field_1351, pos.field_1350 - renderCamera.field_1350);
        matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-gameRendererCamera.method_19330()));
        matrixStack.method_22907(class_7833.field_40714.rotationDegrees(gameRendererCamera.method_19329()));
        final class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        bufferBuilder.method_22918(matrix, bloomSize, -bloomSize, 0.0f).method_22913(0.0f, 1.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, -bloomSize, -bloomSize, 0.0f).method_22913(1.0f, 1.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, -bloomSize, bloomSize, 0.0f).method_22913(1.0f, 0.0f).method_39415(color.getRGB());
        bufferBuilder.method_22918(matrix, bloomSize, bloomSize, 0.0f).method_22913(0.0f, 0.0f).method_39415(color.getRGB());
        class_286.method_43433(bufferBuilder.method_60800());
    }
    
    @Generated
    public static TrailsModule getInstance() {
        return TrailsModule.instance;
    }
    
    static {
        instance = new TrailsModule();
    }
    
    public static class TrailParticle
    {
        private class_243 position;
        private class_243 velocity;
        private final int index;
        private final TimerUtil timer;
        private final AnimationUtil alphaAnimation;
        private float alpha;
        
        public TrailParticle(final class_243 position, final int index) {
            this.timer = new TimerUtil();
            this.alphaAnimation = new AnimationUtil();
            this.alpha = 255.0f;
            this.position = position;
            this.velocity = new class_243(MathUtil.randomInRange(-0.01, 0.01), MathUtil.randomInRange(-0.01, 0.01), MathUtil.randomInRange(-0.01, 0.01));
            this.index = index;
        }
        
        public void handleAlphaTransitions(final int fadeTime, final int maxLife) {
            this.alphaAnimation.update();
            final float currentAlpha = (float)this.alphaAnimation.getValue();
            if (currentAlpha <= 0.0 && !this.timer.finished(fadeTime)) {
                this.alphaAnimation.run(255.0, fadeTime, Easing.LINEAR);
            }
            if (currentAlpha >= 255.0 && this.timer.finished(maxLife - fadeTime)) {
                this.alphaAnimation.run(0.0, fadeTime, Easing.LINEAR);
            }
            this.alpha = (float)this.alphaAnimation.getValue();
        }
        
        public boolean shouldRemove(final int maxLife) {
            final double distance = this.position.method_1022(QuickImports.mc.field_1724.method_19538());
            final boolean expired = this.timer.finished(maxLife) && this.alpha <= 0.0;
            return distance >= 80.0 || expired;
        }
        
        public void update(final boolean enablePhysics) {
            if (enablePhysics) {
                this.applyPhysics();
            }
            else {
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
        
        private boolean isSolidBlock(final double x, final double y, final double z) {
            final class_2338 pos = class_2338.method_49637(x, y, z);
            final class_2680 state = QuickImports.mc.field_1687.method_8320(pos);
            final class_2248 block = state.method_26204();
            return this.isValidBlock(block);
        }
        
        private boolean isValidBlock(final class_2248 block) {
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
        public void setPosition(final class_243 position) {
            this.position = position;
        }
        
        @Generated
        public void setVelocity(final class_243 velocity) {
            this.velocity = velocity;
        }
        
        @Generated
        public void setAlpha(final float alpha) {
            this.alpha = alpha;
        }
    }
}
