// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import java.util.Arrays;
import net.minecraft.class_1269;
import net.minecraft.class_1268;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2246;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1747;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_3965;
import net.minecraft.class_3532;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2828;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.ScaffoldLegitRotation;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import net.minecraft.class_2248;
import java.util.List;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Scaffold", category = Category.PLAYER)
public class ScaffoldModule extends Module
{
    private static final ScaffoldModule instance;
    private static final List<class_2248> BLACKLIST;
    private final ModeSetting rotationMode;
    private final SliderSetting placeDelay;
    private final BooleanSetting keepY;
    private final BooleanSetting safeWalk;
    private final BooleanSetting placeInAir;
    private final TimerUtil placeTimer;
    private int originalSlot;
    private int tickDelay;
    private float savedY;
    
    public ScaffoldModule() {
        this.rotationMode = new ModeSetting("Rotation Mode").value("Silent").values("None", "Packet", "Silent");
        this.placeDelay = new SliderSetting("Place Delay").value(50.0f).range(0.0f, 200.0f).step(5.0f);
        this.keepY = new BooleanSetting("Keep Y").value(true);
        this.safeWalk = new BooleanSetting("Safe Walk").value(true);
        this.placeInAir = new BooleanSetting("Place in Air").value(false).setVisible(() -> this.rotationMode.is("None"));
        this.placeTimer = new TimerUtil();
        this.originalSlot = -1;
        this.tickDelay = 0;
        this.savedY = 0.0f;
        this.addSettings(this.rotationMode, this.placeDelay, this.keepY, this.safeWalk, this.placeInAir);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ScaffoldModule.mc.field_1724 == null || ScaffoldModule.mc.field_1687 == null) {
            this.setEnabled(false);
            return;
        }
        this.originalSlot = ScaffoldModule.mc.field_1724.method_31548().field_7545;
        this.savedY = (float)Math.floor(ScaffoldModule.mc.field_1724.method_23318() - 1.0);
        this.tickDelay = 0;
        this.placeTimer.reset();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (ScaffoldModule.mc.field_1724 != null && this.originalSlot != -1) {
            ScaffoldModule.mc.field_1724.method_31548().field_7545 = this.originalSlot;
        }
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
        //     8: invokedynamic   BootstrapMethod #1, accept:(Lsweetie/leonware/client/features/modules/player/ScaffoldModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/player/other/UpdateEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* updateEvent */
        //    20: invokestatic    sweetie/leonware/api/event/events/player/other/MovementInputEvent.getInstance:()Lsweetie/leonware/api/event/events/player/other/MovementInputEvent;
        //    23: new             Lsweetie/leonware/api/event/Listener;
        //    26: dup            
        //    27: aload_0         /* this */
        //    28: invokedynamic   BootstrapMethod #2, accept:(Lsweetie/leonware/client/features/modules/player/ScaffoldModule;)Ljava/util/function/Consumer;
        //    33: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    36: invokevirtual   sweetie/leonware/api/event/events/player/other/MovementInputEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    39: astore_2        /* moveInputEvent */
        //    40: aload_0         /* this */
        //    41: iconst_2       
        //    42: anewarray       Lsweetie/leonware/api/event/EventListener;
        //    45: dup            
        //    46: iconst_0       
        //    47: aload_1         /* updateEvent */
        //    48: aastore        
        //    49: dup            
        //    50: iconst_1       
        //    51: aload_2         /* moveInputEvent */
        //    52: aastore        
        //    53: invokevirtual   sweetie/leonware/client/features/modules/player/ScaffoldModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
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
    
    private void applySilentRotation(final Rotation rotation) {
        final RotationStrategy strategy = new RotationStrategy(new ScaffoldLegitRotation(), true).ticksUntilReset(3);
        RotationManager.getInstance().addRotation(rotation, strategy, TaskPriority.NORMAL, this);
    }
    
