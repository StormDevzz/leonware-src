/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_279
 *  net.minecraft.class_279$class_9961
 *  net.minecraft.class_284
 *  net.minecraft.class_2960
 *  net.minecraft.class_5944
 *  net.minecraft.class_9909
 *  net.minecraft.class_9960
 */
package sweetie.leonware.client.features.modules.render.shaders;

import java.awt.Color;
import java.util.function.BiConsumer;
import lombok.Generated;
import net.minecraft.class_279;
import net.minecraft.class_284;
import net.minecraft.class_2960;
import net.minecraft.class_5944;
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
import sweetie.leonware.inject.accessors.IShaderProgram;

@ModuleRegister(name="Shaders", category=Category.RENDER)
public class ShadersModule
extends Module {
    private static final ShadersModule instance = new ShadersModule();
    public class_279[] defaultShader;
    public class_279[] gradientShader;
    public class_279[] rainbowShader;
    public class_279[] pulseShader;
    public final ModeSetting shaderMode = new ModeSetting("Mode").values("Default", "Gradient", "Rainbow", "Pulse").value("Default");
    public final ColorSetting fillColor = new ColorSetting("Fill Color").value(new Color(255, 255, 255, 0)).setVisible(() -> this.shaderMode.is("Default"));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color").value(new Color(213, 142, 253, 255)).setVisible(() -> this.shaderMode.is("Default"));
    public final ColorSetting color1 = new ColorSetting("Color 1").value(new Color(213, 142, 253)).setVisible(() -> this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
    public final ColorSetting color2 = new ColorSetting("Color 2").value(new Color(42, 0, 67)).setVisible(() -> this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
    public final SliderSetting brightness = new SliderSetting("Brightness").value(Float.valueOf(1.0f)).range(0.1f, 1.0f).step(0.05f).setVisible(() -> this.shaderMode.is("Rainbow"));
    public final SliderSetting saturation = new SliderSetting("Saturation").value(Float.valueOf(0.6f)).range(0.0f, 1.0f).step(0.05f).setVisible(() -> this.shaderMode.is("Rainbow"));
    public final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.5f, 5.0f).step(0.1f).setVisible(() -> !this.shaderMode.is("Default"));
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
    public final SliderSetting fillAlpha = new SliderSetting("Fill Alpha").value(Float.valueOf(90.0f)).range(0.0f, 255.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
    public final SliderSetting outlineAlpha = new SliderSetting("Outline Alpha").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
    public final SliderSetting lineWidth = new SliderSetting("Line Width").value(Float.valueOf(1.0f)).range(0.0f, 6.0f).step(1.0f);
    public final BooleanSetting bloom = new BooleanSetting("Bloom").value(true);
    public final SliderSetting bloomWidth = new SliderSetting("Bloom Width").value(Float.valueOf(5.0f)).range(0.0f, 15.0f).step(1.0f).setVisible(() -> (Boolean)this.bloom.getValue());
    public final SliderSetting bloomFactor = new SliderSetting("Bloom Factor").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> (Boolean)this.bloom.getValue());
    public final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Self").value(true), new BooleanSetting("Items").value(true), new BooleanSetting("Hostiles").value(true), new BooleanSetting("Passive").value(true), new BooleanSetting("Crystals").value(true), new BooleanSetting("Hands").value(true));

    public ShadersModule() {
        this.addSettings(this.shaderMode, this.fillColor, this.outlineColor, this.color1, this.color2, this.brightness, this.saturation, this.speed, this.scale, this.fillAlpha, this.outlineAlpha, this.lineWidth, this.bloom, this.bloomWidth, this.bloomFactor, this.targets);
    }

    @Override
    public void onEvent() {
    }

    @Override
    public String getDisplayInfo() {
        return (String)this.shaderMode.getValue();
    }

    public void loadShaders() {
        this.defaultShader = this.loadShader("default");
        this.gradientShader = this.loadShader("gradient");
        this.rainbowShader = this.loadShader("rainbow");
        this.pulseShader = this.loadShader("pulse");
    }

    private class_279[] loadShader(String path) {
        return new class_279[]{mc.method_62887().method_62941(class_2960.method_60655((String)"leonware", (String)(path + "_outline")), class_9960.field_53903), mc.method_62887().method_62941(class_2960.method_60655((String)"leonware", (String)(path + "_bloom_outline")), class_9960.field_53903)};
    }

    public void drawShader(class_9909 builder, int textureWidth, int textureHeight, class_279.class_9961 framebufferSet) {
        class_279 postEffectProcessor = switch ((String)this.shaderMode.getValue()) {
            case "Gradient" -> {
                class_279 processor = this.getEffectProcessor(this.gradientShader);
                this.setupUniforms(processor, (name, uniform) -> {
                    this.defaultSetup((String)name, (class_284)uniform);
                    this.modifiedSetup((String)name, (class_284)uniform);
                    switch (name) {
                        case "color1": {
                            uniform.method_1249((float)((Color)this.color1.getValue()).getRed() / 255.0f, (float)((Color)this.color1.getValue()).getGreen() / 255.0f, (float)((Color)this.color1.getValue()).getBlue() / 255.0f);
                            break;
                        }
                        case "color2": {
                            uniform.method_1249((float)((Color)this.color2.getValue()).getRed() / 255.0f, (float)((Color)this.color2.getValue()).getGreen() / 255.0f, (float)((Color)this.color2.getValue()).getBlue() / 255.0f);
                        }
                    }
                });
                yield processor;
            }
            case "Rainbow" -> {
                class_279 processor = this.getEffectProcessor(this.rainbowShader);
                this.setupUniforms(processor, (name, uniform) -> {
                    this.defaultSetup((String)name, (class_284)uniform);
                    this.modifiedSetup((String)name, (class_284)uniform);
                    switch (name) {
                        case "brightness": {
                            uniform.method_1251(((Float)this.brightness.getValue()).floatValue());
                            break;
                        }
                        case "saturation": {
                            uniform.method_1251(((Float)this.saturation.getValue()).floatValue());
                        }
                    }
                });
                yield processor;
            }
            case "Pulse" -> {
                class_279 processor = this.getEffectProcessor(this.pulseShader);
                this.setupUniforms(processor, (name, uniform) -> {
                    this.defaultSetup((String)name, (class_284)uniform);
                    this.modifiedSetup((String)name, (class_284)uniform);
                    switch (name) {
                        case "color1": {
                            uniform.method_1249((float)((Color)this.color1.getValue()).getRed() / 255.0f, (float)((Color)this.color1.getValue()).getGreen() / 255.0f, (float)((Color)this.color1.getValue()).getBlue() / 255.0f);
                            break;
                        }
                        case "color2": {
                            uniform.method_1249((float)((Color)this.color2.getValue()).getRed() / 255.0f, (float)((Color)this.color2.getValue()).getGreen() / 255.0f, (float)((Color)this.color2.getValue()).getBlue() / 255.0f);
                        }
                    }
                });
                yield processor;
            }
            default -> {
                class_279 processor = this.getEffectProcessor(this.defaultShader);
                this.setupUniforms(processor, (name, uniform) -> {
                    this.defaultSetup((String)name, (class_284)uniform);
                    switch (name) {
                        case "color": {
                            uniform.method_35657((float)((Color)this.fillColor.getValue()).getRed() / 255.0f, (float)((Color)this.fillColor.getValue()).getGreen() / 255.0f, (float)((Color)this.fillColor.getValue()).getBlue() / 255.0f, (float)((Color)this.fillColor.getValue()).getAlpha() / 255.0f);
                            break;
                        }
                        case "outlinecolor": {
                            uniform.method_35657((float)((Color)this.outlineColor.getValue()).getRed() / 255.0f, (float)((Color)this.outlineColor.getValue()).getGreen() / 255.0f, (float)((Color)this.outlineColor.getValue()).getBlue() / 255.0f, (float)((Color)this.outlineColor.getValue()).getAlpha() / 255.0f);
                        }
                    }
                });
                yield processor;
            }
        };
        postEffectProcessor.method_62234(builder, textureWidth, textureHeight, framebufferSet);
    }

    private class_279 getEffectProcessor(class_279[] shader) {
        return (Boolean)this.bloom.getValue() != false ? shader[1] : shader[0];
    }

    private void defaultSetup(String name, class_284 uniform) {
        switch (name) {
            case "quality": {
                uniform.method_35649(((Float)this.lineWidth.getValue()).intValue());
                break;
            }
            case "extra_quality": {
                uniform.method_35649(((Float)this.bloomWidth.getValue()).intValue());
                break;
            }
            case "Radius": {
                uniform.method_1251(((Float)this.bloomFactor.getValue()).floatValue());
            }
        }
    }

    private void modifiedSetup(String name, class_284 uniform) {
        switch (name) {
            case "scale": {
                uniform.method_1251(((Float)this.scale.getValue()).floatValue() * 1000.0f);
                break;
            }
            case "time": {
                uniform.method_1251((float)ShadersTicker.getInstance().getPassedTime() / 1000.0f);
                break;
            }
            case "resolution": {
                uniform.method_1255((float)mc.method_22683().method_4480(), (float)mc.method_22683().method_4507());
                break;
            }
            case "fillAlpha": {
                uniform.method_1251(((Float)this.fillAlpha.getValue()).floatValue() / 255.0f);
                break;
            }
            case "outlineAlpha": {
                uniform.method_1251(((Float)this.outlineAlpha.getValue()).floatValue() / 255.0f);
                break;
            }
            case "speed": {
                uniform.method_1251(((Float)this.speed.getValue()).floatValue() * 3.0f);
            }
        }
    }

    private void setupUniforms(class_279 postEffectProcessor, BiConsumer<String, class_284> biConsumer) {
        ((IPostEffectProcessor)postEffectProcessor).leonware$getPasses().forEach(postEffectPass -> {
            class_5944 shaderProgram = postEffectPass.method_62922();
            ((IShaderProgram)shaderProgram).leonware$getUniforms().forEach(biConsumer);
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

    @Generated
    public static ShadersModule getInstance() {
        return instance;
    }
}

