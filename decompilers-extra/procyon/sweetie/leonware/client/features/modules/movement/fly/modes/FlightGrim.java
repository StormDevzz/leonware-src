// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.fly.modes;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.movement.fly.FlightModule;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

public class FlightGrim extends FlightMode
{
    public BypassType bypassType;
    private final FlightModule module;
    private TimerUtil ticks;
    private long speedRampStartTime;
    private boolean isSpeedRamping;
    private final ModeSetting grimType;
    
    @Override
    public String getName() {
        return "Grim";
    }
    
    public FlightGrim(final Supplier<Boolean> condition, final FlightModule module) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   sweetie/leonware/client/features/modules/movement/fly/FlightMode.<init>:()V
        //     4: aload_0         /* this */
        //     5: new             Lsweetie/leonware/api/utils/math/TimerUtil;
        //     8: dup            
        //     9: invokespecial   sweetie/leonware/api/utils/math/TimerUtil.<init>:()V
        //    12: putfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.ticks:Lsweetie/leonware/api/utils/math/TimerUtil;
        //    15: aload_0         /* this */
        //    16: lconst_0       
        //    17: putfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.speedRampStartTime:J
        //    20: aload_0         /* this */
        //    21: iconst_0       
        //    22: putfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.isSpeedRamping:Z
        //    25: aload_0         /* this */
        //    26: new             Lsweetie/leonware/api/module/setting/ModeSetting;
        //    29: dup            
        //    30: ldc             "Grim mode"
        //    32: invokespecial   sweetie/leonware/api/module/setting/ModeSetting.<init>:(Ljava/lang/String;)V
        //    35: getstatic       sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim$BypassType.VERTICAL_ELYTRA:Lsweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim$BypassType;
        //    38: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.value:(Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    41: invokestatic    sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim$BypassType.values:()[Lsweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim$BypassType;
        //    44: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.values:([Ljava/lang/Enum;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    47: aload_0         /* this */
        //    48: invokedynamic   BootstrapMethod #0, run:(Lsweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim;)Ljava/lang/Runnable;
        //    53: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.onAction:(Ljava/lang/Runnable;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    56: putfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //    59: aload_0         /* this */
        //    60: getfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //    63: aload_1         /* condition */
        //    64: invokevirtual   sweetie/leonware/api/module/setting/ModeSetting.setVisible:(Ljava/util/function/Supplier;)Lsweetie/leonware/api/module/setting/ModeSetting;
        //    67: pop            
        //    68: aload_0         /* this */
        //    69: aload_2         /* module */
        //    70: putfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.module:Lsweetie/leonware/client/features/modules/movement/fly/FlightModule;
        //    73: aload_0         /* this */
        //    74: iconst_1       
        //    75: anewarray       Lsweetie/leonware/api/module/setting/Setting;
        //    78: dup            
        //    79: iconst_0       
        //    80: aload_0         /* this */
        //    81: getfield        sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.grimType:Lsweetie/leonware/api/module/setting/ModeSetting;
        //    84: aastore        
        //    85: invokevirtual   sweetie/leonware/client/features/modules/movement/fly/modes/FlightGrim.addSettings:([Lsweetie/leonware/api/module/setting/Setting;)V
        //    88: return         
        //    Signature:
        //  (Ljava/util/function/Supplier<Ljava/lang/Boolean;>;Lsweetie/leonware/client/features/modules/movement/fly/FlightModule;)V
        //    MethodParameters:
        //  Name       Flags  
        //  ---------  -----
        //  condition  
        //  module     
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
    public void onUpdate() {
        if (this.bypassType == BypassType.GLIDE_ELYTRA) {
            return;
        }
        if (FlightGrim.mc.field_1724.method_6128() && (FlightGrim.mc.field_1724.method_18798().field_1351 > 0.08 || FlightGrim.mc.field_1724.field_6017 > 0.1f) && FlightGrim.mc.field_1724.method_18798().field_1352 <= 0.01 && FlightGrim.mc.field_1724.method_18798().field_1350 <= 0.01) {
            final RotationStrategy rotationStrategy = new RotationStrategy(new SmoothRotation(), true);
            FlightGrim.mc.field_1724.method_18798().field_1350 = 0.0;
            FlightGrim.mc.field_1724.method_18798().field_1352 = 0.0;
            final RotationManager rotationManager = RotationManager.getInstance();
            final Rotation rotation = rotationManager.getRotation();
            final RotationPlan configurable = rotationManager.getCurrentRotationPlan();
            final float pitch = (configurable != null) ? rotation.getPitch() : FlightGrim.mc.field_1724.method_36455();
            final boolean validPitch = FlightGrim.mc.field_1724.method_36455() >= -30.0f && FlightGrim.mc.field_1724.method_36455() <= 30.0f;
            if (!this.isSpeedRamping) {
                this.speedRampStartTime = System.currentTimeMillis();
                this.isSpeedRamping = true;
            }
            final long rampDuration = 100L;
            final long elapsed = System.currentTimeMillis() - this.speedRampStartTime;
            final float progress = Math.min(elapsed / (float)rampDuration, 1.0f);
            final double currentBaseSpeed = 0.05 * progress;
            final double maxAddedSpeed = 0.06;
            final double maxVerticalSpeed = 1.11;
            final float normalizedPitch = pitch / 90.0f;
            final double speedAddition = maxAddedSpeed * normalizedPitch * normalizedPitch;
            final double superKuniMan = currentBaseSpeed + speedAddition;
            final class_243 method_18798 = FlightGrim.mc.field_1724.method_18798();
            method_18798.field_1351 += superKuniMan;
            if (FlightGrim.mc.field_1724.method_18798().field_1351 >= maxVerticalSpeed) {
                FlightGrim.mc.field_1724.method_18798().field_1351 = maxVerticalSpeed;
            }
            if (!validPitch) {
                RotationManager.getInstance().addRotation(new Rotation(FlightGrim.mc.field_1724.method_36454(), 0.0f), rotationStrategy, TaskPriority.NORMAL, this.module);
            }
        }
        else {
            this.isSpeedRamping = false;
        }
    }
    
