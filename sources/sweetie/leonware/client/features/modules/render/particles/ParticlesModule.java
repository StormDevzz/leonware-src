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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/ParticlesModule.class */
@ModuleRegister(name = "Particles", category = Category.RENDER)
public class ParticlesModule extends Module {
    private static final ParticlesModule instance = new ParticlesModule();
    private final HitParticles hitParticles = new HitParticles();
    private final CubeParticles worldParticles = new CubeParticles();
    private final BooleanSetting worldBoolean = new BooleanSetting("World").value((Boolean) true).onAction2(() -> {
        if (!getWorldBoolean().getValue().booleanValue()) {
            this.worldParticles.toggle();
            this.worldParticles.removeAllEvents();
            removeEvents(this.worldParticles.getEventListeners());
        } else if (isEnabled()) {
            this.worldParticles.onEvent();
            addEvents(this.worldParticles.getEventListeners());
        }
    });
    private final BooleanSetting hitBoolean = new BooleanSetting("Hit").value((Boolean) false).onAction2(() -> {
        if (!getHitBoolean().getValue().booleanValue()) {
            this.hitParticles.toggle();
            this.hitParticles.removeAllEvents();
            removeEvents(this.hitParticles.getEventListeners());
        } else if (isEnabled()) {
            this.hitParticles.onEvent();
            addEvents(this.hitParticles.getEventListeners());
        }
    });
    private final MultiBooleanSetting spawn = new MultiBooleanSetting("Spawn").value(this.worldBoolean, this.hitBoolean);

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

    /* JADX WARN: Type inference failed for: r1v4, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ParticlesModule() {
        addSettings(this.spawn);
        for (Setting<?> setting : this.worldParticles.getSettings()) {
            setting.setVisible(() -> {
                return Boolean.valueOf(this.spawn.isEnabled(this.worldBoolean.getName()));
            });
        }
        addSettings(this.worldParticles.getSettings());
        for (Setting<?> setting2 : this.hitParticles.getSettings()) {
            setting2.setVisible(() -> {
                return Boolean.valueOf(this.spawn.isEnabled(this.hitBoolean.getName()));
            });
        }
        addSettings(this.hitParticles.getSettings());
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        if (this.worldBoolean.getValue().booleanValue()) {
            this.worldParticles.onEvent();
            addEvents(this.worldParticles.getEventListeners());
        }
        if (this.hitBoolean.getValue().booleanValue()) {
            this.hitParticles.onEvent();
            addEvents(this.hitParticles.getEventListeners());
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.worldParticles.toggle();
        this.worldParticles.removeAllEvents();
        this.hitParticles.toggle();
        this.hitParticles.removeAllEvents();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/particles/ParticlesModule$BaseSettings.class */
    public static class BaseSettings extends Configurable {
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

        public BaseSettings(String prefix) {
            this.prefix = prefix;
            this.textureMode = new ModeSetting(this.prefix + ": Texture").value("Spark").values(ParticleRender.textures);
            this.count = new SliderSetting(this.prefix + ": Count").value(Float.valueOf(25.0f)).range(10.0f, 100.0f).step(1.0f);
            this.size = new SliderSetting(this.prefix + ": Size").value(Float.valueOf(0.2f)).range(0.1f, 0.4f).step(0.05f);
            this.lifeTime = new SliderSetting(this.prefix + ": Life time").value(Float.valueOf(10.0f)).range(2.0f, 100.0f).step(1.0f);
            this.spawnDuration = new SliderSetting(this.prefix + ": Spawn duration").value(Float.valueOf(15.0f)).range(0.0f, 40.0f).step(1.0f);
            this.dyingDuration = new SliderSetting(this.prefix + ": Dying duration").value(Float.valueOf(15.0f)).range(0.0f, 40.0f).step(1.0f);
            this.rotate = new BooleanSetting(this.prefix + ": Rotate").value((Boolean) true);
            this.trail = new BooleanSetting(this.prefix + ": Trail").value((Boolean) false);
            this.trailLength = new SliderSetting(this.prefix + ": Trail length").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f);
            this.dyingEffect = new BooleanSetting(this.prefix + ": Dying effect").value((Boolean) false);
            addSettings(this.textureMode, this.count, this.size, this.lifeTime, this.spawnDuration, this.dyingDuration, this.rotate, this.trail, this.trailLength, this.dyingEffect);
        }
    }
}
