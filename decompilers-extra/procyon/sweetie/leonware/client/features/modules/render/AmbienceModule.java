// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1163;
import net.minecraft.class_6539;
import net.minecraft.class_6854;
import net.minecraft.class_5636;
import net.minecraft.class_3532;
import net.minecraft.class_9958;
import net.minecraft.class_4184;
import com.mojang.blaze3d.platform.GlStateManager;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import java.awt.Color;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Ambience", category = Category.RENDER)
public class AmbienceModule extends Module
{
    private static final AmbienceModule instance;
    private final ModeSetting time;
    public final ModeSetting weather;
    private final BooleanSetting customFog;
    private final ColorSetting fogColor;
    private final SliderSetting fogDistance;
    private final SliderSetting fogDensity;
    private final BooleanSetting customSky;
    private final ColorSetting skyColor;
    private final BooleanSetting customClouds;
    private final ColorSetting cloudsColor;
    private final BooleanSetting customWater;
    private final ColorSetting waterColor;
    private final BooleanSetting customGrass;
    private final ColorSetting grassColor;
    private final BooleanSetting customFoliage;
    private final ColorSetting foliageColor;
    private final BooleanSetting customEntity;
    private final ColorSetting entityColor;
    
    public AmbienceModule() {
        this.time = new ModeSetting("Time").value(WorldTime.DAY).values((Enum<?>[])WorldTime.values());
        this.weather = new ModeSetting("Weather").value(Weather.SUNNY).values((Enum<?>[])Weather.values());
        this.customFog = new BooleanSetting("Custom fog").value(false);
        final ColorSetting value = new ColorSetting("Fog color").value(new Color(200, 200, 200));
        final BooleanSetting customFog = this.customFog;
        Objects.requireNonNull(customFog);
        this.fogColor = value.setVisible((Supplier<Boolean>)customFog::getValue);
        final SliderSetting step = new SliderSetting("Fog distance").value(-8.0f).range(-8.0f, 25.0f).step(1.0f);
        final BooleanSetting customFog2 = this.customFog;
        Objects.requireNonNull(customFog2);
        this.fogDistance = step.setVisible((Supplier<Boolean>)customFog2::getValue);
        final SliderSetting step2 = new SliderSetting("Fog density").value(100.0f).range(0.0f, 100.0f).step(1.0f);
        final BooleanSetting customFog3 = this.customFog;
        Objects.requireNonNull(customFog3);
        this.fogDensity = step2.setVisible((Supplier<Boolean>)customFog3::getValue);
        this.customSky = new BooleanSetting("Custom sky").value(false);
        final ColorSetting value2 = new ColorSetting("Sky color").value(new Color(135, 206, 235));
        final BooleanSetting customSky = this.customSky;
        Objects.requireNonNull(customSky);
        this.skyColor = value2.setVisible((Supplier<Boolean>)customSky::getValue);
        this.customClouds = new BooleanSetting("Custom clouds").value(false);
        final ColorSetting value3 = new ColorSetting("Clouds color").value(new Color(255, 255, 255));
        final BooleanSetting customClouds = this.customClouds;
        Objects.requireNonNull(customClouds);
        this.cloudsColor = value3.setVisible((Supplier<Boolean>)customClouds::getValue);
        this.customWater = new BooleanSetting("Water color").value(false);
        final ColorSetting value4 = new ColorSetting("Water").value(new Color(63, 118, 228));
        final BooleanSetting customWater = this.customWater;
        Objects.requireNonNull(customWater);
        this.waterColor = value4.setVisible((Supplier<Boolean>)customWater::getValue);
        this.customGrass = new BooleanSetting("Grass color").value(false);
        final ColorSetting value5 = new ColorSetting("Grass").value(new Color(110, 180, 60));
        final BooleanSetting customGrass = this.customGrass;
        Objects.requireNonNull(customGrass);
        this.grassColor = value5.setVisible((Supplier<Boolean>)customGrass::getValue);
        this.customFoliage = new BooleanSetting("Foliage color").value(false);
        final ColorSetting value6 = new ColorSetting("Foliage").value(new Color(56, 153, 26));
        final BooleanSetting customFoliage = this.customFoliage;
        Objects.requireNonNull(customFoliage);
        this.foliageColor = value6.setVisible((Supplier<Boolean>)customFoliage::getValue);
        this.customEntity = new BooleanSetting("Entity tint").value(false);
        final ColorSetting value7 = new ColorSetting("Entity color").value(new Color(255, 255, 255));
        final BooleanSetting customEntity = this.customEntity;
        Objects.requireNonNull(customEntity);
        this.entityColor = value7.setVisible((Supplier<Boolean>)customEntity::getValue);
        this.addSettings(this.time, this.weather, this.customFog, this.fogColor, this.fogDistance, this.fogDensity, this.customSky, this.skyColor, this.customClouds, this.cloudsColor, this.customWater, this.waterColor, this.customGrass, this.grassColor, this.customFoliage, this.foliageColor, this.customEntity, this.entityColor);
    }
    
