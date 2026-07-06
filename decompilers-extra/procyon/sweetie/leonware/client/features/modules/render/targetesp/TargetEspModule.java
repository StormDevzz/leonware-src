// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp;

import lombok.Generated;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspCircle;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspGhost2;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspGhosts;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspCrystals;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspTexture;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Target Esp", category = Category.RENDER)
public class TargetEspModule extends Module
{
    public static final TargetEspModule instance;
    private final TargetEspTexture espTexture;
    private final TargetEspCrystals espCrystals;
    private final TargetEspGhosts espGhosts;
    private final TargetEspGhost2 espGhost2;
    private final TargetEspCircle espCircle;
    private TargetEspMode currentMode;
    private final ModeSetting mode;
    private final ModeSetting animation;
    private final SliderSetting duration;
    private final SliderSetting crystalsCount;
    private final SliderSetting crystalsSpeed;
    public final BooleanSetting lastPosition;
    
    public TargetEspModule() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/api/module/Module.<init>:()V
        //     4: aload_0         /* this */
        //     5: new             Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspTexture;
        //     8: dup            
        //     9: invokespecial   sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspTexture.<init>:()V
        //    12: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espTexture:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspTexture;
        //    15: aload_0         /* this */
        //    16: new             Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCrystals;
        //    19: dup            
        //    20: invokespecial   sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCrystals.<init>:()V
        //    23: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espCrystals:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCrystals;
        //    26: aload_0         /* this */
        //    27: new             Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhosts;
        //    30: dup            
        //    31: invokespecial   sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhosts.<init>:()V
        //    34: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espGhosts:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhosts;
        //    37: aload_0         /* this */
        //    38: new             Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhost2;
        //    41: dup            
        //    42: invokespecial   sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhost2.<init>:()V
        //    45: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espGhost2:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspGhost2;
        //    48: aload_0         /* this */
        //    49: new             Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCircle;
        //    52: dup            
        //    53: invokespecial   sweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCircle.<init>:()V
        //    56: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espCircle:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspCircle;
        //    59: aload_0         /* this */
        //    60: aload_0         /* this */
        //    61: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.espTexture:Lsweetie/leonware/client/features/modules/render/targetesp/modes/TargetEspTexture;
        //    64: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.currentMode:Lsweetie/leonware/client/features/modules/render/targetesp/TargetEspMode;
        //    67: aload_0         /* this */
        //    68: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //    71: dup            
        //    72: ldc             "Mode"
        //    74: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //    77: ldc             "Crystals"
        //    79: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    82: iconst_5       
        //    83: anewarray       Ljava/lang/String;
        //    86: dup            
        //    87: iconst_0       
        //    88: ldc             "Marker"
        //    90: aastore        
        //    91: dup            
        //    92: iconst_1       
        //    93: ldc             "Crystals"
        //    95: aastore        
        //    96: dup            
        //    97: iconst_2       
        //    98: ldc             "Ghosts"
        //   100: aastore        
        //   101: dup            
        //   102: iconst_3       
        //   103: ldc             "Ghost2"
        //   105: aastore        
        //   106: dup            
        //   107: iconst_4       
        //   108: ldc             "Circle"
        //   110: aastore        
        //   111: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   114: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   117: aload_0         /* this */
        //   118: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //   121: dup            
        //   122: ldc             "Animation"
        //   124: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //   127: ldc             "In"
        //   129: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   132: iconst_3       
        //   133: anewarray       Ljava/lang/String;
        //   136: dup            
        //   137: iconst_0       
        //   138: ldc             "In"
        //   140: aastore        
        //   141: dup            
        //   142: iconst_1       
        //   143: ldc             "Out"
        //   145: aastore        
        //   146: dup            
        //   147: iconst_2       
        //   148: ldc             "None"
        //   150: aastore        
        //   151: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/String;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   154: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.animation:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   157: aload_0         /* this */
        //   158: new             Lsweetie/leonware/api/module/setting/SliderSetting;
        //   161: dup            
        //   162: ldc             "Duration"
        //   164: invokespecial   sweetie/leonware/api/module/setting/SliderSetting.<init>:(Ljava/lang/String;)V
        //   167: ldc             3.0
        //   169: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   172: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.value:(Ljava/lang/Float;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   175: fconst_1       
        //   176: ldc             20.0
        //   178: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.range:(FF)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   181: fconst_1       
        //   182: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.step:(F)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   185: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.duration:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   188: aload_0         /* this */
        //   189: new             Lsweetie/leonware/api/module/setting/SliderSetting;
        //   192: dup            
        //   193: ldc             "Amount"
        //   195: invokespecial   sweetie/leonware/api/module/setting/SliderSetting.<init>:(Ljava/lang/String;)V
        //   198: ldc             14.0
        //   200: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   203: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.value:(Ljava/lang/Float;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   206: fconst_1       
        //   207: ldc             36.0
        //   209: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.range:(FF)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   212: fconst_1       
        //   213: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.step:(F)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   216: aload_0         /* this */
        //   217: invokedynamic   BootstrapMethod #0, get:(Lsweetie/leonware/client/features/modules/render/targetesp/TargetEspModule;)Ljava/util/function/Supplier;
        //   222: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   225: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.crystalsCount:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   228: aload_0         /* this */
        //   229: new             Lsweetie/leonware/api/module/setting/SliderSetting;
        //   232: dup            
        //   233: ldc             "Speed"
        //   235: invokespecial   sweetie/leonware/api/module/setting/SliderSetting.<init>:(Ljava/lang/String;)V
        //   238: ldc             3.0
        //   240: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   243: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.value:(Ljava/lang/Float;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   246: fconst_0       
        //   247: ldc             5.0
        //   249: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.range:(FF)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   252: ldc             0.5
        //   254: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.step:(F)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   257: aload_0         /* this */
        //   258: invokedynamic   BootstrapMethod #1, get:(Lsweetie/leonware/client/features/modules/render/targetesp/TargetEspModule;)Ljava/util/function/Supplier;
        //   263: invokevirtual   sweetie/leonware/api/module/setting/SliderSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/SliderSetting;
        //   266: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.crystalsSpeed:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   269: aload_0         /* this */
        //   270: new             Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   273: dup            
        //   274: ldc             "Last position"
        //   276: invokespecial   sweetie/leonware/api/module/setting/BooleanSetting.<init>:(Ljava/lang/String;)V
        //   279: iconst_1       
        //   280: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   283: invokevirtual   sweetie/leonware/api/module/setting/BooleanSetting.value:(Ljava/lang/Boolean;)Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   286: putfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.lastPosition:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   289: aload_0         /* this */
        //   290: bipush          6
        //   292: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //   295: dup            
        //   296: iconst_0       
        //   297: aload_0         /* this */
        //   298: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   301: aastore        
        //   302: dup            
        //   303: iconst_1       
        //   304: aload_0         /* this */
        //   305: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.animation:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   308: aastore        
        //   309: dup            
        //   310: iconst_2       
        //   311: aload_0         /* this */
        //   312: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.duration:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   315: aastore        
        //   316: dup            
        //   317: iconst_3       
        //   318: aload_0         /* this */
        //   319: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.crystalsCount:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   322: aastore        
        //   323: dup            
        //   324: iconst_4       
        //   325: aload_0         /* this */
        //   326: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.crystalsSpeed:Lsweetie/leonware/api/module/setting/SliderSetting;
        //   329: aastore        
        //   330: dup            
        //   331: iconst_5       
        //   332: aload_0         /* this */
        //   333: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.lastPosition:Lsweetie/leonware/api/module/setting/BooleanSetting;
        //   336: aastore        
        //   337: invokevirtual   sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //   340: aload_0         /* this */
        //   341: getfield        sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.mode:Lsweetie/leonware/api/module/setting/ModeSetting;
        //   344: aload_0         /* this */
        //   345: invokedynamic   BootstrapMethod #2, run:(Lsweetie/leonware/client/features/modules/render/targetesp/TargetEspModule;)Ljava/lang/Runnable;
        //   350: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //   353: pop            
        //   354: return         
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
    public void onEvent() {
        this.addEvents(Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            TargetEspMode.updatePositions();
            this.currentMode.onRender3D(event);
        })), UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            this.currentMode.updateAnimation(this.duration.getValue().longValue() * 50L, this.animation.getValue(), 1.0f, 0.0f, 2.0f);
            this.currentMode.updateTarget();
            this.currentMode.onUpdate();
        })));
    }
    
    public int getCrystalsCount() {
        return this.crystalsCount.getValue().intValue();
    }
    
    public float getCrystalsSpeed() {
        return this.crystalsSpeed.getValue();
    }
    
    @Generated
    public static TargetEspModule getInstance() {
        return TargetEspModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new TargetEspModule();
    }
}
