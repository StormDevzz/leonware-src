package sweetie.leonware.client.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.Color;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1163;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import net.minecraft.class_6539;
import net.minecraft.class_6854;
import net.minecraft.class_9958;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Choice;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/AmbienceModule.class */
@ModuleRegister(name = "Ambience", category = Category.RENDER)
public class AmbienceModule extends Module {
    private static final AmbienceModule instance = new AmbienceModule();
    private final ModeSetting time = new ModeSetting("Time").value((Enum<?>) WorldTime.DAY).values(WorldTime.values());
    public final ModeSetting weather = new ModeSetting("Weather").value((Enum<?>) Weather.SUNNY).values(Weather.values());
    private final BooleanSetting customFog = new BooleanSetting("Custom fog").value((Boolean) false);
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

    @Generated
    public static AmbienceModule getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/AmbienceModule$WorldTime.class */
    private enum WorldTime implements ModeSetting.NamedChoice {
        NO_CHANGE("No change"),
        DAWN("Dawn"),
        DAY("Day"),
        NOON("Noon"),
        DUSK("Dusk"),
        NIGHT("Night"),
        MID_NIGHT("Mid Night");

        private final String name;

        @Generated
        WorldTime(final String name) {
            this.name = name;
        }

        @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
        public String getName() {
            return this.name;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/AmbienceModule$Weather.class */
    public enum Weather implements ModeSetting.NamedChoice {
        NO_CHANGE("No change"),
        SUNNY("Sunny"),
        RAINY("Rainy"),
        SNOWY("Snowy"),
        THUNDER("Thunder");

        private final String name;

        @Generated
        Weather(final String name) {
            this.name = name;
        }

        @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
        public String getName() {
            return this.name;
        }
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v15, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v20, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v35, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v40, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v45, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v50, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    public AmbienceModule() {
        ColorSetting colorSettingValue = new ColorSetting("Fog color").value(new Color(200, 200, 200));
        BooleanSetting booleanSetting = this.customFog;
        Objects.requireNonNull(booleanSetting);
        this.fogColor = colorSettingValue.setVisible(booleanSetting::getValue);
        SliderSetting sliderSettingStep = new SliderSetting("Fog distance").value(Float.valueOf(-8.0f)).range(-8.0f, 25.0f).step(1.0f);
        BooleanSetting booleanSetting2 = this.customFog;
        Objects.requireNonNull(booleanSetting2);
        this.fogDistance = sliderSettingStep.setVisible(booleanSetting2::getValue);
        SliderSetting sliderSettingStep2 = new SliderSetting("Fog density").value(Float.valueOf(100.0f)).range(0.0f, 100.0f).step(1.0f);
        BooleanSetting booleanSetting3 = this.customFog;
        Objects.requireNonNull(booleanSetting3);
        this.fogDensity = sliderSettingStep2.setVisible(booleanSetting3::getValue);
        this.customSky = new BooleanSetting("Custom sky").value((Boolean) false);
        ColorSetting colorSettingValue2 = new ColorSetting("Sky color").value(new Color(135, 206, 235));
        BooleanSetting booleanSetting4 = this.customSky;
        Objects.requireNonNull(booleanSetting4);
        this.skyColor = colorSettingValue2.setVisible(booleanSetting4::getValue);
        this.customClouds = new BooleanSetting("Custom clouds").value((Boolean) false);
        ColorSetting colorSettingValue3 = new ColorSetting("Clouds color").value(new Color(255, 255, 255));
        BooleanSetting booleanSetting5 = this.customClouds;
        Objects.requireNonNull(booleanSetting5);
        this.cloudsColor = colorSettingValue3.setVisible(booleanSetting5::getValue);
        this.customWater = new BooleanSetting("Water color").value((Boolean) false);
        ColorSetting colorSettingValue4 = new ColorSetting("Water").value(new Color(63, 118, 228));
        BooleanSetting booleanSetting6 = this.customWater;
        Objects.requireNonNull(booleanSetting6);
        this.waterColor = colorSettingValue4.setVisible(booleanSetting6::getValue);
        this.customGrass = new BooleanSetting("Grass color").value((Boolean) false);
        ColorSetting colorSettingValue5 = new ColorSetting("Grass").value(new Color(110, 180, 60));
        BooleanSetting booleanSetting7 = this.customGrass;
        Objects.requireNonNull(booleanSetting7);
        this.grassColor = colorSettingValue5.setVisible(booleanSetting7::getValue);
        this.customFoliage = new BooleanSetting("Foliage color").value((Boolean) false);
        ColorSetting colorSettingValue6 = new ColorSetting("Foliage").value(new Color(56, 153, 26));
        BooleanSetting booleanSetting8 = this.customFoliage;
        Objects.requireNonNull(booleanSetting8);
        this.foliageColor = colorSettingValue6.setVisible(booleanSetting8::getValue);
        this.customEntity = new BooleanSetting("Entity tint").value((Boolean) false);
        ColorSetting colorSettingValue7 = new ColorSetting("Entity color").value(new Color(255, 255, 255));
        BooleanSetting booleanSetting9 = this.customEntity;
        Objects.requireNonNull(booleanSetting9);
        this.entityColor = colorSettingValue7.setVisible(booleanSetting9::getValue);
        addSettings(this.time, this.weather, this.customFog, this.fogColor, this.fogDistance, this.fogDensity, this.customSky, this.skyColor, this.customClouds, this.cloudsColor, this.customWater, this.waterColor, this.customGrass, this.grassColor, this.customFoliage, this.foliageColor, this.customEntity, this.entityColor);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public long getTime(long original) throws MatchException {
        if (mc.field_1687 == null || !isEnabled()) {
            return original;
        }
        WorldTime selected = (WorldTime) Choice.getChoiceByName(this.time.getValue(), WorldTime.values());
        switch (selected.ordinal()) {
            case 0:
                return original;
            case 1:
                return 23041L;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return 1000L;
            case 3:
                return 6000L;
            case 4:
                return 12610L;
            case 5:
                return 13000L;
            case 6:
                return 18000L;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    public boolean applyBackgroundColor() {
        if (!isEnabled() || !this.customFog.getValue().booleanValue()) {
            return false;
        }
        GlStateManager._clearColor(this.fogColor.getValue().getRed() / 255.0f, this.fogColor.getValue().getGreen() / 255.0f, this.fogColor.getValue().getBlue() / 255.0f, this.fogColor.getValue().getAlpha() / 255.0f);
        return true;
    }

    public class_9958 applyCustomFog(class_4184 camera, float viewDistance, class_9958 fog) {
        if (!isEnabled() || !this.customFog.getValue().booleanValue()) {
            return fog;
        }
        float start = class_3532.method_15363(this.fogDistance.getValue().floatValue(), -8.0f, viewDistance);
        float end = class_3532.method_15363(this.fogDistance.getValue().floatValue() + this.fogDensity.getValue().floatValue(), 0.0f, viewDistance);
        class_6854 shape = fog.comp_3011();
        if (camera.method_19334() == class_5636.field_27888) {
            shape = class_6854.field_36350;
        }
        return new class_9958(start, end, shape, this.fogColor.getValue().getRed() / 255.0f, this.fogColor.getValue().getGreen() / 255.0f, this.fogColor.getValue().getBlue() / 255.0f, this.fogColor.getValue().getAlpha() / 255.0f);
    }

    public int applySkyColor(int original) {
        if (!isEnabled() || !this.customSky.getValue().booleanValue()) {
            return original;
        }
        Color c = this.skyColor.getValue();
        return (c.getAlpha() << 24) | (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    public int applyCloudsColor(int original) {
        if (!isEnabled() || !this.customClouds.getValue().booleanValue()) {
            return original;
        }
        Color c = this.cloudsColor.getValue();
        return (-16777216) | (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    public int applyBiomeColor(int original, class_6539 resolver) {
        if (!isEnabled()) {
            return original;
        }
        if (this.customWater.getValue().booleanValue() && resolver == class_1163.field_5666) {
            Color c = this.waterColor.getValue();
            return (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
        }
        if (this.customGrass.getValue().booleanValue() && resolver == class_1163.field_5665) {
            Color c2 = this.grassColor.getValue();
            return (c2.getRed() << 16) | (c2.getGreen() << 8) | c2.getBlue();
        }
        if (this.customFoliage.getValue().booleanValue() && resolver == class_1163.field_5664) {
            Color c3 = this.foliageColor.getValue();
            return (c3.getRed() << 16) | (c3.getGreen() << 8) | c3.getBlue();
        }
        return original;
    }

    public boolean isEntityTintActive() {
        return isEnabled() && this.customEntity.getValue().booleanValue();
    }

    public float[] getEntityTint() {
        Color c = this.entityColor.getValue();
        return new float[]{c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f};
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
