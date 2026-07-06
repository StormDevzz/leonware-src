package sweetie.leonware.api.utils.neuro;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2767;
import net.minecraft.class_3417;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/neuro/DataCollector.class */
public class DataCollector implements QuickImports {
    private static final DataCollector instance = new DataCollector();
    private class_1309 target;
    private int dataSize = 0;
    private final List<FeaturesData> dataBuffer = new ObjectArrayList(20);
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil attackTimer = new TimerUtil();

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

    public void startCollecting() {
        this.dataBuffer.clear();
        this.dataSize = 0;
        print("Начата сборка датасета");
    }

    public void stopCollecting() {
        this.dataSize = 0;
        saveDataset();
        this.dataBuffer.clear();
        print("Прекращена сборка датасета");
    }

    public void onAttack(AttackEvent.AttackEventData event) {
        class_1309 class_1309VarEntity = event.entity();
        if (class_1309VarEntity instanceof class_1309) {
            class_1309 livingEntity = class_1309VarEntity;
            this.target = livingEntity;
            this.timer.reset();
            this.attackTimer.reset();
        }
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (event.isReceive()) {
            class_2767 class_2767VarPacket = event.packet();
            if (class_2767VarPacket instanceof class_2767) {
                class_2767 play = class_2767VarPacket;
                if (play.method_11894().comp_349() == class_3417.field_15016 || play.method_11894().comp_349() == class_3417.field_14706) {
                    new Thread(() -> {
                        if (!this.attackTimer.finished(200L)) {
                            saveDataset();
                        }
                        this.dataSize += this.dataBuffer.size();
                        this.dataBuffer.clear();
                    }).start();
                }
            }
        }
    }

    public void onUpdate() {
        parse(mc.field_1724);
    }

    public void saveDataset() {
        if (this.dataBuffer.isEmpty()) {
            return;
        }
        File file = new File("rotation_data.csv");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            try {
                writer.write("yaw_delta,pitch_delta,target_yaw,target_pitch,since_attack,distance,on_ground,mini_hitbox,player_yaw,player_pitch,player_pos_x,player_pos_y,player_pos_z,player_vel_x,player_vel_y,player_vel_z,target_pos_x,target_pos_y,target_pos_z,target_vel_x,target_vel_y,target_vel_z,delta_x,delta_y,delta_z\n");
                for (FeaturesData d : this.dataBuffer) {
                    writer.write(String.format(Locale.US, "%f,%f,%f,%f,%f,%f,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n", Float.valueOf(d.yawDelta()), Float.valueOf(d.pitchDelta()), Float.valueOf(d.targetYaw()), Float.valueOf(d.targetPitch()), Float.valueOf(d.sinceAttack()), Float.valueOf(d.distance()), Integer.valueOf(d.onGround()), Integer.valueOf(d.miniHitbox()), Float.valueOf(d.playerYaw()), Float.valueOf(d.playerPitch()), Float.valueOf(d.playerPosX()), Float.valueOf(d.playerPosY()), Float.valueOf(d.playerPosZ()), Float.valueOf(d.playerVelX()), Float.valueOf(d.playerVelY()), Float.valueOf(d.playerVelZ()), Float.valueOf(d.targetPosX()), Float.valueOf(d.targetPosY()), Float.valueOf(d.targetPosZ()), Float.valueOf(d.targetVelX()), Float.valueOf(d.targetVelY()), Float.valueOf(d.targetVelZ()), Float.valueOf(d.deltaX()), Float.valueOf(d.deltaY()), Float.valueOf(d.deltaZ())));
                }
                this.dataSize += this.dataBuffer.size();
                this.dataBuffer.clear();
                writer.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse(class_1309 player) {
        if (this.timer.finished(2000L) || player.method_29504() || !mc.field_1687.method_18456().contains(player) || this.target == null || this.target.method_29504()) {
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
        float yawDiff = class_3532.method_15393(atTarget.getYaw());
        float pitchDiff = class_3532.method_15393(atTarget.getPitch());
        class_243 pv = player.method_18798();
        class_243 tv = this.target.method_18798();
        class_243 pp = player.method_19538();
        class_243 tp = this.target.method_19538();
        float deltaX = (float) (this.target.method_23317() - this.target.field_6014);
        float deltaY = (float) (this.target.method_23318() - this.target.field_6036);
        float deltaZ = (float) (this.target.method_23321() - this.target.field_5969);
        this.dataBuffer.add(new FeaturesData(Math.abs(currentYaw - prevYaw), Math.abs(currentPitch - prevPitch), yawDiff, pitchDiff, this.timer.getElapsedTime(), (float) pp.method_1022(tp), player.method_24828() ? 1 : 0, (player.method_6128() || player.method_5681()) ? 1 : 0, currentYaw, currentPitch, (float) pp.field_1352, (float) pp.field_1351, (float) pp.field_1350, (float) pv.field_1352, (float) pv.field_1351, (float) pv.field_1350, (float) tp.field_1352, (float) tp.field_1351, (float) tp.field_1350, (float) tv.field_1352, (float) tv.field_1351, (float) tv.field_1350, deltaX, deltaY, deltaZ));
    }

    public float normalizeTo360(float angle) {
        float angle2 = angle % 360.0f;
        if (angle2 < 0.0f) {
            angle2 += 360.0f;
        }
        return angle2;
    }

    public float normalizeTo180(float angle) {
        float angle2 = normalizeTo360(angle);
        if (angle2 > 180.0f) {
            angle2 -= 360.0f;
        }
        return angle2;
    }

    public float getAngleDifference(float angle1, float angle2) {
        float normalizedAngle1 = normalizeTo360(angle1);
        float normalizedAngle2 = normalizeTo360(angle2);
        float difference = normalizedAngle1 - normalizedAngle2;
        return normalizeTo180(difference);
    }
}
