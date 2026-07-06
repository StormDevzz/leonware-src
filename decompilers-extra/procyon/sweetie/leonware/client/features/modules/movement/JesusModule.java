// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.events.client.PacketEvent;
import lombok.Generated;
import net.minecraft.class_2680;
import net.minecraft.class_238;
import net.minecraft.class_3532;
import net.minecraft.class_2404;
import net.minecraft.class_2338;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Jesus", category = Category.MOVEMENT)
public class JesusModule extends Module
{
    private static final JesusModule instance;
    public final ModeSetting mode;
    public final BooleanSetting glide;
    public final BooleanSetting strict;
    public final BooleanSetting boost;
    public final SliderSetting matrixSpeed;
    public final BooleanSetting matrixBoost;
    public final SliderSetting boostSpeed;
    public final SliderSetting boostTicks;
    public final SliderSetting motionUp;
    private boolean jumping;
    private int glideCounter;
    private float lastOffset;
    
    public JesusModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("Solid").values("Solid", "Trampoline", "Dolphin", "Matrix Ground", "Matrix Zoom", "Matrix Solid");
        this.glide = new BooleanSetting("\u0413\u043b\u0430\u0439\u0434").value(false).setVisible(() -> this.mode.is("Solid"));
        this.strict = new BooleanSetting("\u0421\u0442\u0440\u043e\u0433\u0438\u0439").value(false).setVisible(() -> this.mode.is("Solid"));
        this.boost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u0432 \u043b\u0430\u0432\u0435").value(false).setVisible(() -> this.mode.is("Trampoline"));
        this.matrixSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(1.0f).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.mode.is("Matrix Ground") || this.mode.is("Matrix Solid") || this.mode.is("Matrix Zoom"));
        this.matrixBoost = new BooleanSetting("\u0411\u0443\u0441\u0442").value(false).setVisible(() -> this.mode.is("Matrix Ground"));
        this.boostSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(1.35f).range(0.1f, 4.0f).step(0.01f).setVisible(() -> this.mode.is("Matrix Ground") && this.matrixBoost.getValue());
        this.boostTicks = new SliderSetting("\u0422\u0438\u043a\u0438 \u0431\u0443\u0441\u0442\u0430").value(2.0f).range(1.0f, 30.0f).step(1.0f).setVisible(() -> this.mode.is("Matrix Ground") && this.matrixBoost.getValue());
        this.motionUp = new SliderSetting("Motion Up").value(0.42f).range(0.1f, 2.0f).step(0.01f).setVisible(() -> this.mode.is("Solid"));
        this.jumping = false;
        this.glideCounter = 0;
        this.lastOffset = 0.0f;
        this.addSettings(this.mode, this.glide, this.strict, this.boost, this.matrixSpeed, this.matrixBoost, this.boostSpeed, this.boostTicks, this.motionUp);
    }
    
    @Override
    public void onDisable() {
        this.jumping = false;
        this.glideCounter = 0;
        this.lastOffset = 0.0f;
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
        //     8: invokedynamic   BootstrapMethod #8, accept:(Lsweetie/leonware/client/features/modules/movement/JesusModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/player/other/UpdateEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* updateEvent */
        //    20: invokestatic    sweetie/leonware/api/event/events/client/PacketEvent.getInstance:()Lsweetie/leonware/api/event/events/client/PacketEvent;
        //    23: new             Lsweetie/leonware/api/event/Listener;
        //    26: dup            
        //    27: aload_0         /* this */
        //    28: invokedynamic   BootstrapMethod #9, accept:(Lsweetie/leonware/client/features/modules/movement/JesusModule;)Ljava/util/function/Consumer;
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
        //    53: invokevirtual   sweetie/leonware/client/features/modules/movement/JesusModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
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
    
    private boolean isOnLiquidBlock() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        final class_2338 pos = class_2338.method_49637(JesusModule.mc.field_1724.method_23317(), JesusModule.mc.field_1724.method_23318() - 0.1, JesusModule.mc.field_1724.method_23321());
        return JesusModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2404;
    }
    
    public static boolean isInLiquid() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        if (JesusModule.mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        final class_238 bb = JesusModule.mc.field_1724.method_5829();
        final int y = (int)bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357(bb.field_1323); x <= class_3532.method_15357(bb.field_1320); ++x) {
            for (int z = class_3532.method_15357(bb.field_1321); z <= class_3532.method_15357(bb.field_1324); ++z) {
                final class_2680 state = JesusModule.mc.field_1687.method_8320(new class_2338(x, y, z));
                if (!state.method_26215()) {
                    if (!(state.method_26204() instanceof class_2404)) {
                        return false;
                    }
                    found = true;
                }
            }
        }
        return found;
    }
    
    public static boolean isOnLiquid() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        if (JesusModule.mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        final class_238 bb = JesusModule.mc.field_1724.method_5829().method_989(0.0, -0.05, 0.0);
        final int y = (int)bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357(bb.field_1323); x <= class_3532.method_15357(bb.field_1320); ++x) {
            for (int z = class_3532.method_15357(bb.field_1321); z <= class_3532.method_15357(bb.field_1324); ++z) {
                final class_2680 state = JesusModule.mc.field_1687.method_8320(new class_2338(x, y, z));
                if (!state.method_26215()) {
                    if (!(state.method_26204() instanceof class_2404)) {
                        return false;
                    }
                    found = true;
                }
            }
        }
        return found;
    }
    
    public static boolean checkCollide() {
        return JesusModule.mc.field_1724 != null && !JesusModule.mc.field_1724.method_5715() && (!JesusModule.mc.field_1724.method_5765() || JesusModule.mc.field_1724.method_5854() == null || JesusModule.mc.field_1724.method_5854().field_6017 < 3.0f) && JesusModule.mc.field_1724.field_6017 <= 3.0f;
    }
    
    @Generated
    public static JesusModule getInstance() {
        return JesusModule.instance;
    }
    
    static {
        instance = new JesusModule();
    }
}
