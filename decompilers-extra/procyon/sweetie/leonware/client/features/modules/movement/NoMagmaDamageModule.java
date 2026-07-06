// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import net.minecraft.class_2680;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2246;
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.events.client.PacketEvent;
import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Magma Damage", category = Category.MOVEMENT)
public class NoMagmaDamageModule extends Module
{
    private static final NoMagmaDamageModule instance;
    private final ModeSetting mode;
    private boolean wasOnMagma;
    private int jumpTicks;
    
    public NoMagmaDamageModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u0421\u043f\u0443\u0444 \u041d\u0430 \u0417\u0435\u043c\u043b\u0435", "\u0421\u0431\u0440\u043e\u0441", "\u0421\u043a\u0440\u044b\u0442\u043d\u044b\u0439 \u0421\u043f\u0443\u0444").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
        this.wasOnMagma = false;
        this.jumpTicks = 0;
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }
    
    @Override
    public void onEvent() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: new             Lsweetie/leonware/api/event/Listener;
        //     6: dup            
        //     7: aload_0         /* this */
        //     8: invokedynamic   BootstrapMethod #0, accept:(Lsweetie/leonware/client/features/modules/movement/NoMagmaDamageModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/player/other/UpdateEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* updateEvent */
        //    20: invokestatic    sweetie/leonware/api/event/events/client/PacketEvent.getInstance:()Lsweetie/leonware/api/event/events/client/PacketEvent;
        //    23: new             Lsweetie/leonware/api/event/Listener;
        //    26: dup            
        //    27: aload_0         /* this */
        //    28: invokedynamic   BootstrapMethod #1, accept:(Lsweetie/leonware/client/features/modules/movement/NoMagmaDamageModule;)Ljava/util/function/Consumer;
        //    33: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    36: invokevirtual   sweetie/leonware/api/event/events/client/PacketEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    39: astore_2        /* packetEvent */
        //    40: aload_0         /* this */
        //    41: iconst_2       
        //    42: anewarray       Lsweetie/leonware/api/event/EventListener;
        //    45: dup            
        //    46: iconst_0       
        //    47: aload_1         /* updateEvent */
        //    48: aastore        
        //    49: dup            
        //    50: iconst_1       
        //    51: aload_2         /* packetEvent */
        //    52: aastore        
        //    53: invokevirtual   sweetie/leonware/client/features/modules/movement/NoMagmaDamageModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
        //    56: return         
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
    
    @Generated
    public static NoMagmaDamageModule getInstance() {
        return NoMagmaDamageModule.instance;
    }
    
    static {
        instance = new NoMagmaDamageModule();
    }
}
