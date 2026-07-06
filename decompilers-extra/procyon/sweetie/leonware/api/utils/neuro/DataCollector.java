// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.neuro;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import java.util.Iterator;
import java.io.IOException;
import java.util.Locale;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import net.minecraft.class_2596;
import net.minecraft.class_3417;
import net.minecraft.class_2767;
import sweetie.leonware.api.event.events.client.PacketEvent;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import sweetie.leonware.api.utils.math.TimerUtil;
import net.minecraft.class_1309;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class DataCollector implements QuickImports
{
    private static final DataCollector instance;
    private int dataSize;
    private final List<FeaturesData> dataBuffer;
    private class_1309 target;
    private final TimerUtil timer;
    private final TimerUtil attackTimer;
    
    public DataCollector() {
        this.dataSize = 0;
        this.dataBuffer = (List<FeaturesData>)new ObjectArrayList(20);
        this.timer = new TimerUtil();
        this.attackTimer = new TimerUtil();
    }
    
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
    
    public void onAttack(final AttackEvent.AttackEventData event) {
        final class_1297 entity = event.entity();
        if (entity instanceof final class_1309 livingEntity) {
            this.target = livingEntity;
            this.timer.reset();
            this.attackTimer.reset();
        }
    }
    
    public void onPacket(final PacketEvent.PacketEventData event) {
        if (event.isReceive()) {
            final class_2596<?> packet = event.packet();
            if (packet instanceof final class_2767 play) {
                if (play.method_11894().comp_349() == class_3417.field_15016 || play.method_11894().comp_349() == class_3417.field_14706) {
                    new Thread(() -> {
                        if (!this.attackTimer.finished(200L)) {
                            this.saveDataset();
                        }
                        this.dataSize += this.dataBuffer.size();
                        this.dataBuffer.clear();
                    }).start();
                }
            }
        }
    }
    
    public void onUpdate() {
        this.parse((class_1309)DataCollector.mc.field_1724);
    }
    
    public void saveDataset() {
        if (this.dataBuffer.isEmpty()) {
            return;
        }
        final File file = new File("rotation_data.csv");
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("yaw_delta,pitch_delta,target_yaw,target_pitch,since_attack,distance,on_ground,mini_hitbox,player_yaw,player_pitch,player_pos_x,player_pos_y,player_pos_z,player_vel_x,player_vel_y,player_vel_z,target_pos_x,target_pos_y,target_pos_z,target_vel_x,target_vel_y,target_vel_z,delta_x,delta_y,delta_z\n");
            for (final FeaturesData d : this.dataBuffer) {
                writer.write(String.format(Locale.US, "%f,%f,%f,%f,%f,%f,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n", d.yawDelta(), d.pitchDelta(), d.targetYaw(), d.targetPitch(), d.sinceAttack(), d.distance(), d.onGround(), d.miniHitbox(), d.playerYaw(), d.playerPitch(), d.playerPosX(), d.playerPosY(), d.playerPosZ(), d.playerVelX(), d.playerVelY(), d.playerVelZ(), d.targetPosX(), d.targetPosY(), d.targetPosZ(), d.targetVelX(), d.targetVelY(), d.targetVelZ(), d.deltaX(), d.deltaY(), d.deltaZ()));
            }
            this.dataSize += this.dataBuffer.size();
            this.dataBuffer.clear();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public void parse(final class_1309 player) {
        if (this.timer.finished(2000L) || player.method_29504() || !DataCollector.mc.field_1687.method_18456().contains(player)) {
            return;
        }
        if (this.target == null || this.target.method_29504()) {
            return;
        }
        final Rotation atTarget = RotationUtil.rotationAt(this.target.method_19538());
        float currentYaw = player.method_36454();
        float currentPitch = player.method_36455();
        float prevYaw = player.field_5982;
        float prevPitch = player.field_6004;
        final RotationManager rotationManager = RotationManager.getInstance();
        if (rotationManager.getCurrentRotationPlan() != null) {
            final Rotation current = rotationManager.getRotation();
            final Rotation prev = rotationManager.getPreviousRotation();
            currentYaw = current.getYaw();
            currentPitch = current.getPitch();
            prevYaw = prev.getYaw();
            prevPitch = prev.getPitch();
        }
        final float yawDiff = class_3532.method_15393(atTarget.getYaw());
        final float pitchDiff = class_3532.method_15393(atTarget.getPitch());
        final class_243 pv = player.method_18798();
        final class_243 tv = this.target.method_18798();
        final class_243 pp = player.method_19538();
        final class_243 tp = this.target.method_19538();
        final float deltaX = (float)(this.target.method_23317() - this.target.field_6014);
        final float deltaY = (float)(this.target.method_23318() - this.target.field_6036);
        final float deltaZ = (float)(this.target.method_23321() - this.target.field_5969);
        this.dataBuffer.add(new FeaturesData(Math.abs(currentYaw - prevYaw), Math.abs(currentPitch - prevPitch), yawDiff, pitchDiff, (float)this.timer.getElapsedTime(), (float)pp.method_1022(tp), (int)(player.method_24828() ? 1 : 0), (int)((player.method_6128() || player.method_5681()) ? 1 : 0), currentYaw, currentPitch, (float)pp.field_1352, (float)pp.field_1351, (float)pp.field_1350, (float)pv.field_1352, (float)pv.field_1351, (float)pv.field_1350, (float)tp.field_1352, (float)tp.field_1351, (float)tp.field_1350, (float)tv.field_1352, (float)tv.field_1351, (float)tv.field_1350, deltaX, deltaY, deltaZ));
    }
    
    public float normalizeTo360(float angle) {
        angle %= 360.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public float normalizeTo180(float angle) {
        angle = this.normalizeTo360(angle);
        if (angle > 180.0f) {
            angle -= 360.0f;
        }
        return angle;
    }
    
    public float getAngleDifference(final float angle1, final float angle2) {
        final float normalizedAngle1 = this.normalizeTo360(angle1);
        final float normalizedAngle2 = this.normalizeTo360(angle2);
        final float difference = normalizedAngle1 - normalizedAngle2;
        return this.normalizeTo180(difference);
    }
    
    @Generated
    public static DataCollector getInstance() {
        return DataCollector.instance;
    }
    
    @Generated
    public int getDataSize() {
        return this.dataSize;
    }
    
    @Generated
    public List<FeaturesData> getDataBuffer() {
        return this.dataBuffer;
    }
    
    static {
        instance = new DataCollector();
    }
}