    @Override
    public void onMotion(final MotionEvent.MotionEventData event) {
        if (this.bypassType == BypassType.VERTICAL_ELYTRA || !FlightGrim.mc.field_1724.method_6128()) {
            return;
        }
        final class_243 pos = FlightGrim.mc.field_1724.method_19538();
        final float yaw = FlightGrim.mc.field_1724.method_36454();
        double forward = 6.087;
        double motion = MathUtil.getEntityBPS((class_1297)FlightGrim.mc.field_1724);
        final float doni = (FlightGrim.mc.method_1562().method_45734() != null && FlightGrim.mc.method_1562().method_45734().field_3761.contains("reallyworld")) ? 48.0f : 52.0f;
        if (motion >= doni) {
            forward = 0.0;
            motion = 0.0;
        }
        final double dx = -Math.sin(Math.toRadians(yaw)) * forward;
        final double dz = Math.cos(Math.toRadians(yaw)) * forward;
        FlightGrim.mc.field_1724.method_18800(dx * MathUtil.randomInRange(1.1f, 1.21f), FlightGrim.mc.field_1724.method_18798().field_1351 - 0.019999999552965164, dz * MathUtil.randomInRange(1.1f, 1.21f));
        if (this.ticks.finished(50L)) {
            FlightGrim.mc.field_1724.method_5814(pos.field_1352 + dx, pos.field_1351, pos.field_1350 + dz);
            this.ticks.reset();
        }
        FlightGrim.mc.field_1724.method_18800(dx * MathUtil.randomInRange(1.1f, 1.21f), FlightGrim.mc.field_1724.method_18798().field_1351 + 0.01600000075995922, dz * MathUtil.randomInRange(1.1f, 1.21f));
    }
    
    @Generated
    public ModeSetting getGrimType() {
        return this.grimType;
    }
    
    public enum BypassType implements ModeSetting.NamedChoice
    {
        VERTICAL_ELYTRA("Vertical elytra"), 
        GLIDE_ELYTRA("Glide elytra");
        
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
