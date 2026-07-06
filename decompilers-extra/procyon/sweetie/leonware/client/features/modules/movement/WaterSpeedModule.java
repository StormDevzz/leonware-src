// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2708;
import sweetie.leonware.api.event.events.client.PacketEvent;
import net.minecraft.class_2680;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import java.util.Iterator;
import net.minecraft.class_238;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Water Speed", category = Category.MOVEMENT)
public class WaterSpeedModule extends Module
{
    private static final WaterSpeedModule instance;
    private final ModeSetting mode;
    private final ModeSetting wallMode;
    private final SliderSetting ftSpeed;
    private final SliderSetting vanillaSpeed;
    private final SliderSetting ftBoost;
    private final BooleanSetting offInUse;
    private final BooleanSetting hvhBoost;
    private final BooleanSetting hvhUpDown;
    private final BooleanSetting hvhAntiFlag;
    private final SliderSetting hvhGeneralSpeed;
    private final SliderSetting hvhBoostCooldown;
    private final SliderSetting hvhBoostDuration;
    private final SliderSetting hvhBoostSpeed;
    private final SliderSetting hvhUpDownSpeed;
    private final SliderSetting hvhAntiFlagTime;
    private final BooleanSetting hvhTarget;
    private final SliderSetting hvhTargetDistance;
    private final BooleanSetting hvhVerticalTarget;
    private final TimerUtil timer;
    private double wallVerticalBoost;
    private double wallRadius;
    private double wallBoost;
    private float currentSpeed;
    private boolean boostActive;
    private long lastBoostTime;
    private long boostEndTime;
    private boolean antiFlagTriggered;
    private long flagTriggerTime;
    private float boostMultiplier;
    
    public WaterSpeedModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Intave", "Vanilla", "Wall", "HvH", "SprintE").value("Intave");
        this.wallMode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c \u043e\u0442 \u0441\u0442\u0435\u043d\u044b").values("MetaHvH", "FunTime").value("MetaHvH").setVisible(() -> this.mode.is("Wall"));
        this.ftSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c FunTime").value(0.2f).range(0.1f, 0.5f).step(0.01f).setVisible(() -> this.mode.is("Wall") && this.wallMode.is("FunTime"));
        this.vanillaSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(0.4f).range(0.1f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("Vanilla"));
        this.ftBoost = new SliderSetting("\u0411\u0443\u0441\u0442 \u043a \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u0438").value(1.0357f).range(1.0f, 1.1f).step(5.0E-4f).setVisible(() -> this.mode.is("Intave"));
        this.offInUse = new BooleanSetting("\u0421\u0442\u043e\u043f \u0435\u0441\u043b\u0438 \u043a\u0443\u0448\u0430\u0435\u043c").value(true).setVisible(() -> this.mode.is("Intave"));
        this.hvhBoost = new BooleanSetting("\u0411\u0443\u0441\u0442").value(true).setVisible(() -> this.mode.is("HvH"));
        this.hvhUpDown = new BooleanSetting("\u0412\u0432\u0435\u0440\u0445-\u0432\u043d\u0438\u0437").value(false).setVisible(() -> this.mode.is("HvH"));
        this.hvhAntiFlag = new BooleanSetting("\u0410\u043d\u0442\u0438\u0424\u043b\u0430\u0433").value(true).setVisible(() -> this.mode.is("HvH"));
        this.hvhGeneralSpeed = new SliderSetting("\u041e\u0431\u0449\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(1.0f).range(0.0f, 2.0f).step(0.05f).setVisible(() -> this.mode.is("HvH"));
        this.hvhBoostCooldown = new SliderSetting("\u041a\u0443\u043b\u0434\u0430\u0443\u043d \u0431\u0443\u0441\u0442\u0430").value(650.0f).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> this.mode.is("HvH") && this.hvhBoost.getValue());
        this.hvhBoostDuration = new SliderSetting("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(350.0f).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> this.mode.is("HvH") && this.hvhBoost.getValue());
        this.hvhBoostSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(0.17f).range(0.05f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("HvH") && this.hvhBoost.getValue());
        this.hvhUpDownSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u0432\u0435\u0440\u0445/\u0432\u043d\u0438\u0437").value(0.2f).range(0.05f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("HvH") && this.hvhUpDown.getValue());
        this.hvhAntiFlagTime = new SliderSetting("\u0412\u0440\u0435\u043c\u044f \u0410\u043d\u0442\u0438\u0424\u043b\u0430\u0433\u0430").value(7.0f).range(0.5f, 10.0f).step(0.1f).setVisible(() -> this.mode.is("HvH") && this.hvhAntiFlag.getValue());
        this.hvhTarget = new BooleanSetting("\u0422\u0430\u0440\u0433\u0435\u0442").value(false).setVisible(() -> this.mode.is("HvH"));
        this.hvhTargetDistance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0434\u043e \u0446\u0435\u043b\u0438").value(1.0f).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.mode.is("HvH") && this.hvhTarget.getValue());
        this.hvhVerticalTarget = new BooleanSetting("\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u044b\u0439 \u0442\u0430\u0440\u0433\u0435\u0442").value(true).setVisible(() -> this.mode.is("HvH") && this.hvhTarget.getValue());
        this.timer = new TimerUtil();
        this.wallVerticalBoost = 0.05;
        this.currentSpeed = 0.0f;
        this.boostActive = false;
        this.lastBoostTime = 0L;
        this.boostEndTime = 0L;
        this.antiFlagTriggered = false;
        this.flagTriggerTime = 0L;
        this.boostMultiplier = 0.0f;
        this.addSettings(this.mode, this.wallMode, this.ftSpeed, this.vanillaSpeed, this.ftBoost, this.offInUse, this.hvhBoost, this.hvhUpDown, this.hvhAntiFlag, this.hvhGeneralSpeed, this.hvhBoostCooldown, this.hvhBoostDuration, this.hvhBoostSpeed, this.hvhUpDownSpeed, this.hvhAntiFlagTime, this.hvhTarget, this.hvhTargetDistance, this.hvhVerticalTarget);
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
        this.resetHvH();
    }
    