    private void applyPacketRotation(final Rotation rotation) {
        final class_243 playerPos = ScaffoldModule.mc.field_1724.method_19538();
        final class_2828.class_2830 packet = new class_2828.class_2830(playerPos.field_1352, playerPos.field_1351, playerPos.field_1350, rotation.getYaw(), rotation.getPitch(), ScaffoldModule.mc.field_1724.method_24828(), ScaffoldModule.mc.field_1724.field_5976);
        NetworkUtil.sendPacket((class_2596<?>)packet);
    }
    
    private class_2338 getPredictedPos() {
        final class_243 vel = ScaffoldModule.mc.field_1724.method_18798();
        final int dx = (int)Math.round(vel.field_1352);
        final int dz = (int)Math.round(vel.field_1350);
        int targetY;
        if (this.keepY.getValue()) {
            targetY = (int)this.savedY;
        }
        else {
            targetY = class_3532.method_15357(ScaffoldModule.mc.field_1724.method_23318() - 1.0);
        }
        final class_2338 playerPos = ScaffoldModule.mc.field_1724.method_24515();
        return new class_2338(playerPos.method_10263() + dx, targetY, playerPos.method_10260() + dz);
    }
    
    private class_3965 createAirPlacement(final class_2338 target) {
        final class_243 hitVec = class_243.method_24953((class_2382)target);
        return new class_3965(hitVec, class_2350.field_11036, target, false);
    }
    
    private int findInventoryBlock() {
        for (int i = 0; i < 27; ++i) {
            final class_1799 stack = ScaffoldModule.mc.field_1724.method_31548().method_5438(i + 9);
            if (stack.method_7947() > 0) {
                final class_1792 method_7909 = stack.method_7909();
                if (method_7909 instanceof final class_1747 blockItem) {
                    if (!ScaffoldModule.BLACKLIST.contains(blockItem.method_7711())) {
                        return i + 9;
                    }
                }
            }
        }
        return -1;
    }
    
    private int findBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            final class_1799 stack = ScaffoldModule.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7947() > 0) {
                final class_1792 method_7909 = stack.method_7909();
                if (method_7909 instanceof final class_1747 blockItem) {
                    if (!ScaffoldModule.BLACKLIST.contains(blockItem.method_7711())) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    private class_3965 findHit(final class_2338 target) {
        final class_2350[] array;
        final class_2350[] faces = array = new class_2350[] { class_2350.field_11033, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034 };
        for (final class_2350 face : array) {
            final class_2338 neighbour = target.method_10093(face);
            if (!ScaffoldModule.mc.field_1687.method_8320(neighbour).method_26215()) {
                final class_243 hitVec = class_243.method_24953((class_2382)neighbour).method_1019(class_243.method_24954(face.method_62675()).method_1021(0.5));
                return new class_3965(hitVec, face.method_10153(), neighbour, false);
            }
        }
        return null;
    }
    
    public boolean isSafeWalk() {
        return this.isEnabled() && this.safeWalk.getValue();
    }
    
    @Generated
    public static ScaffoldModule getInstance() {
        return ScaffoldModule.instance;
    }
    
    static {
        instance = new ScaffoldModule();
        BLACKLIST = Arrays.asList(class_2246.field_10034, class_2246.field_10443, class_2246.field_10380, class_2246.field_10102, class_2246.field_10534, class_2246.field_10255, class_2246.field_10029, class_2246.field_10343, class_2246.field_9980, class_2246.field_10181, class_2246.field_16333, class_2246.field_16334, class_2246.field_10158, class_2246.field_10484, class_2246.field_10592, class_2246.field_10332, class_2246.field_10026, class_2246.field_10397, class_2246.field_10470, class_2246.field_22130, class_2246.field_22131, class_2246.field_10081, class_2246.field_10535, class_2246.field_10105, class_2246.field_10414, class_2246.field_10327, class_2246.field_10394, class_2246.field_10575, class_2246.field_10217, class_2246.field_10276, class_2246.field_10385, class_2246.field_10160, class_2246.field_10336, class_2246.field_10099, class_2246.field_10363, class_2246.field_10057, class_2246.field_10494, class_2246.field_10214, class_2246.field_10479, class_2246.field_10428, class_2246.field_10477, class_2246.field_21211);
    }
}
