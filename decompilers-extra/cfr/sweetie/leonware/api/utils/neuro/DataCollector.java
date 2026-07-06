/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2767
 *  net.minecraft.class_3417
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.neuro;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2767;
import net.minecraft.class_3417;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.neuro.FeaturesData;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;

public class DataCollector
implements QuickImports {
    private static final DataCollector instance = new DataCollector();
    private int dataSize = 0;
    private final List<FeaturesData> dataBuffer = new ObjectArrayList(20);
    private class_1309 target;
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil attackTimer = new TimerUtil();

    public void startCollecting() {
        this.dataBuffer.clear();
        this.dataSize = 0;
        this.print("\u041d\u0430\u0447\u0430\u0442\u0430 \u0441\u0431\u043e\u0440\u043a\u0430 \u0434\u0430\u0442\u0430\u0441\u0435\u0442\u0430");
    }

    public void stopCollecting() {
        this.dataSize = 0;
        this.saveDataset();
        this.dataBuffer.clear();
        this.print("\u041f\u0440\u0435\u043a\u0440\u0430\u0449\u0435\u043d\u0430 \u0441\u0431\u043e\u0440\u043a\u0430 \u0434\u0430\u0442\u0430\u0441\u0435\u0442\u0430");
    }

    public void onAttack(AttackEvent.AttackEventData event) {
        class_1297 class_12972 = event.entity();
        if (class_12972 instanceof class_1309) {
            class_1309 livingEntity;
            this.target = livingEntity = (class_1309)class_12972;
            this.timer.reset();
            this.attackTimer.reset();
        }
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        class_2767 play;
        class_2596<?> class_25962;
        if (event.isReceive() && (class_25962 = event.packet()) instanceof class_2767 && ((play = (class_2767)class_25962).method_11894().comp_349() == class_3417.field_15016 || play.method_11894().comp_349() == class_3417.field_14706)) {
            new Thread(() -> {
                if (!this.attackTimer.finished(200L)) {
                    this.saveDataset();
                }
                this.dataSize += this.dataBuffer.size();
                this.dataBuffer.clear();
            }).start();
        }
    }

    public void onUpdate() {
        this.parse((class_1309)DataCollector.mc.field_1724);
    }

    public void saveDataset() {
        if (this.dataBuffer.isEmpty()) {
            return;
        }
        File file = new File("rotation_data.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));){
            writer.write("yaw_delta,pitch_delta,target_yaw,target_pitch,since_attack,distance,on_ground,mini_hitbox,player_yaw,player_pitch,player_pos_x,player_pos_y,player_pos_z,player_vel_x,player_vel_y,player_vel_z,target_pos_x,target_pos_y,target_pos_z,target_vel_x,target_vel_y,target_vel_z,delta_x,delta_y,delta_z\n");
            for (FeaturesData d : this.dataBuffer) {
                writer.write(String.format(Locale.US, "%f,%f,%f,%f,%f,%f,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n", Float.valueOf(d.yawDelta()), Float.valueOf(d.pitchDelta()), Float.valueOf(d.targetYaw()), Float.valueOf(d.targetPitch()), Float.valueOf(d.sinceAttack()), Float.valueOf(d.distance()), d.onGround(), d.miniHitbox(), Float.valueOf(d.playerYaw()), Float.valueOf(d.playerPitch()), Float.valueOf(d.playerPosX()), Float.valueOf(d.playerPosY()), Float.valueOf(d.playerPosZ()), Float.valueOf(d.playerVelX()), Float.valueOf(d.playerVelY()), Float.valueOf(d.playerVelZ()), Float.valueOf(d.targetPosX()), Float.valueOf(d.targetPosY()), Float.valueOf(d.targetPosZ()), Float.valueOf(d.targetVelX()), Float.valueOf(d.targetVelY()), Float.valueOf(d.targetVelZ()), Float.valueOf(d.deltaX()), Float.valueOf(d.deltaY()), Float.valueOf(d.deltaZ())));
            }
            this.dataSize += this.dataBuffer.size();
            this.dataBuffer.clear();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse(class_1309 player) {
        if (this.timer.finished(2000L) || player.method_29504() || !DataCollector.mc.field_1687.method_18456().contains(player)) {
            return;
        }
        if (this.target == null || this.target.method_29504()) {
            return;
        }
        Rotation atTarget = RotationUtil.rotationAt(this.target.method_19538());
        float currentYaw = player.method_36454();
        float currentPitch = player.method_36455();
        float prevYaw = player.field_5982;
        float prevPitch = player.field_6004;
        RotationManager rotationManager = RotationManager.getInstance();
        if (rotationManager.getCurrentRotationPlan() != null) {
            Rotation current = rotationManager.getRotation();
            Rotation prev = rotationManager.getPreviousRotation();
            currentYaw = current.getYaw();
            currentPitch = current.getPitch();
            prevYaw = prev.getYaw();
            prevPitch = prev.getPitch();
        }
        float yawDiff = class_3532.method_15393((float)atTarget.getYaw());
        float pitchDiff = class_3532.method_15393((float)atTarget.getPitch());
        class_243 pv = player.method_18798();
        class_243 tv = this.target.method_18798();
        class_243 pp = player.method_19538();
        class_243 tp = this.target.method_19538();
        float deltaX = (float)(this.target.method_23317() - this.target.field_6014);
        float deltaY = (float)(this.target.method_23318() - this.target.field_6036);
        float deltaZ = (float)(this.target.method_23321() - this.target.field_5969);
        this.dataBuffer.add(new FeaturesData(Math.abs(currentYaw - prevYaw), Math.abs(currentPitch - prevPitch), yawDiff, pitchDiff, this.timer.getElapsedTime(), (float)pp.method_1022(tp), player.method_24828() ? 1 : 0, player.method_6128() || player.method_5681() ? 1 : 0, currentYaw, currentPitch, (float)pp.field_1352, (float)pp.field_1351, (float)pp.field_1350, (float)pv.field_1352, (float)pv.field_1351, (float)pv.field_1350, (float)tp.field_1352, (float)tp.field_1351, (float)tp.field_1350, (float)tv.field_1352, (float)tv.field_1351, (float)tv.field_1350, deltaX, deltaY, deltaZ));
    }

    public float normalizeTo360(float angle) {
        if ((angle %= 360.0f) < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public float normalizeTo180(float angle) {
        if ((angle = this.normalizeTo360(angle)) > 180.0f) {
            angle -= 360.0f;
        }
        return angle;
    }

    public float getAngleDifference(float angle1, float angle2) {
        float normalizedAngle1 = this.normalizeTo360(angle1);
        float normalizedAngle2 = this.normalizeTo360(angle2);
        float difference = normalizedAngle1 - normalizedAngle2;
        return this.normalizeTo180(difference);
    }

    @Generated
    public static DataCollector getInstance() {
        return instance;
    }

    @Generated
    public int getDataSize() {
        return this.dataSize;
    }

    @Generated
    public List<FeaturesData> getDataBuffer() {
        return this.dataBuffer;
    }
}