    public long getTime(final long original) {
        if (AmbienceModule.mc.field_1687 == null || !this.isEnabled()) {
            return original;
        }
        final WorldTime selected = Choice.getChoiceByName(this.time.getValue(), WorldTime.values());
        return switch (selected.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> original;
            case 1 -> 23041L;
            case 2 -> 1000L;
            case 3 -> 6000L;
            case 4 -> 12610L;
            case 5 -> 13000L;
            case 6 -> 18000L;
        };
    }
    
    public boolean applyBackgroundColor() {
        if (!this.isEnabled() || !this.customFog.getValue()) {
            return false;
        }
        GlStateManager._clearColor(this.fogColor.getValue().getRed() / 255.0f, this.fogColor.getValue().getGreen() / 255.0f, this.fogColor.getValue().getBlue() / 255.0f, this.fogColor.getValue().getAlpha() / 255.0f);
        return true;
    }
    
    public class_9958 applyCustomFog(final class_4184 camera, final float viewDistance, final class_9958 fog) {
        if (!this.isEnabled() || !this.customFog.getValue()) {
            return fog;
        }
        final float start = class_3532.method_15363((float)this.fogDistance.getValue(), -8.0f, viewDistance);
        final float end = class_3532.method_15363(this.fogDistance.getValue() + this.fogDensity.getValue(), 0.0f, viewDistance);
        class_6854 shape = fog.comp_3011();
        if (camera.method_19334() == class_5636.field_27888) {
            shape = class_6854.field_36350;
        }
        return new class_9958(start, end, shape, this.fogColor.getValue().getRed() / 255.0f, this.fogColor.getValue().getGreen() / 255.0f, this.fogColor.getValue().getBlue() / 255.0f, this.fogColor.getValue().getAlpha() / 255.0f);
    }
    
    public int applySkyColor(final int original) {
        if (!this.isEnabled() || !this.customSky.getValue()) {
            return original;
        }
        final Color c = this.skyColor.getValue();
        return c.getAlpha() << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
    }
    
    public int applyCloudsColor(final int original) {
        if (!this.isEnabled() || !this.customClouds.getValue()) {
            return original;
        }
        final Color c = this.cloudsColor.getValue();
        return 0xFF000000 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
    }
    
    public int applyBiomeColor(final int original, final class_6539 resolver) {
        if (!this.isEnabled()) {
            return original;
        }
        if (this.customWater.getValue() && resolver == class_1163.field_5666) {
            final Color c = this.waterColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        if (this.customGrass.getValue() && resolver == class_1163.field_5665) {
            final Color c = this.grassColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        if (this.customFoliage.getValue() && resolver == class_1163.field_5664) {
            final Color c = this.foliageColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        return original;
    }
    
    public boolean isEntityTintActive() {
        return this.isEnabled() && this.customEntity.getValue();
    }
    
    public float[] getEntityTint() {
        final Color c = this.entityColor.getValue();
        return new float[] { c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f };
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static AmbienceModule getInstance() {
        return AmbienceModule.instance;
    }
    
    static {
        instance = new AmbienceModule();
    }
    
    private enum WorldTime implements ModeSetting.NamedChoice
    {
        NO_CHANGE("No change"), 
        DAWN("Dawn"), 
        DAY("Day"), 
        NOON("Noon"), 
        DUSK("Dusk"), 
        NIGHT("Night"), 
        MID_NIGHT("Mid Night");
        
        private final String name;
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Generated
        private WorldTime(final String name) {
            this.name = name;
        }
    }
    
    public enum Weather implements ModeSetting.NamedChoice
    {
        NO_CHANGE("No change"), 
        SUNNY("Sunny"), 
        RAINY("Rainy"), 
        SNOWY("Snowy"), 
        THUNDER("Thunder");
        
        private final String name;
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Generated
        private Weather(final String name) {
            this.name = name;
        }
    }
}
