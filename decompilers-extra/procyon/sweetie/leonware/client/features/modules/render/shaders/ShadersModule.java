// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.shaders;

import net.minecraft.class_283;
import lombok.Generated;
import net.minecraft.class_5944;
import sweetie.leonware.inject.accessors.IShaderProgram;
import sweetie.leonware.inject.accessors.IPostEffectProcessor;
import java.util.function.BiConsumer;
import sweetie.leonware.api.utils.render.ShadersTicker;
import net.minecraft.class_284;
import net.minecraft.class_9909;
import net.minecraft.class_9960;
import net.minecraft.class_2960;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import net.minecraft.class_279;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Shaders", category = Category.RENDER)
public class ShadersModule extends Module
{
    private static final ShadersModule instance;
    public class_279[] defaultShader;
    public class_279[] gradientShader;
    public class_279[] rainbowShader;
    public class_279[] pulseShader;
    public final ModeSetting shaderMode;
    public final ColorSetting fillColor;
    public final ColorSetting outlineColor;
    public final ColorSetting color1;
    public final ColorSetting color2;
    public final SliderSetting brightness;
    public final SliderSetting saturation;
    public final SliderSetting speed;
    public final SliderSetting scale;
    public final SliderSetting fillAlpha;
    public final SliderSetting outlineAlpha;
    public final SliderSetting lineWidth;
    public final BooleanSetting bloom;
    public final SliderSetting bloomWidth;
    public final SliderSetting bloomFactor;
    public final MultiBooleanSetting targets;
    
