// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import java.util.Iterator;
import net.minecraft.class_3414;
import net.minecraft.class_1309;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_3419;
import sweetie.leonware.api.utils.other.SoundUtil;
import sweetie.leonware.api.event.events.client.TickEvent;
import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashSet;
import java.util.Set;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Death Sounds", category = Category.OTHER)
public class DeathSoundsModule extends Module
{
    private static final DeathSoundsModule instance;
    private final ModeSetting sound;
    private final SliderSetting volume;
    private final BooleanSetting onlyPlayers;
    private final Set<Integer> deadIds;
    
    public DeathSoundsModule() {
        this.sound = new ModeSetting("Sound").value("Schoolboy").values("Schoolboy", "Schoolboy 2", "Wasted");
        this.volume = new SliderSetting("Volume").value(60.0f).range(1.0f, 100.0f).step(1.0f);
        this.onlyPlayers = new BooleanSetting("Only Players").value(true);
        this.deadIds = new HashSet<Integer>();
        this.addSettings(this.sound, this.volume, this.onlyPlayers);
    }
    
    @Override
    public void onDisable() {
        this.deadIds.clear();
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
        //     8: invokedynamic   BootstrapMethod #0, accept:(Lsweetie/leonware/client/features/modules/other/DeathSoundsModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/client/TickEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* tickEvent */
        //    20: aload_0         /* this */
        //    21: iconst_1       
        //    22: anewarray       Lsweetie/leonware/api/event/EventListener;
        //    25: dup            
        //    26: iconst_0       
        //    27: aload_1         /* tickEvent */
        //    28: aastore        
        //    29: invokevirtual   sweetie/leonware/client/features/modules/other/DeathSoundsModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
        //    32: return         
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
    public static DeathSoundsModule getInstance() {
        return DeathSoundsModule.instance;
    }
    
    static {
        instance = new DeathSoundsModule();
    }
}
