package sweetie.leonware.client.features.modules.render.shaders;

import java.awt.Color;
import java.util.function.BiConsumer;
import lombok.Generated;
import net.minecraft.class_279;
import net.minecraft.class_284;
import net.minecraft.class_2960;
import net.minecraft.class_9909;
import net.minecraft.class_9960;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.render.ShadersTicker;
import sweetie.leonware.inject.accessors.IPostEffectProcessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/shaders/ShadersModule.class */
@ModuleRegister(name = "Shaders", category = Category.RENDER)
public class ShadersModule extends Module {
    private static final ShadersModule instance = new ShadersModule();
    public class_279[] defaultShader;
    public class_279[] gradientShader;
    public class_279[] rainbowShader;
    public class_279[] pulseShader;
    public final ModeSetting shaderMode = new ModeSetting("Mode").values("Default", "Gradient", "Rainbow", "Pulse").value("Default");
    public final ColorSetting fillColor = new ColorSetting("Fill Color").value(new Color(255, 255, 255, 0)).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Default"));
    });
    public final ColorSetting outlineColor = new ColorSetting("Outline Color").value(new Color(213, 142, 253, 255)).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Default"));
    });
    public final ColorSetting color1 = new ColorSetting("Color 1").value(new Color(213, 142, 253)).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
    });
    public final ColorSetting color2 = new ColorSetting("Color 2").value(new Color(42, 0, 67)).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
    });
    public final SliderSetting brightness = new SliderSetting("Brightness").value(Float.valueOf(1.0f)).range(0.1f, 1.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Rainbow"));
    });
    public final SliderSetting saturation = new SliderSetting("Saturation").value(Float.valueOf(0.6f)).range(0.0f, 1.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.shaderMode.is("Rainbow"));
    });
    public final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.5f, 5.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(!this.shaderMode.is("Default"));
    });
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(!this.shaderMode.is("Default"));
    });
    public final SliderSetting fillAlpha = new SliderSetting("Fill Alpha").value(Float.valueOf(90.0f)).range(0.0f, 255.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(!this.shaderMode.is("Default"));
    });
    public final SliderSetting outlineAlpha = new SliderSetting("Outline Alpha").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(!this.shaderMode.is("Default"));
    });
    public final SliderSetting lineWidth = new SliderSetting("Line Width").value(Float.valueOf(1.0f)).range(0.0f, 6.0f).step(1.0f);
    public final BooleanSetting bloom = new BooleanSetting("Bloom").value((Boolean) true);
    public final SliderSetting bloomWidth = new SliderSetting("Bloom Width").value(Float.valueOf(5.0f)).range(0.0f, 15.0f).step(1.0f).setVisible(() -> {
        return this.bloom.getValue();
    });
    public final SliderSetting bloomFactor = new SliderSetting("Bloom Factor").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return this.bloom.getValue();
    });
    public final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value((Boolean) true), new BooleanSetting("Self").value((Boolean) true), new BooleanSetting("Items").value((Boolean) true), new BooleanSetting("Hostiles").value((Boolean) true), new BooleanSetting("Passive").value((Boolean) true), new BooleanSetting("Crystals").value((Boolean) true), new BooleanSetting("Hands").value((Boolean) true));

    @Generated
    public static ShadersModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v29, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v34, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v39, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v44, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v55, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v60, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v8, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    public ShadersModule() {
        addSettings(this.shaderMode, this.fillColor, this.outlineColor, this.color1, this.color2, this.brightness, this.saturation, this.speed, this.scale, this.fillAlpha, this.outlineAlpha, this.lineWidth, this.bloom, this.bloomWidth, this.bloomFactor, this.targets);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    @Override // sweetie.leonware.api.module.Module
    public String getDisplayInfo() {
        return this.shaderMode.getValue();
    }

    public void loadShaders() {
        this.defaultShader = loadShader("default");
        this.gradientShader = loadShader("gradient");
        this.rainbowShader = loadShader("rainbow");
        this.pulseShader = loadShader("pulse");
    }

    private class_279[] loadShader(String path) {
        return new class_279[]{mc.method_62887().method_62941(class_2960.method_60655("leonware", path + "_outline"), class_9960.field_53903), mc.method_62887().method_62941(class_2960.method_60655("leonware", path + "_bloom_outline"), class_9960.field_53903)};
    }

    public void drawShader(class_9909 builder, int textureWidth, int textureHeight, class_279.class_9961 framebufferSet) {
        class_279 class_279Var;
        switch (this.shaderMode.getValue()) {
            case "Gradient":
                class_279 processor = getEffectProcessor(this.gradientShader);
                setupUniforms(processor, (name, uniform) -> {
                    defaultSetup(name, uniform);
                    modifiedSetup(name, uniform);
                    switch (name) {
                        case "color1":
                            uniform.method_1249(this.color1.getValue().getRed() / 255.0f, this.color1.getValue().getGreen() / 255.0f, this.color1.getValue().getBlue() / 255.0f);
                            break;
                        case "color2":
                            uniform.method_1249(this.color2.getValue().getRed() / 255.0f, this.color2.getValue().getGreen() / 255.0f, this.color2.getValue().getBlue() / 255.0f);
                            break;
                    }
                });
                class_279Var = processor;
                break;
            case "Rainbow":
                class_279 processor2 = getEffectProcessor(this.rainbowShader);
                setupUniforms(processor2, (name2, uniform2) -> {
                    defaultSetup(name2, uniform2);
                    modifiedSetup(name2, uniform2);
                    switch (name2) {
                        case "brightness":
                            uniform2.method_1251(this.brightness.getValue().floatValue());
                            break;
                        case "saturation":
                            uniform2.method_1251(this.saturation.getValue().floatValue());
                            break;
                    }
                });
                class_279Var = processor2;
                break;
            case "Pulse":
                class_279 processor3 = getEffectProcessor(this.pulseShader);
                setupUniforms(processor3, (name3, uniform3) -> {
                    defaultSetup(name3, uniform3);
                    modifiedSetup(name3, uniform3);
                    switch (name3) {
                        case "color1":
                            uniform3.method_1249(this.color1.getValue().getRed() / 255.0f, this.color1.getValue().getGreen() / 255.0f, this.color1.getValue().getBlue() / 255.0f);
                            break;
                        case "color2":
                            uniform3.method_1249(this.color2.getValue().getRed() / 255.0f, this.color2.getValue().getGreen() / 255.0f, this.color2.getValue().getBlue() / 255.0f);
                            break;
                    }
                });
                class_279Var = processor3;
                break;
            default:
                class_279 processor4 = getEffectProcessor(this.defaultShader);
                setupUniforms(processor4, (name4, uniform4) -> {
                    defaultSetup(name4, uniform4);
                    switch (name4) {
                        case "color":
                            uniform4.method_35657(this.fillColor.getValue().getRed() / 255.0f, this.fillColor.getValue().getGreen() / 255.0f, this.fillColor.getValue().getBlue() / 255.0f, this.fillColor.getValue().getAlpha() / 255.0f);
                            break;
                        case "outlinecolor":
                            uniform4.method_35657(this.outlineColor.getValue().getRed() / 255.0f, this.outlineColor.getValue().getGreen() / 255.0f, this.outlineColor.getValue().getBlue() / 255.0f, this.outlineColor.getValue().getAlpha() / 255.0f);
                            break;
                    }
                });
                class_279Var = processor4;
                break;
        }
        class_279 postEffectProcessor = class_279Var;
        postEffectProcessor.method_62234(builder, textureWidth, textureHeight, framebufferSet);
    }

    private class_279 getEffectProcessor(class_279[] shader) {
        return this.bloom.getValue().booleanValue() ? shader[1] : shader[0];
    }

    private void defaultSetup(String name, class_284 uniform) {
        switch (name) {
            case "quality":
                uniform.method_35649(this.lineWidth.getValue().intValue());
                break;
            case "extra_quality":
                uniform.method_35649(this.bloomWidth.getValue().intValue());
                break;
            case "Radius":
                uniform.method_1251(this.bloomFactor.getValue().floatValue());
                break;
        }
    }

    private void modifiedSetup(String name, class_284 uniform) {
        switch (name) {
            case "scale":
                uniform.method_1251(this.scale.getValue().floatValue() * 1000.0f);
                break;
            case "time":
                uniform.method_1251(ShadersTicker.getInstance().getPassedTime() / 1000.0f);
                break;
            case "resolution":
                uniform.method_1255(mc.method_22683().method_4480(), mc.method_22683().method_4507());
                break;
            case "fillAlpha":
                uniform.method_1251(this.fillAlpha.getValue().floatValue() / 255.0f);
                break;
            case "outlineAlpha":
                uniform.method_1251(this.outlineAlpha.getValue().floatValue() / 255.0f);
                break;
            case "speed":
                uniform.method_1251(this.speed.getValue().floatValue() * 3.0f);
                break;
        }
    }

    private void setupUniforms(class_279 postEffectProcessor, BiConsumer<String, class_284> biConsumer) {
        ((IPostEffectProcessor) postEffectProcessor).leonware$getPasses().forEach(postEffectPass -> {
            postEffectPass.method_62922().leonware$getUniforms().forEach(biConsumer);
        });
    }

    public boolean isTargetPlayers() {
        return this.targets.isEnabled("Players");
    }

    public boolean isTargetSelf() {
        return this.targets.isEnabled("Self");
    }

    public boolean isTargetItems() {
        return this.targets.isEnabled("Items");
    }

    public boolean isTargetHostiles() {
        return this.targets.isEnabled("Hostiles");
    }

    public boolean isTargetPassive() {
        return this.targets.isEnabled("Passive");
    }

    public boolean isTargetCrystals() {
        return this.targets.isEnabled("Crystals");
    }

    public boolean isTargetHands() {
        return this.targets.isEnabled("Hands");
    }
}
