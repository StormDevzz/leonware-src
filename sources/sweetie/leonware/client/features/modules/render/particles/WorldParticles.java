package sweetie.leonware.client.features.modules.render.particles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.features.modules.render.particles.ParticlesModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/WorldParticles.class */
public class WorldParticles extends ParticlesModule.BaseSettings {
    private final SliderSetting distance;
    private final SliderSetting height;
    private final SliderSetting gravity;
    private final List<Particle> particles;
    private final TimerUtil timerUtil;

    public WorldParticles() {
        super("World");
        this.distance = new SliderSetting(this.prefix + "Distance").value(Float.valueOf(15.0f)).range(5.0f, 50.0f).step(1.0f);
        this.height = new SliderSetting(this.prefix + "Height").value(Float.valueOf(8.0f)).range(5.0f, 15.0f).step(1.0f);
        this.gravity = new SliderSetting(this.prefix + "Gravity").value(Float.valueOf(0.3f)).range(0.1f, 1.0f).step(0.1f);
        this.particles = new ArrayList();
        this.timerUtil = new TimerUtil();
        addSettings(this.distance, this.height, this.gravity);
    }

    public void toggle() {
        this.particles.clear();
        this.timerUtil.reset();
    }

    @Override // sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.particles.removeIf((v0) -> {
                return v0.update();
            });
            int diff = count().getValue().intValue() - this.particles.size();
            if (this.particles.size() <= count().getValue().floatValue()) {
                float d = this.distance.getValue().floatValue();
                for (int i = 0; i < diff; i++) {
                    this.particles.add(new Particle(this, (float) (mc.field_1724.method_23317() + ((double) MathUtil.randomInRange(-d, d))), (float) (mc.field_1724.method_23318() + ((double) this.height.getValue().floatValue())), (float) (mc.field_1724.method_23321() + ((double) MathUtil.randomInRange(-d, d))), 0.0f, -MathUtil.randomInRange(this.gravity.getValue().floatValue() * 0.1f, this.gravity.getValue().floatValue()), 0.0f, this.particles.size(), ParticleRender.getTexture(textureMode().getValue()), size().getValue().floatValue(), rotate().getValue().booleanValue(), lifeTime().getValue().intValue(), spawnDuration().getValue().floatValue(), dyingDuration().getValue().floatValue(), trail().getValue().booleanValue(), trailLength().getValue().intValue()));
                }
            }
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
        addEvents(renderEvent, updateEvent);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/WorldParticles$Particle.class */
    private class Particle extends ParticleRender {
        public Particle(WorldParticles worldParticles, float x, float y, float z, float motionX, float motionY, float motionZ, int index, class_2960 identifier, float size, boolean rotate, int lifetime, float sp, float dy, boolean trail, int trailLength) {
            super(x, y, z, lifetime);
            super.gravityFalls(true).dropPhysics(true).size(size).index(index).motionX(motionX).motionY(motionY).motionZ(motionZ).identifier(identifier).rotating(rotate).spawnDuration(sp).dyingDuration(dy).trail(trail).trailLength(trailLength);
        }
    }
}
