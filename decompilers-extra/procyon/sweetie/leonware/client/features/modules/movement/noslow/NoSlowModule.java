// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.noslow;

import sweetie.leonware.api.system.backend.Choice;
import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowFuntime;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowV3;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowGrim;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowSlotUpdate;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowCancel;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Slow", category = Category.MOVEMENT)
public class NoSlowModule extends Module
{
    private static final NoSlowModule instance;
    private final NoSlowCancel noSlowCancel;
    private final NoSlowSlotUpdate noSlowSlotUpdate;
    private final NoSlowGrim noSlowGrim;
    private final NoSlowV3 noSlowGrimV3;
    private final NoSlowFuntime noSlowFunTime;
    private final NoSlowMode[] modes;
    private NoSlowMode currentMode;
    private final ModeSetting mode;
    private final ModeSetting grimMode;
    
    public NoSlowModule() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/api/module/Module.<init>:()V
        //     4: aload_0         /* this */
        //     5: new             Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel;
        //     8: dup            
        //     9: invokespecial   sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel.<init>:()V
        //    12: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowCancel:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel;
        //    15: aload_0         /* this */
        //    16: new             Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowSlotUpdate;
        //    19: dup            
        //    20: invokespecial   sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowSlotUpdate.<init>:()V
        //    23: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowSlotUpdate:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowSlotUpdate;
        //    26: aload_0         /* this */
        //    27: new             Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim;
        //    30: dup            
        //    31: invokespecial   sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim.<init>:()V
        //    34: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowGrim:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim;
        //    37: aload_0         /* this */
        //    38: new             Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowV3;
        //    41: dup            
        //    42: invokespecial   sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowV3.<init>:()V
        //    45: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowGrimV3:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowV3;
        //    48: aload_0         /* this */
        //    49: new             Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowFuntime;
        //    52: dup            
        //    53: invokespecial   sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowFuntime.<init>:()V
        //    56: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowFunTime:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowFuntime;
        //    59: aload_0         /* this */
        //    60: iconst_5       
        //    61: anewarray       Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowMode;
        //    64: dup            
        //    65: iconst_0       
        //    66: aload_0         /* this */
        //    67: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowCancel:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel;
        //    70: aastore        
        //    71: dup            
        //    72: iconst_1       
        //    73: aload_0         /* this */
        //    74: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowSlotUpdate:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowSlotUpdate;
        //    77: aastore        
        //    78: dup            
        //    79: iconst_2       
        //    80: aload_0         /* this */
        //    81: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowGrim:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim;
        //    84: aastore        
        //    85: dup            
        //    86: iconst_3       
        //    87: aload_0         /* this */
        //    88: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowGrimV3:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowV3;
        //    91: aastore        
        //    92: dup            
        //    93: iconst_4       
        //    94: aload_0         /* this */
        //    95: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowFunTime:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowFuntime;
        //    98: aastore        
        //    99: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.modes:[Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowMode;
        //   102: aload_0         /* this */
        //   103: aload_0         /* this */
        //   104: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowCancel:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel;
        //   107: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.currentMode:Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowMode;
        //   110: aload_0         /* this */
        //   111: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //   114: dup            
        //   115: ldc             "Mode"
        //   117: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //   120: ldc             "Cancel"
        //   122: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   125: aload_0         /* this */
        //   126: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.modes:[Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowMode;
        //   129: invokestatic    sweetie/leonware/api/system/backend/Choice.getValues:([Lsweetie/leonware/api/system/backend/Choice;)[Ljava/lang/String;
        //   132: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   135: aload_0         /* this */
        //   136: invokedynamic   BootstrapMethod #0, run:(Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowModule;)Ljava/lang/Runnable;
        //   141: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   144: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   147: aload_0         /* this */
        //   148: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //   151: dup            
        //   152: ldc             "Grim mode"
        //   154: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //   157: ldc             "Tick"
        //   159: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   162: iconst_4       
        //   163: anewarray       Ljava/lang/String;
        //   166: dup            
        //   167: iconst_0       
        //   168: ldc             "Tick"
        //   170: aastore        
        //   171: dup            
        //   172: iconst_1       
        //   173: ldc             "TickS"
        //   175: aastore        
        //   176: dup            
        //   177: iconst_2       
        //   178: ldc             "Old"
        //   180: aastore        
        //   181: dup            
        //   182: iconst_3       
        //   183: ldc             "Drop"
        //   185: aastore        
        //   186: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   189: aload_0         /* this */
        //   190: invokedynamic   BootstrapMethod #1, get:(Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowModule;)Ljava/util/function/Supplier;
        //   195: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   198: aload_0         /* this */
        //   199: invokedynamic   BootstrapMethod #2, run:(Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowModule;)Ljava/lang/Runnable;
        //   204: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   207: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.grimMode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   210: aload_0         /* this */
        //   211: iconst_2       
        //   212: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //   215: dup            
        //   216: iconst_0       
        //   217: aload_0         /* this */
        //   218: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   221: aastore        
        //   222: dup            
        //   223: iconst_1       
        //   224: aload_0         /* this */
        //   225: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.grimMode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   228: aastore        
        //   229: invokevirtual   sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //   232: aload_0         /* this */
        //   233: aload_0         /* this */
        //   234: getfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.noSlowCancel:Lsweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel;
        //   237: putfield        sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.currentMode:Lsweetie/leonware/client/features/modules/movement/noslow/NoSlowMode;
        //   240: return         
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
    
    public boolean doUseNoSlow() {
        return this.isEnabled() && NoSlowModule.mc.field_1724 != null && this.currentMode != null && NoSlowModule.mc.field_1724.method_6115() && this.currentMode.slowingCancel();
    }
    
    public NoSlowV3 getNoSlowV3() {
        return this.noSlowGrimV3;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
            return;
        }));
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onTick();
            }
            return;
        }));
        this.addEvents(updateEvent, tickEvent);
    }
    
    @Override
    public void onDisable() {
        this.noSlowFunTime.onDisable();
        super.onDisable();
    }
    
    @Generated
    public static NoSlowModule getInstance() {
        return NoSlowModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    @Generated
    public ModeSetting getGrimMode() {
        return this.grimMode;
    }
    
    static {
        instance = new NoSlowModule();
    }
}