    public ShadersModule() {
        this.shaderMode = new ModeSetting("Mode").values("Default", "Gradient", "Rainbow", "Pulse").value("Default");
        this.fillColor = new ColorSetting("Fill Color").value(new Color(255, 255, 255, 0)).setVisible(() -> this.shaderMode.is("Default"));
        this.outlineColor = new ColorSetting("Outline Color").value(new Color(213, 142, 253, 255)).setVisible(() -> this.shaderMode.is("Default"));
        this.color1 = new ColorSetting("Color 1").value(new Color(213, 142, 253)).setVisible(() -> this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
        this.color2 = new ColorSetting("Color 2").value(new Color(42, 0, 67)).setVisible(() -> this.shaderMode.is("Gradient") || this.shaderMode.is("Pulse"));
        this.brightness = new SliderSetting("Brightness").value(1.0f).range(0.1f, 1.0f).step(0.05f).setVisible(() -> this.shaderMode.is("Rainbow"));
        this.saturation = new SliderSetting("Saturation").value(0.6f).range(0.0f, 1.0f).step(0.05f).setVisible(() -> this.shaderMode.is("Rainbow"));
        this.speed = new SliderSetting("Speed").value(1.0f).range(0.5f, 5.0f).step(0.1f).setVisible(() -> !this.shaderMode.is("Default"));
        this.scale = new SliderSetting("Scale").value(10.0f).range(1.0f, 20.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
        this.fillAlpha = new SliderSetting("Fill Alpha").value(90.0f).range(0.0f, 255.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
        this.outlineAlpha = new SliderSetting("Outline Alpha").value(255.0f).range(0.0f, 255.0f).step(1.0f).setVisible(() -> !this.shaderMode.is("Default"));
        this.lineWidth = new SliderSetting("Line Width").value(1.0f).range(0.0f, 6.0f).step(1.0f);
        this.bloom = new BooleanSetting("Bloom").value(true);
        this.bloomWidth = new SliderSetting("Bloom Width").value(5.0f).range(0.0f, 15.0f).step(1.0f).setVisible(() -> this.bloom.getValue());
        this.bloomFactor = new SliderSetting("Bloom Factor").value(5.0f).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.bloom.getValue());
        this.targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Self").value(true), new BooleanSetting("Items").value(true), new BooleanSetting("Hostiles").value(true), new BooleanSetting("Passive").value(true), new BooleanSetting("Crystals").value(true), new BooleanSetting("Hands").value(true));
        this.addSettings(this.shaderMode, this.fillColor, this.outlineColor, this.color1, this.color2, this.brightness, this.saturation, this.speed, this.scale, this.fillAlpha, this.outlineAlpha, this.lineWidth, this.bloom, this.bloomWidth, this.bloomFactor, this.targets);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Override
    public String getDisplayInfo() {
        return this.shaderMode.getValue();
    }
    
    public void loadShaders() {
        this.defaultShader = this.loadShader("default");
        this.gradientShader = this.loadShader("gradient");
        this.rainbowShader = this.loadShader("rainbow");
        this.pulseShader = this.loadShader("pulse");
    }
    
    private class_279[] loadShader(final String path) {
        return new class_279[] { ShadersModule.mc.method_62887().method_62941(class_2960.method_60655("leonware", path + "_outline"), class_9960.field_53903), ShadersModule.mc.method_62887().method_62941(class_2960.method_60655("leonware", path + "_bloom_outline"), class_9960.field_53903) };
    }
    
    public void drawShader(final class_9909 builder, final int textureWidth, final int textureHeight, final class_279.class_9961 framebufferSet) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        sweetie/leonware/client/features/modules/render/shaders/ShadersModule.shaderMode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //     4: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.getValue:()Ljava/lang/Object;
        //     7: checkcast       Ljava/lang/String;
        //    10: astore          6
        //    12: iconst_m1      
        //    13: istore          7
        //    15: aload           6
        //    17: invokevirtual   java/lang/String.hashCode:()I
        //    20: lookupswitch {
        //          -1656737386: 72
        //          77474681: 88
        //          154295120: 56
        //          default: 101
        //        }
        //    56: aload           6
        //    58: ldc             "Gradient"
        //    60: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    63: ifeq            101
        //    66: iconst_0       
        //    67: istore          7
        //    69: goto            101
        //    72: aload           6
        //    74: ldc             "Rainbow"
        //    76: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    79: ifeq            101
        //    82: iconst_1       
        //    83: istore          7
        //    85: goto            101
        //    88: aload           6
        //    90: ldc             "Pulse"
        //    92: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    95: ifeq            101
        //    98: iconst_2       
        //    99: istore          7
        //   101: iload           7
        //   103: tableswitch {
        //                0: 128
        //                1: 155
        //                2: 182
        //          default: 209
        //        }
        //   128: aload_0         /* this */
        //   129: aload_0         /* this */
        //   130: getfield        sweetie/leonware/client/features/modules/render/shaders/ShadersModule.gradientShader:[Lnet/minecraft/class_279;
        //   133: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.getEffectProcessor:([Lnet/minecraft/class_279;)Lnet/minecraft/class_279;
        //   136: astore          processor
        //   138: aload_0         /* this */
        //   139: aload           processor
        //   141: aload_0         /* this */
        //   142: invokedynamic   BootstrapMethod #14, accept:(Lsweetie/leonware/client/features/modules/render/shaders/ShadersModule;)Ljava/util/function/BiConsumer;
        //   147: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.setupUniforms:(Lnet/minecraft/class_279;Ljava/util/function/BiConsumer;)V
        //   150: aload           processor
        //   152: goto            236
        //   155: aload_0         /* this */
        //   156: aload_0         /* this */
        //   157: getfield        sweetie/leonware/client/features/modules/render/shaders/ShadersModule.rainbowShader:[Lnet/minecraft/class_279;
        //   160: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.getEffectProcessor:([Lnet/minecraft/class_279;)Lnet/minecraft/class_279;
        //   163: astore          processor
        //   165: aload_0         /* this */
        //   166: aload           processor
        //   168: aload_0         /* this */
        //   169: invokedynamic   BootstrapMethod #15, accept:(Lsweetie/leonware/client/features/modules/render/shaders/ShadersModule;)Ljava/util/function/BiConsumer;
        //   174: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.setupUniforms:(Lnet/minecraft/class_279;Ljava/util/function/BiConsumer;)V
        //   177: aload           processor
        //   179: goto            236
        //   182: aload_0         /* this */
        //   183: aload_0         /* this */
        //   184: getfield        sweetie/leonware/client/features/modules/render/shaders/ShadersModule.pulseShader:[Lnet/minecraft/class_279;
        //   187: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.getEffectProcessor:([Lnet/minecraft/class_279;)Lnet/minecraft/class_279;
        //   190: astore          processor
        //   192: aload_0         /* this */
        //   193: aload           processor
        //   195: aload_0         /* this */
        //   196: invokedynamic   BootstrapMethod #16, accept:(Lsweetie/leonware/client/features/modules/render/shaders/ShadersModule;)Ljava/util/function/BiConsumer;
        //   201: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.setupUniforms:(Lnet/minecraft/class_279;Ljava/util/function/BiConsumer;)V
        //   204: aload           processor
        //   206: goto            236
        //   209: aload_0         /* this */
        //   210: aload_0         /* this */
        //   211: getfield        sweetie/leonware/client/features/modules/render/shaders/ShadersModule.defaultShader:[Lnet/minecraft/class_279;
        //   214: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.getEffectProcessor:([Lnet/minecraft/class_279;)Lnet/minecraft/class_279;
        //   217: astore          processor
        //   219: aload_0         /* this */
        //   220: aload           processor
        //   222: aload_0         /* this */
        //   223: invokedynamic   BootstrapMethod #17, accept:(Lsweetie/leonware/client/features/modules/render/shaders/ShadersModule;)Ljava/util/function/BiConsumer;
        //   228: invokevirtual   sweetie/leonware/client/features/modules/render/shaders/ShadersModule.setupUniforms:(Lnet/minecraft/class_279;Ljava/util/function/BiConsumer;)V
        //   231: aload           processor
        //   233: goto            236
        //   236: astore          postEffectProcessor
        //   238: aload           postEffectProcessor
        //   240: aload_1         /* builder */
        //   241: iload_2         /* textureWidth */
        //   242: iload_3         /* textureHeight */
        //   243: aload           framebufferSet
        //   245: invokevirtual   net/minecraft/class_279.method_62234:(Lnet/minecraft/class_9909;IILnet/minecraft/class_279$class_9961;)V
        //   248: return         
        //    MethodParameters:
        //  Name            Flags  
        //  --------------  -----
        //  builder         
        //  textureWidth    
        //  textureHeight   
        //  framebufferSet  
        //    StackMapTable: 00 09 FE 00 38 00 07 00 3E 01 0F 0F 0C 1A 1A 1A 1A FF 00 1A 00 05 07 00 02 07 01 AD 01 01 07 00 0D 00 01 07 00 0F
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot invoke "com.strobel.assembler.metadata.TypeReference.getSimpleType()" because the return value of "com.strobel.decompiler.ast.Variable.getType()" is null
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private class_279 getEffectProcessor(final class_279[] shader) {
        return this.bloom.getValue() ? shader[1] : shader[0];
    }
    
    private void defaultSetup(final String name, final class_284 uniform) {
        switch (name) {
            case "quality": {
                uniform.method_35649(this.lineWidth.getValue().intValue());
                break;
            }
            case "extra_quality": {
                uniform.method_35649(this.bloomWidth.getValue().intValue());
                break;
            }
            case "Radius": {
                uniform.method_1251((float)this.bloomFactor.getValue());
                break;
            }
        }
    }
    
    private void modifiedSetup(final String name, final class_284 uniform) {
        switch (name) {
            case "scale": {
                uniform.method_1251(this.scale.getValue() * 1000.0f);
                break;
            }
            case "time": {
                uniform.method_1251(ShadersTicker.getInstance().getPassedTime() / 1000.0f);
                break;
            }
            case "resolution": {
                uniform.method_1255((float)ShadersModule.mc.method_22683().method_4480(), (float)ShadersModule.mc.method_22683().method_4507());
                break;
            }
            case "fillAlpha": {
                uniform.method_1251(this.fillAlpha.getValue() / 255.0f);
                break;
            }
            case "outlineAlpha": {
                uniform.method_1251(this.outlineAlpha.getValue() / 255.0f);
                break;
            }
            case "speed": {
                uniform.method_1251(this.speed.getValue() * 3.0f);
                break;
            }
        }
    }
    
    private void setupUniforms(final class_279 postEffectProcessor, final BiConsumer<String, class_284> biConsumer) {
        ((IPostEffectProcessor)postEffectProcessor).leonware$getPasses().forEach(postEffectPass -> {
            final class_5944 shaderProgram = postEffectPass.method_62922();
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
        return ShadersModule.instance;
    }
    
    static {
        instance = new ShadersModule();
    }
}
