// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.particles;

import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Configurable;
import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Particles", category = Category.RENDER)
public class ParticlesModule extends Module
{
    private static final ParticlesModule instance;
    private final HitParticles hitParticles;
    private final CubeParticles worldParticles;
    private final BooleanSetting worldBoolean;
    private final BooleanSetting hitBoolean;
    private final MultiBooleanSetting spawn;
    
    public ParticlesModule() {
        this.hitParticles = new HitParticles();
        this.worldParticles = new CubeParticles();
        this.worldBoolean = new BooleanSetting("World").value(true).onAction(() -> {
            if (!this.getWorldBoolean().getValue()) {
                this.worldParticles.toggle();
                this.worldParticles.removeAllEvents();
                this.removeEvents(this.worldParticles.getEventListeners());
            }
            else if (this.isEnabled()) {
                this.worldParticles.onEvent();
                this.addEvents(this.worldParticles.getEventListeners());
            }
            return;
        });
        this.hitBoolean = new BooleanSetting("Hit").value(false).onAction(() -> {
            if (!this.getHitBoolean().getValue()) {
                this.hitParticles.toggle();
                this.hitParticles.removeAllEvents();
                this.removeEvents(this.hitParticles.getEventListeners());
            }
            else if (this.isEnabled()) {
                this.hitParticles.onEvent();
                this.addEvents(this.hitParticles.getEventListeners());
            }
            return;
        });
        this.spawn = new MultiBooleanSetting("Spawn").value(this.worldBoolean, this.hitBoolean);
        this.addSettings(this.spawn);
        for (final Setting<?> setting : this.worldParticles.getSettings()) {
            setting.setVisible(() -> this.spawn.isEnabled(this.worldBoolean.getName()));
        }
        this.addSettings(this.worldParticles.getSettings());
        for (final Setting<?> setting : this.hitParticles.getSettings()) {
            setting.setVisible(() -> this.spawn.isEnabled(this.hitBoolean.getName()));
        }
        this.addSettings(this.hitParticles.getSettings());
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (this.worldBoolean.getValue()) {
            this.worldParticles.onEvent();
            this.addEvents(this.worldParticles.getEventListeners());
        }
        if (this.hitBoolean.getValue()) {
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
        return ParticlesModule.instance;
    }
    
    @Generated
    public BooleanSetting getWorldBoolean() {
        return this.worldBoolean;
    }
    
    @Generated
    public BooleanSetting getHitBoolean() {
        return this.hitBoolean;
    }
    
    static {
        instance = new ParticlesModule();
    }
    
    public static class BaseSettings extends Configurable
    {
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
        
        public BaseSettings(final String prefix) {
            this.prefix = prefix;
            this.textureMode = new ModeSetting(this.prefix + ": Texture").value("Spark").values(ParticleRender.textures);
            this.count = new SliderSetting(this.prefix + ": Count").value(25.0f).range(10.0f, 100.0f).step(1.0f);
            this.size = new SliderSetting(this.prefix + ": Size").value(0.2f).range(0.1f, 0.4f).step(0.05f);
            this.lifeTime = new SliderSetting(this.prefix + ": Life time").value(10.0f).range(2.0f, 100.0f).step(1.0f);
            this.spawnDuration = new SliderSetting(this.prefix + ": Spawn duration").value(15.0f).range(0.0f, 40.0f).step(1.0f);
            this.dyingDuration = new SliderSetting(this.prefix + ": Dying duration").value(15.0f).range(0.0f, 40.0f).step(1.0f);
            this.rotate = new BooleanSetting(this.prefix + ": Rotate").value(true);
            this.trail = new BooleanSetting(this.prefix + ": Trail").value(false);
            this.trailLength = new SliderSetting(this.prefix + ": Trail length").value(5.0f).range(1.0f, 20.0f).step(1.0f);
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
