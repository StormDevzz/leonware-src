/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  lombok.Generated
 *  net.minecraft.class_1163
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  net.minecraft.class_5636
 *  net.minecraft.class_6539
 *  net.minecraft.class_6854
 *  net.minecraft.class_9958
 */
package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_1163;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import net.minecraft.class_6539;
import net.minecraft.class_6854;
import net.minecraft.class_9958;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Choice;

@ModuleRegister(name="Ambience", category=Category.RENDER)
public class AmbienceModule
extends Module {
    private static final AmbienceModule instance = new AmbienceModule();
    private final ModeSetting time = new ModeSetting("Time").value(WorldTime.DAY).values(WorldTime.values());
    public final ModeSetting weather = new ModeSetting("Weather").value(Weather.SUNNY).values(Weather.values());
    private final BooleanSetting customFog = new BooleanSetting("Custom fog").value(false);
    private final ColorSetting fogColor = new ColorSetting("Fog color").value(new Color(200, 200, 200)).setVisible(this.customFog::getValue);
    private final SliderSetting fogDistance = new SliderSetting("Fog distance").value(Float.valueOf(-8.0f)).range(-8.0f, 25.0f).step(1.0f).setVisible(this.customFog::getValue);
    private final SliderSetting fogDensity = new SliderSetting("Fog density").value(Float.valueOf(100.0f)).range(0.0f, 100.0f).step(1.0f).setVisible(this.customFog::getValue);
    private final BooleanSetting customSky = new BooleanSetting("Custom sky").value(false);
    private final ColorSetting skyColor = new ColorSetting("Sky color").value(new Color(135, 206, 235)).setVisible(this.customSky::getValue);
    private final BooleanSetting customClouds = new BooleanSetting("Custom clouds").value(false);
    private final ColorSetting cloudsColor = new ColorSetting("Clouds color").value(new Color(255, 255, 255)).setVisible(this.customClouds::getValue);
    private final BooleanSetting customWater = new BooleanSetting("Water color").value(false);
    private final ColorSetting waterColor = new ColorSetting("Water").value(new Color(63, 118, 228)).setVisible(this.customWater::getValue);
    private final BooleanSetting customGrass = new BooleanSetting("Grass color").value(false);
    private final ColorSetting grassColor = new ColorSetting("Grass").value(new Color(110, 180, 60)).setVisible(this.customGrass::getValue);
    private final BooleanSetting customFoliage = new BooleanSetting("Foliage color").value(false);
    private final ColorSetting foliageColor = new ColorSetting("Foliage").value(new Color(56, 153, 26)).setVisible(this.customFoliage::getValue);
    private final BooleanSetting customEntity = new BooleanSetting("Entity tint").value(false);
    private final ColorSetting entityColor = new ColorSetting("Entity color").value(new Color(255, 255, 255)).setVisible(this.customEntity::getValue);

    public AmbienceModule() {
        this.addSettings(this.time, this.weather, this.customFog, this.fogColor, this.fogDistance, this.fogDensity, this.customSky, this.skyColor, this.customClouds, this.cloudsColor, this.customWater, this.waterColor, this.customGrass, this.grassColor, this.customFoliage, this.foliageColor, this.customEntity, this.entityColor);
    }

    public long getTime(long original) {
        if (AmbienceModule.mc.field_1687 == null || !this.isEnabled()) {
            return original;
        }
        WorldTime selected = (WorldTime)Choice.getChoiceByName((String)((String)this.time.getValue()), (ModeSetting.NamedChoice[])WorldTime.values());
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
        if (!this.isEnabled() || !((Boolean)this.customFog.getValue()).booleanValue()) {
            return false;
        }
        GlStateManager._clearColor((float)((float)((Color)this.fogColor.getValue()).getRed() / 255.0f), (float)((float)((Color)this.fogColor.getValue()).getGreen() / 255.0f), (float)((float)((Color)this.fogColor.getValue()).getBlue() / 255.0f), (float)((float)((Color)this.fogColor.getValue()).getAlpha() / 255.0f));
        return true;
    }

    public class_9958 applyCustomFog(class_4184 camera, float viewDistance, class_9958 fog) {
        if (!this.isEnabled() || !((Boolean)this.customFog.getValue()).booleanValue()) {
            return fog;
        }
        float start = class_3532.method_15363((float)((Float)this.fogDistance.getValue()).floatValue(), (float)-8.0f, (float)viewDistance);
        float end = class_3532.method_15363((float)(((Float)this.fogDistance.getValue()).floatValue() + ((Float)this.fogDensity.getValue()).floatValue()), (float)0.0f, (float)viewDistance);
        class_6854 shape = fog.comp_3011();
        if (camera.method_19334() == class_5636.field_27888) {
            shape = class_6854.field_36350;
        }
        return new class_9958(start, end, shape, (float)((Color)this.fogColor.getValue()).getRed() / 255.0f, (float)((Color)this.fogColor.getValue()).getGreen() / 255.0f, (float)((Color)this.fogColor.getValue()).getBlue() / 255.0f, (float)((Color)this.fogColor.getValue()).getAlpha() / 255.0f);
    }

    public int applySkyColor(int original) {
        if (!this.isEnabled() || !((Boolean)this.customSky.getValue()).booleanValue()) {
            return original;
        }
        Color c = (Color)this.skyColor.getValue();
        return c.getAlpha() << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
    }

    public int applyCloudsColor(int original) {
        if (!this.isEnabled() || !((Boolean)this.customClouds.getValue()).booleanValue()) {
            return original;
        }
        Color c = (Color)this.cloudsColor.getValue();
        return 0xFF000000 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
    }

    public int applyBiomeColor(int original, class_6539 resolver) {
        if (!this.isEnabled()) {
            return original;
        }
        if (((Boolean)this.customWater.getValue()).booleanValue() && resolver == class_1163.field_5666) {
            Color c = (Color)this.waterColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        if (((Boolean)this.customGrass.getValue()).booleanValue() && resolver == class_1163.field_5665) {
            Color c = (Color)this.grassColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        if (((Boolean)this.customFoliage.getValue()).booleanValue() && resolver == class_1163.field_5664) {
            Color c = (Color)this.foliageColor.getValue();
            return c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        }
        return original;
    }

    public boolean isEntityTintActive() {
        return this.isEnabled() && (Boolean)this.customEntity.getValue() != false;
    }

    public float[] getEntityTint() {
        Color c = (Color)this.entityColor.getValue();
        return new float[]{(float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, (float)c.getAlpha() / 255.0f};
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static AmbienceModule getInstance() {
        return instance;
    }

    private static enum WorldTime implements ModeSetting.NamedChoice
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
        private WorldTime(String name) {
            this.name = name;
        }
    }

    public static enum Weather implements ModeSetting.NamedChoice
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
        private Weather(String name) {
            this.name = name;
        }
    }
}

