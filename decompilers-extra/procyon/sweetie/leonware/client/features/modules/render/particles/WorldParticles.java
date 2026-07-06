// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.particles;

import net.minecraft.class_2960;
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
import sweetie.leonware.api.module.setting.SliderSetting;

public class WorldParticles extends ParticlesModule.BaseSettings
{
    private final SliderSetting distance;
    private final SliderSetting height;
    private final SliderSetting gravity;
    private final List<Particle> particles;
    private final TimerUtil timerUtil;
    
    public WorldParticles() {
        super("World");
        this.distance = new SliderSetting(this.prefix + "Distance").value(15.0f).range(5.0f, 50.0f).step(1.0f);
        this.height = new SliderSetting(this.prefix + "Height").value(8.0f).range(5.0f, 15.0f).step(1.0f);
        this.gravity = new SliderSetting(this.prefix + "Gravity").value(0.3f).range(0.1f, 1.0f).step(0.1f);
        this.particles = new ArrayList<Particle>();
        this.timerUtil = new TimerUtil();
        this.addSettings(this.distance, this.height, this.gravity);
    }
    
    public void toggle() {
        this.particles.clear();
        this.timerUtil.reset();
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            this.particles.removeIf(ParticleRender::update);
            final int diff = this.count().getValue().intValue() - this.particles.size();
            if (this.particles.size() <= this.count().getValue()) {
                final float d = this.distance.getValue();
                for (int i = 0; i < diff; ++i) {
                    this.particles.add(new Particle((float)(WorldParticles.mc.field_1724.method_23317() + MathUtil.randomInRange(-d, d)), (float)(WorldParticles.mc.field_1724.method_23318() + this.height.getValue()), (float)(WorldParticles.mc.field_1724.method_23321() + MathUtil.randomInRange(-d, d)), 0.0f, -MathUtil.randomInRange(this.gravity.getValue() * 0.1f, this.gravity.getValue()), 0.0f, this.particles.size(), ParticleRender.getTexture(this.textureMode().getValue()), this.size().getValue(), this.rotate().getValue(), this.lifeTime().getValue().intValue(), this.spawnDuration().getValue(), this.dyingDuration().getValue(), this.trail().getValue(), this.trailLength().getValue().intValue()));
                }
            }
            return;
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            final class_4587 matrixStack = event.matrixStack();
            this.particles.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Particle particle = iterator.next();
                particle.updateAlpha();
                RenderUtil.WORLD.startRender(matrixStack);
                particle.renderTrail(matrixStack);
                RenderUtil.WORLD.endRender(matrixStack);
                RenderUtil.WORLD.startRender(matrixStack);
                particle.render(matrixStack);
                RenderUtil.WORLD.endRender(matrixStack);
            }
            return;
        }));
        this.addEvents(renderEvent, updateEvent);
    }
    
    private class Particle extends ParticleRender
    {
        public Particle(final WorldParticles worldParticles, final float x, final float y, final float z, final float motionX, final float motionY, final float motionZ, final int index, final class_2960 identifier, final float size, final boolean rotate, final int lifetime, final float sp, final float dy, final boolean trail, final int trailLength) {
            super(x, y, z, lifetime);
            super.gravityFalls(true).dropPhysics(true).size(size).index(index).motionX(motionX).motionY(motionY).motionZ(motionZ).identifier(identifier).rotating(rotate).spawnDuration(sp).dyingDuration(dy).trail(trail).trailLength((float)trailLength);
        }
    }
}
