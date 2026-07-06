package sweetie.leonware.client.features.modules.render.particles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.particles.ParticlesModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/HitParticles.class */
public class HitParticles extends ParticlesModule.BaseSettings {
    private final ModeSetting physics;
    private final SliderSetting motion;
    private final List<Particle> particles;

    public HitParticles() {
        super("Hit");
        this.physics = new ModeSetting(this.prefix + "Physics").value("Drop").values("Fly", "Drop");
        this.motion = new SliderSetting(this.prefix + "Motion").value(Float.valueOf(0.3f)).range(0.1f, 1.0f).step(0.1f);
        this.particles = new ArrayList();
        addSettings(this.physics, this.motion);
    }

    public void toggle() {
        this.particles.clear();
        removeAllEvents();
    }

    @Override // sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.particles.removeIf((v0) -> {
                return v0.update();
            });
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            class_4587 matrixStack = event2.matrixStack();
            for (Particle particle : this.particles) {
                particle.updateAlpha();
                RenderUtil.WORLD.startRender(matrixStack);
                particle.renderTrail(matrixStack);
                RenderUtil.WORLD.endRender(matrixStack);
                RenderUtil.WORLD.startRender(matrixStack);
                particle.render(matrixStack);
                RenderUtil.WORLD.endRender(matrixStack);
            }
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event3 -> {
            class_1309 class_1309VarEntity = event3.entity();
            if (class_1309VarEntity instanceof class_1309) {
                class_1309 entity = class_1309VarEntity;
                float m = this.motion.getValue().floatValue();
                for (int i = 0; i < count().getValue().floatValue(); i++) {
                    this.particles.add(new Particle(this, (float) (entity.method_23317() + ((double) MathUtil.randomInRange(-0.1f, 0.1f))), (float) (entity.method_23318() + ((double) (entity.method_17682() / 2.0f)) + ((double) MathUtil.randomInRange(-0.1f, 0.1f))), (float) (entity.method_23321() + ((double) MathUtil.randomInRange(-0.1f, 0.1f))), MathUtil.randomInRange(-m, m), MathUtil.randomInRange(-m, m), MathUtil.randomInRange(-m, m), this.particles.size(), ParticleRender.getTexture(textureMode().getValue()), size().getValue().floatValue(), !this.physics.is("Fly"), rotate().getValue().booleanValue(), lifeTime().getValue().intValue(), spawnDuration().getValue().floatValue(), dyingDuration().getValue().floatValue(), trail().getValue().booleanValue(), trailLength().getValue().intValue()));
                }
            }
        }));
        addEvents(renderEvent, attackEvent, updateEvent);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/HitParticles$Particle.class */
    private class Particle extends ParticleRender {
        public Particle(HitParticles hitParticles, float x, float y, float z, float motionX, float motionY, float motionZ, int index, class_2960 identifier, float size, boolean drop, boolean rotate, int lifetime, float sp, float dy, boolean trail, int trailLength) {
            super(x, y, z, lifetime);
            super.gravityFalls(false).dropPhysics(drop).size(size).index(index).motionX(motionX).motionY(motionY).motionZ(motionZ).identifier(identifier).rotating(rotate).spawnDuration(sp).dyingDuration(dy).trail(trail).trailLength(trailLength);
        }
    }
}
