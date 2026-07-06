/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render.particles;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.client.features.modules.render.particles.CubeParticles;
import sweetie.leonware.client.features.modules.render.particles.HitParticles;
import sweetie.leonware.client.features.modules.render.particles.ParticleRender;

@ModuleRegister(name="Particles", category=Category.RENDER)
public class ParticlesModule
extends Module {
    private static final ParticlesModule instance = new ParticlesModule();
    private final HitParticles hitParticles = new HitParticles();
    private final CubeParticles worldParticles = new CubeParticles();
    private final BooleanSetting worldBoolean = new BooleanSetting("World").value(true).onAction(() -> {
        if (!((Boolean)this.getWorldBoolean().getValue()).booleanValue()) {
            this.worldParticles.toggle();
            this.worldParticles.removeAllEvents();
            this.removeEvents(this.worldParticles.getEventListeners());
        } else if (this.isEnabled()) {
            this.worldParticles.onEvent();
            this.addEvents(this.worldParticles.getEventListeners());
        }
    });
    private final BooleanSetting hitBoolean = new BooleanSetting("Hit").value(false).onAction(() -> {
        if (!((Boolean)this.getHitBoolean().getValue()).booleanValue()) {
            this.hitParticles.toggle();
            this.hitParticles.removeAllEvents();
            this.removeEvents(this.hitParticles.getEventListeners());
        } else if (this.isEnabled()) {
            this.hitParticles.onEvent();
            this.addEvents(this.hitParticles.getEventListeners());
        }
    });
    private final MultiBooleanSetting spawn = new MultiBooleanSetting("Spawn").value(this.worldBoolean, this.hitBoolean);

    public ParticlesModule() {
        this.addSettings(this.spawn);
        for (Setting<?> setting : this.worldParticles.getSettings()) {
            setting.setVisible(() -> this.spawn.isEnabled(this.worldBoolean.getName()));
        }
        this.addSettings(this.worldParticles.getSettings());
        for (Setting<?> setting : this.hitParticles.getSettings()) {
            setting.setVisible(() -> this.spawn.isEnabled(this.hitBoolean.getName()));
        }
        this.addSettings(this.hitParticles.getSettings());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (((Boolean)this.worldBoolean.getValue()).booleanValue()) {
            this.worldParticles.onEvent();
            this.addEvents(this.worldParticles.getEventListeners());
        }
        if (((Boolean)this.hitBoolean.getValue()).booleanValue()) {
            this.hitParticles.onEvent();
            this.addEvents(this.hitParticles.getEventListeners());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.worldParticles.toggle();
        this.worldParticles.removeAllEvents();
        this.hitParticles.toggle();
        this.hitParticles.removeAllEvents();
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static ParticlesModule getInstance() {
        return instance;
    }

    @Generated
    public BooleanSetting getWorldBoolean() {
        return this.worldBoolean;
    }

    @Generated
    public BooleanSetting getHitBoolean() {
        return this.hitBoolean;
    }

    public static class BaseSettings
    extends Configurable {
        private final ModeSetting textureMode;
        private final SliderSetting count;
        private final SliderSetting size;
        private final SliderSetting lifeTime;
        private final SliderSetting spawnDuration;
        private final SliderSetting dyingDuration;
        private final BooleanSetting rotate;
        private final BooleanSetting trail;
        private final SliderSetting trailLength;
        private final BooleanSetting dyingEffect;
        public final String prefix;

        public BaseSettings(String prefix) {
            this.prefix = prefix;
            this.textureMode = new ModeSetting(this.prefix + ": Texture").value("Spark").values(ParticleRender.textures);
            this.count = new SliderSetting(this.prefix + ": Count").value(Float.valueOf(25.0f)).range(10.0f, 100.0f).step(1.0f);
            this.size = new SliderSetting(this.prefix + ": Size").value(Float.valueOf(0.2f)).range(0.1f, 0.4f).step(0.05f);
            this.lifeTime = new SliderSetting(this.prefix + ": Life time").value(Float.valueOf(10.0f)).range(2.0f, 100.0f).step(1.0f);
            this.spawnDuration = new SliderSetting(this.prefix + ": Spawn duration").value(Float.valueOf(15.0f)).range(0.0f, 40.0f).step(1.0f);
            this.dyingDuration = new SliderSetting(this.prefix + ": Dying duration").value(Float.valueOf(15.0f)).range(0.0f, 40.0f).step(1.0f);
            this.rotate = new BooleanSetting(this.prefix + ": Rotate").value(true);
            this.trail = new BooleanSetting(this.prefix + ": Trail").value(false);
            this.trailLength = new SliderSetting(this.prefix + ": Trail length").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f);
            this.dyingEffect = new BooleanSetting(this.prefix + ": Dying effect").value(false);
            this.addSettings(this.textureMode, this.count, this.size, this.lifeTime, this.spawnDuration, this.dyingDuration, this.rotate, this.trail, this.trailLength, this.dyingEffect);
        }

        @Generated
        public ModeSetting textureMode() {
            return this.textureMode;
        }

        @Generated
        public SliderSetting count() {
            return this.count;
        }

        @Generated
        public SliderSetting size() {
            return this.size;
        }

        @Generated
        public SliderSetting lifeTime() {
            return this.lifeTime;
        }

        @Generated
        public SliderSetting spawnDuration() {
            return this.spawnDuration;
        }

        @Generated
        public SliderSetting dyingDuration() {
            return this.dyingDuration;
        }

        @Generated
        public BooleanSetting rotate() {
            return this.rotate;
        }

        @Generated
        public BooleanSetting trail() {
            return this.trail;
        }

        @Generated
        public SliderSetting trailLength() {
            return this.trailLength;
        }

        @Generated
        public BooleanSetting dyingEffect() {
            return this.dyingEffect;
        }

        @Generated
        public String prefix() {
            return this.prefix;
        }
    }
}