    @Override
    public void onDisable() {
        this.resetHvH();
    }
    
    private void resetHvH() {
        this.boostActive = false;
        this.boostMultiplier = 0.0f;
        this.lastBoostTime = 0L;
        this.boostEndTime = 0L;
        this.antiFlagTriggered = false;
        this.flagTriggerTime = 0L;
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
        //     8: invokedynamic   BootstrapMethod #17, accept:(Lsweetie/leonware/client/features/modules/movement/WaterSpeedModule;)Ljava/util/function/Consumer;
        //    13: invokespecial   sweetie/leonware/api/event/Listener.<init>:(Ljava/util/function/Consumer;)V
        //    16: invokevirtual   sweetie/leonware/api/event/events/player/other/UpdateEvent.subscribe:(Lsweetie/leonware/api/event/Listener;)Lsweetie/leonware/api/event/EventListener;
        //    19: astore_1        /* updateEvent */
        //    20: invokestatic    sweetie/leonware/api/event/events/client/PacketEvent.getInstance:()Lsweetie/leonware/api/event/events/client/PacketEvent;
        //    23: new             Lsweetie/leonware/api/event/Listener;
        //    26: dup            
        //    27: aload_0         /* this */
        //    28: invokedynamic   BootstrapMethod #18, accept:(Lsweetie/leonware/client/features/modules/movement/WaterSpeedModule;)Ljava/util/function/Consumer;
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
        //    53: invokevirtual   sweetie/leonware/client/features/modules/movement/WaterSpeedModule.addEvents:([Lsweetie/leonware/api/event/EventListener;)V
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
    
    private void handleSprintE() {
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        final boolean forward = WaterSpeedModule.mc.field_1724.field_3913.field_3905 > 0.0f;
        final boolean backward = WaterSpeedModule.mc.field_1724.field_3913.field_3905 < 0.0f;
        if (forward && !backward) {
            WaterSpeedModule.mc.field_1724.method_5728(true);
        }
    }
    
    private void handleIntave() {
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        final boolean isMoving = MoveUtil.isMoving();
        if (isMoving) {
            this.timer.reset();
        }
        if (WaterSpeedModule.mc.field_1690.field_1894.method_1434()) {
            final float speedMultiplier = this.getIntaveMultiplier();
            WaterSpeedModule.mc.field_1724.method_18800(WaterSpeedModule.mc.field_1724.method_18798().field_1352 * speedMultiplier, WaterSpeedModule.mc.field_1724.method_18798().field_1351, WaterSpeedModule.mc.field_1724.method_18798().field_1350 * speedMultiplier);
        }
        if (!WaterSpeedModule.mc.field_1724.field_5976 && !isMoving && this.timer.finished(300L)) {
            final double yAnim = (WaterSpeedModule.mc.field_1724.field_6012 % 3 == 0) ? -0.03 : 0.019;
            WaterSpeedModule.mc.field_1724.method_18800(WaterSpeedModule.mc.field_1724.method_18798().field_1352, WaterSpeedModule.mc.field_1724.method_18798().field_1351 + yAnim, WaterSpeedModule.mc.field_1724.method_18798().field_1350);
        }
    }
    
    private float getIntaveMultiplier() {
        if (this.offInUse.getValue() && WaterSpeedModule.mc.field_1724.method_6115()) {
            return 1.0f;
        }
        boolean hasDepthStrider = false;
        final class_1799 boots = WaterSpeedModule.mc.field_1724.method_31548().method_7372(0);
        if (!boots.method_7960()) {
            final String name = boots.method_7964().getString().toLowerCase();
            if (name.contains("depth") || name.contains("aqua") || name.contains("water")) {
                hasDepthStrider = true;
            }
        }
        if (hasDepthStrider) {
            final boolean hasHead = WaterSpeedModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8575;
            return hasHead ? 1.04f : 1.043f;
        }
        return this.ftBoost.getValue();
    }
    
    private void handleVanilla() {
        if (WaterSpeedModule.mc.field_1724.method_5799() && !WaterSpeedModule.mc.field_1724.method_5681() && MoveUtil.isMoving()) {
            MoveUtil.setSpeed(this.vanillaSpeed.getValue());
        }
    }
    
    private void handleWall() {
        if (this.wallMode.is("FunTime")) {
            this.wallRadius = 0.35;
            this.wallBoost = 0.05;
        }
        else if (this.wallMode.is("MetaHvH")) {
            this.wallRadius = 0.35;
            this.wallBoost = 0.4;
        }
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        if (!WaterSpeedModule.mc.field_1724.field_5976) {
            return;
        }
        if (!this.isWaterNearFeet()) {
            return;
        }
        final class_2350 collisionFace = this.getCollisionFace();
        if (collisionFace == null) {
            return;
        }
        final class_243 pushDir = new class_243((double)(-collisionFace.method_10148()), 0.0, (double)(-collisionFace.method_10165()));
        if (pushDir.method_1027() < 1.0E-6) {
            return;
        }
        final double[] moveDir = this.calculateDirection(WaterSpeedModule.mc.field_1724.field_3913.field_3905, WaterSpeedModule.mc.field_1724.field_3913.field_3907, this.wallBoost);
        final class_243 combined = new class_243(moveDir[0], 0.0, moveDir[1]).method_1019(pushDir.method_1029().method_1021(this.wallBoost * 0.6));
        final class_243 velocity = WaterSpeedModule.mc.field_1724.method_18798();
        final class_243 result = velocity.method_1019(combined);
        final double vertical = Math.max(velocity.field_1351, this.wallVerticalBoost);
        WaterSpeedModule.mc.field_1724.method_18800(result.field_1352, vertical, result.field_1350);
        WaterSpeedModule.mc.field_1724.field_6017 = 0.0f;
    }
    
    private void handleHvH() {
        if (!WaterSpeedModule.mc.field_1724.method_5681()) {
            this.boostActive = false;
            this.updateBoost();
            return;
        }
        this.updateAntiFlag();
        if (this.hvhUpDown.getValue()) {
            this.handleVerticalMovement();
        }
        if (!WaterSpeedModule.mc.field_1724.method_5624()) {
            this.boostActive = false;
            this.updateBoost();
            return;
        }
        this.currentSpeed = this.calculateHvHSpeed();
        final long currentTime = System.currentTimeMillis();
        if (this.hvhBoost.getValue() && !this.antiFlagTriggered) {
            if (this.boostEndTime > 0L && currentTime >= this.boostEndTime) {
                this.boostActive = false;
            }
            if (!this.boostActive && currentTime - this.lastBoostTime >= this.hvhBoostCooldown.getValue().longValue()) {
                this.lastBoostTime = currentTime;
                this.boostEndTime = currentTime + this.hvhBoostDuration.getValue().longValue();
                this.boostActive = true;
            }
        }
        else {
            this.boostActive = false;
        }
        this.updateBoost();
        float speed = this.currentSpeed * this.hvhGeneralSpeed.getValue();
        if (this.hvhBoost.getValue() && this.boostMultiplier > 0.01f) {
            speed += this.hvhBoostSpeed.getValue() * this.boostMultiplier;
        }
        final class_1309 target = this.getTarget();
        boolean shouldTarget = false;
        if (this.hvhTarget.getValue()) {
            if (WaterSpeedModule.mc.field_1690.field_1894.method_1434() || WaterSpeedModule.mc.field_1690.field_1881.method_1434() || WaterSpeedModule.mc.field_1690.field_1913.method_1434() || WaterSpeedModule.mc.field_1690.field_1849.method_1434()) {
                shouldTarget = true;
            }
            if (target != null) {
                shouldTarget = true;
            }
        }
        if (shouldTarget && target != null) {
            final float distance = this.hvhTargetDistance.getValue();
            final double yawRad = Math.toRadians(target.method_36454());
            final double targetX = target.method_23317() - Math.sin(yawRad) * distance;
            final double targetZ = target.method_23321() + Math.cos(yawRad) * distance;
            final double diffX = targetX - WaterSpeedModule.mc.field_1724.method_23317();
            final double diffZ = targetZ - WaterSpeedModule.mc.field_1724.method_23321();
            final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
            if (dist > 0.01) {
                final double angle = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
                final double adjustedYaw = this.adjustYaw(angle, 0.0);
                final double sin = -Math.sin(Math.toRadians(adjustedYaw));
                final double cos = Math.cos(Math.toRadians(adjustedYaw));
                MoveUtil.setSpeed(speed);
                WaterSpeedModule.mc.field_1724.method_18800(sin * speed, WaterSpeedModule.mc.field_1724.method_18798().field_1351, cos * speed);
                if (this.hvhVerticalTarget.getValue()) {
                    final double targetY = target.method_23318() + target.method_17682() / 2.0;
                    final double playerY = WaterSpeedModule.mc.field_1724.method_23318() + WaterSpeedModule.mc.field_1724.method_18381(WaterSpeedModule.mc.field_1724.method_18376());
                    final double diffY = targetY - playerY;
                    if (Math.abs(diffY) > 0.1) {
                        final class_243 motion = WaterSpeedModule.mc.field_1724.method_18798();
                        WaterSpeedModule.mc.field_1724.method_18800(motion.field_1352, Math.signum(diffY) * this.hvhUpDownSpeed.getValue(), motion.field_1350);
                    }
                }
                return;
            }
        }
        MoveUtil.setSpeed(speed);
    }
    
    private float calculateHvHSpeed() {
        final class_1799 offhand = WaterSpeedModule.mc.field_1724.method_6079();
        final String displayName = offhand.method_7964().getString();
        final class_1293 speedEffect = WaterSpeedModule.mc.field_1724.method_6112(class_1294.field_5904);
        final boolean hasLomtikDyni = displayName.contains("\u041b\u043e\u043c\u0442\u0438\u043a \u0414\u044b\u043d\u0438");
        if (hasLomtikDyni && speedEffect != null && speedEffect.method_5578() == 2) {
            return 0.68069994f;
        }
        float baseSpeed;
        if (speedEffect == null) {
            baseSpeed = 0.4012f;
        }
        else if (speedEffect.method_5578() == 2) {
            baseSpeed = 0.6372f;
        }
        else if (speedEffect.method_5578() == 1) {
            baseSpeed = 0.59f;
        }
        else {
            baseSpeed = 0.4012f;
        }
        final class_1799 helmet = WaterSpeedModule.mc.field_1724.method_31548().method_7372(3);
        if (!helmet.method_7960() && helmet.method_7909() == class_1802.field_8575) {
            final String helmetName = helmet.method_7964().getString();
            if (helmetName.toLowerCase().contains("\u0271\u029f\u1d07\u1d21 \u1d04\u1d00\u0274\u1d1b\u028f") || helmetName.toLowerCase().contains("\u026f\u043b\u1d07\u028d \u1d04\u1d00\u043d\u0442\u044b") || helmetName.toLowerCase().contains("\u0448\u043b\u0435\u043c \u0441\u0430\u043d\u0442\u044b")) {
                baseSpeed *= 0.85f;
            }
        }
        return baseSpeed;
    }
    
    private void updateBoost() {
        final float target = this.boostActive ? 1.0f : 0.0f;
        final float step = this.hvhBoostSpeed.getValue() * 0.4f;
        this.boostMultiplier += (target - this.boostMultiplier) * step;
        if (Math.abs(target - this.boostMultiplier) < 0.005f) {
            this.boostMultiplier = target;
        }
    }
    
    private void handleVerticalMovement() {
        if (WaterSpeedModule.mc.field_1724.method_36455() < -25.0f || WaterSpeedModule.mc.field_1724.method_36455() > 25.0f) {
            return;
        }
        float motion = 0.0f;
        if (WaterSpeedModule.mc.field_1690.field_1903.method_1434()) {
            motion = this.hvhUpDownSpeed.getValue();
        }
        else if (WaterSpeedModule.mc.field_1690.field_1832.method_1434()) {
            motion = -this.hvhUpDownSpeed.getValue();
        }
        if (motion != 0.0f) {
            final class_243 vel = WaterSpeedModule.mc.field_1724.method_18798();
            WaterSpeedModule.mc.field_1724.method_18800(vel.field_1352, (double)motion, vel.field_1350);
        }
    }
    
    private void triggerAntiFlag() {
        this.antiFlagTriggered = true;
        this.flagTriggerTime = System.currentTimeMillis();
        this.boostActive = false;
    }
    
    private void updateAntiFlag() {
        if (this.antiFlagTriggered) {
            final long time = (long)(this.hvhAntiFlagTime.getValue() * 1000.0f);
            if (System.currentTimeMillis() - this.flagTriggerTime > time) {
                this.antiFlagTriggered = false;
            }
        }
    }
    
    private class_1309 getTarget() {
        final AuraModule aura = AuraModule.getInstance();
        if (aura != null && aura.isEnabled() && aura.target != null) {
            return aura.target;
        }
        return null;
    }
    
    private boolean checkCollision(final double yaw, final double distance) {
        final double yawRad = Math.toRadians(yaw);
        final double x = WaterSpeedModule.mc.field_1724.method_23317() + -Math.sin(yawRad) * distance;
        final double z = WaterSpeedModule.mc.field_1724.method_23321() + Math.cos(yawRad) * distance;
        final class_2338 pos = class_2338.method_49637(x, WaterSpeedModule.mc.field_1724.method_23318(), z);
        return !WaterSpeedModule.mc.field_1687.method_8320(pos).method_26215() && !WaterSpeedModule.mc.field_1687.method_8320(pos).method_26220((class_1922)WaterSpeedModule.mc.field_1687, pos).method_1110();
    }
    
    private double adjustYaw(final double yaw, final double distance) {
        final double[] array;
        final double[] offsets = array = new double[] { 15.0, -15.0, 30.0, -30.0, 45.0, -45.0 };
        for (final double offset : array) {
            if (!this.checkCollision(yaw + offset, distance)) {
                return yaw + offset;
            }
        }
        return yaw;
    }
    
    private class_2350 getCollisionFace() {
        final class_238 box = WaterSpeedModule.mc.field_1724.method_5829();
        for (final class_2350 dir : class_2350.class_2353.field_11062) {
            final class_238 shifted = box.method_989(dir.method_10148() * 0.05, 0.0, dir.method_10165() * 0.05);
            final boolean hasCollision = WaterSpeedModule.mc.field_1687.method_20812((class_1297)WaterSpeedModule.mc.field_1724, shifted).iterator().hasNext();
            if (hasCollision) {
                return dir;
            }
        }
        return null;
    }
    
    private boolean isWaterNearFeet() {
        final class_238 box = WaterSpeedModule.mc.field_1724.method_5829();
        final int minX = class_3532.method_15357(box.field_1323 - this.wallRadius);
        final int maxX = class_3532.method_15357(box.field_1320 + this.wallRadius);
        final int minY = class_3532.method_15357(box.field_1322 - 0.2);
        final int maxY = class_3532.method_15357(box.field_1322 + 0.2);
        final int minZ = class_3532.method_15357(box.field_1321 - this.wallRadius);
        final int maxZ = class_3532.method_15357(box.field_1324 + this.wallRadius);
        final class_2338.class_2339 mutablePos = new class_2338.class_2339();
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    mutablePos.method_10103(x, y, z);
                    final class_2680 state = WaterSpeedModule.mc.field_1687.method_8320((class_2338)mutablePos);
                    if (state.method_26227().method_15767(class_3486.field_15517) || state.method_26227().method_15767(class_3486.field_15518)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private double[] calculateDirection(final float forward, final float sideways, final double distance) {
        final float yaw = WaterSpeedModule.mc.field_1724.method_36454();
        final float sinYaw = class_3532.method_15374((float)Math.toRadians(yaw + 90.0f));
        final float cosYaw = class_3532.method_15362((float)Math.toRadians(yaw + 90.0f));
        final double xMovement = forward * distance * cosYaw + sideways * distance * sinYaw;
        final double zMovement = forward * distance * sinYaw - sideways * distance * cosYaw;
        return new double[] { xMovement, zMovement };
    }
    
    public static WaterSpeedModule getInstance() {
        return WaterSpeedModule.instance;
    }
    
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new WaterSpeedModule();
    }
}
