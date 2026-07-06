// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import java.util.List;
import java.util.Collection;
import net.minecraft.class_640;
import java.util.ArrayList;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Random;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Spammer", category = Category.PLAYER)
public class SpammerModule extends Module
{
    private static final SpammerModule instance;
    private final SliderSetting delay;
    private final ModeSetting mode;
    private String spamText;
    private final TimerUtil timer;
    private final Random random;
    
    public SpammerModule() {
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u0441\u0435\u043a)").value(3.0f).range(0.1f, 10.0f).step(0.1f);
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439", "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u044b\u0439", "\u0412 \u043b\u0441").value("\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439");
        this.spamText = "LeonWare - \u043b\u0443\u0447\u0448\u0438\u0439 \u0441\u043e\u0444\u0442!";
        this.timer = new TimerUtil();
        this.random = new Random();
        this.addSettings(this.delay, this.mode);
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
        //     8: invokedynamic   BootstrapMethod #0, accept:(Lsweetie/leonware/client/features/modules/player/SpammerModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/player/other/UpdateEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* updateEvent */
        //    20: aload_0         /* this */
        //    21: iconst_1       
        //    22: anewarray       Lsweetie/leonware/api/event/EventListener;
        //    25: dup            
        //    26: iconst_0       
        //    27: aload_1         /* updateEvent */
        //    28: aastore        
        //    29: invokevirtual   sweetie/leonware/client/features/modules/player/SpammerModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
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
    public static SpammerModule getInstance() {
        return SpammerModule.instance;
    }
    
    @Generated
    public String getSpamText() {
        return this.spamText;
    }
    
    @Generated
    public void setSpamText(final String spamText) {
        this.spamText = spamText;
    }
    
    static {
        instance = new SpammerModule();
    }
}
