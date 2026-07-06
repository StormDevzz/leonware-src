// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.particles;

import net.minecraft.class_2960;
import net.minecraft.class_1297;
import java.util.Iterator;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;

public class HitParticles extends ParticlesModule.BaseSettings
{
    private final ModeSetting physics;
    private final SliderSetting motion;
    private final List<Particle> particles;
    
    public HitParticles() {
        super("Hit");
        this.physics = new ModeSetting(this.prefix + "Physics").value("Drop").values("Fly", "Drop");
        this.motion = new SliderSetting(this.prefix + "Motion").value(0.3f).range(0.1f, 1.0f).step(0.1f);
        this.particles = new ArrayList<Particle>();
        this.addSettings(this.physics, this.motion);
    }
    
    public void toggle() {
        this.particles.clear();
        this.removeAllEvents();
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.particles.removeIf(ParticleRender::update)));
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
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            final class_1297 patt0$temp = event.entity();
            if (patt0$temp instanceof final class_1309 entity) {
                final float m = this.motion.getValue();
                for (int i = 0; i < this.count().getValue(); ++i) {
                    final List<Particle> particles = this.particles;
                    new Particle((float)(entity.method_23317() + MathUtil.randomInRange(-0.1f, 0.1f)), (float)(entity.method_23318() + entity.method_17682() / 2.0f + MathUtil.randomInRange(-0.1f, 0.1f)), (float)(entity.method_23321() + MathUtil.randomInRange(-0.1f, 0.1f)), MathUtil.randomInRange(-m, m), MathUtil.randomInRange(-m, m), MathUtil.randomInRange(-m, m), this.particles.size(), ParticleRender.getTexture(this.textureMode().getValue()), this.size().getValue(), !this.physics.is("Fly"), this.rotate().getValue(), this.lifeTime().getValue().intValue(), this.spawnDuration().getValue(), this.dyingDuration().getValue(), this.trail().getValue(), this.trailLength().getValue().intValue());
                    final Particle particle2;
                    particles.add(particle2);
                }
            }
            return;
        }));
        this.addEvents(renderEvent, attackEvent, updateEvent);
    }
    
    private class Particle extends ParticleRender
    {
        public Particle(final HitParticles hitParticles, final float x, final float y, final float z, final float motionX, final float motionY, final float motionZ, final int index, final class_2960 identifier, final float size, final boolean drop, final boolean rotate, final int lifetime, final float sp, final float dy, final boolean trail, final int trailLength) {
            super(x, y, z, lifetime);
            super.gravityFalls(false).dropPhysics(drop).size(size).index(index).motionX(motionX).motionY(motionY).motionZ(motionZ).identifier(identifier).rotating(rotate).spawnDuration(sp).dyingDuration(dy).trail(trail).trailLength((float)trailLength);
        }
    }
}
