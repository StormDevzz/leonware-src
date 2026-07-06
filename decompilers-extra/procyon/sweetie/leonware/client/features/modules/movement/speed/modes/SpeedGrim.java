// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.client.features.modules.movement.speed.SpeedModule;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import net.minecraft.class_1531;
import net.minecraft.class_1309;
import net.minecraft.class_1297;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedGrim extends SpeedMode
{
    public BypassType bypassType;
    private boolean boosting;
    private final TimerUtil timerUtil;
    private final ModeSetting grimType;
    private final SliderSetting collideNewSpeed;
    private final BooleanSetting onlyInAir;
    
    @Override
    public String getName() {
        return "Grim";
    }
    
    public SpeedGrim(final Supplier<Boolean> condition) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/client/features/modules/movement/speed/SpeedMode.<init>:()V
        //     4: aload_0         /* this */
        //     5: getstatic       sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType.COLLIDE:Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType;
        //     8: putfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.bypassType:Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType;
        //    11: aload_0         /* this */
        //    12: new             Lsweetie/leonware/api/utils/math/TimerUtil;
        //    15: dup            
        //    16: invokespecial   sweetie/leonware/api/utils/math/TimerUtil.<init>:()V
        //    19: putfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.timerUtil:Lsweetie/leonware/api/utils/math/TimerUtil;
        //    22: aload_0         /* this */
        //    23: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //    26: dup            
        //    27: ldc             "Grim mode"
        //    29: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //    32: getstatic       sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType.COLLIDE:Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType;
        //    35: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    38: invokestatic    sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType.values:()[Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType;
        //    41: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    44: aload_0         /* this */
        //    45: invokedynamic   BootstrapMethod #0, run:(Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim;)Ljava/lang/Runnable;
        //    50: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    53: putfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //    56: aload_0         /* this */
        //    57: new             Lsweetie/leonware/api/module/setting/SliderSetting;
        //    60: dup            
        //    61: ldc             "Collide Speed"
        //    63: invokespecial   sweetie/leonware/api/module/setting/SliderSetting.<init>:(Ljava/lang/String;)V
        //    66: ldc             0.08
        //    68: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //    71: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.value:(Ljava/lang/Float;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    74: ldc             0.03
        //    76: ldc             0.15
        //    78: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.range:(FF)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    81: ldc             0.01
        //    83: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.step:(F)Lsweetie/leonware/api/module/setting/SliderSetting;
        //    86: putfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.collideNewSpeed:Lsweetie/leonware/api/module/setting/SliderSetting;
        //    89: aload_0         /* this */
        //    90: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //    93: dup            
        //    94: ldc             "Only in Air"
        //    96: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //    99: iconst_0       
        //   100: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   103: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   106: putfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.onlyInAir:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   109: aload_0         /* this */
        //   110: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.collideNewSpeed:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   113: aload_0         /* this */
        //   114: aload_1         /* condition */
        //   115: invokedynamic   BootstrapMethod #1, get:(Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim;Ljava/util/function/Supplier;)Ljava/util/function/Supplier;
        //   120: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   123: pop            
        //   124: aload_0         /* this */
        //   125: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.onlyInAir:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   128: aload_0         /* this */
        //   129: aload_1         /* condition */
        //   130: invokedynamic   BootstrapMethod #2, get:(Lsweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim;Ljava/util/function/Supplier;)Ljava/util/function/Supplier;
        //   135: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   138: pop            
        //   139: aload_0         /* this */
        //   140: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   143: aload_1         /* condition */
        //   144: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   147: pop            
        //   148: aload_0         /* this */
        //   149: iconst_3       
        //   150: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //   153: dup            
        //   154: iconst_0       
        //   155: aload_0         /* this */
        //   156: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   159: aastore        
        //   160: dup            
        //   161: iconst_1       
        //   162: aload_0         /* this */
        //   163: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.collideNewSpeed:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   166: aastore        
        //   167: dup            
        //   168: iconst_2       
        //   169: aload_0         /* this */
        //   170: getfield        sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.onlyInAir:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   173: aastore        
        //   174: invokevirtual   sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //   177: return         
        //    Signature:
        //  (Ljava/util/function/Supplier<Ljava/lang/Boolean;>;)V
        //    MethodParameters:
        //  Name       Flags  
        //  ---------  -----
        //  condition  
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:799)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:635)
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
    
    @Override
    public void onTravel() {
        switch (this.bypassType.ordinal()) {
            case 0:
            case 1: {
                final boolean newMode = this.bypassType == BypassType.COLLIDE_NEW;
                if (newMode && this.onlyInAir.getValue() && SpeedGrim.mc.field_1724.method_24828()) {
                    return;
                }
                int collisions = 0;
                for (final class_1297 entity : SpeedGrim.mc.field_1687.method_18112()) {
                    if (entity instanceof final class_1309 living) {
                        if (living == SpeedGrim.mc.field_1724) {
                            continue;
                        }
                        if (living instanceof class_1531) {
                            continue;
                        }
                        if (!PlayerUtil.hasCollisionWith((class_1297)living, newMode ? 0.0f : 1.0f)) {
                            continue;
                        }
                        ++collisions;
                    }
                }
                if (collisions > 0) {
                    final double[] forward = MoveUtil.forward(this.collideNewSpeed.getValue() * collisions);
                    SpeedGrim.mc.field_1724.method_5762(forward[0], 0.0, forward[1]);
                }
                break;
            }
            default: {
                if (this.timerUtil.finished(1100L)) {
                    this.boosting = true;
                }
                if (this.timerUtil.finished(7000L)) {
                    this.boosting = false;
                    this.timerUtil.reset();
                }
                TimerManager.getInstance().addTimer(this.boosting ? ((SpeedGrim.mc.field_1724.field_6012 % 2 == 0) ? 1.5f : 1.2f) : 0.05f, TaskPriority.HIGH, SpeedModule.getInstance(), 1);
                break;
            }
        }
    }
    
    @Generated
    public ModeSetting getGrimType() {
        return this.grimType;
    }
    
    @Generated
    public SliderSetting getCollideNewSpeed() {
        return this.collideNewSpeed;
    }
    
    @Generated
    public BooleanSetting getOnlyInAir() {
        return this.onlyInAir;
    }
    
    public enum BypassType implements ModeSetting.NamedChoice
    {
        COLLIDE("Collide"), 
        COLLIDE_NEW("Collide new"), 
        TIMER("Timer");
        
        private final String name;
        
        private BypassType(final String name) {
            this.name = name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
