package sweetie.leonware.api.utils.neuro;

import ai.catboost.CatBoostModel;
import ai.catboost.CatBoostPredictions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_5611;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.animation.wrap.infinity.RotationAnimation;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.rotations.SpookyTimeRotation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/neuro/AIPredictor.class */
public class AIPredictor extends Configurable implements QuickImports {
    private CatBoostModel yawModel;
    private CatBoostModel pitchModel;
    private final TimerUtil timer = new TimerUtil();
    public RotationAnimation interpolation = new RotationAnimation();

    @Override // sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event -> {
            this.timer.reset();
        }));
        addEvents(attackEvent);
    }

    public void loadModel(String model) {
        if (!model.equals("Default")) {
            try {
                String pisun = ClientInfo.CONFIG_PATH_AI_MODELS + "/" + model;
                this.yawModel = CatBoostModel.loadModel(pisun + "_yaw.model");
                this.pitchModel = CatBoostModel.loadModel(pisun + "_pitch.model");
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.yawModel = loadModelFromResource(model.toLowerCase() + "_yaw.model");
            this.pitchModel = loadModelFromResource(model.toLowerCase() + "_pitch.model");
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private CatBoostModel loadModelFromResource(String resourceName) throws Exception {
        InputStream is = FileUtil.getFromAssets("models/" + resourceName);
        if (is == null) {
            throw new FileNotFoundException(resourceName);
        }
        File temp = File.createTempFile(resourceName, ".cbm");
        temp.deleteOnExit();
        byte[] buffer = is.readAllBytes();
        FileOutputStream out = new FileOutputStream(temp);
        try {
            out.write(buffer);
            out.close();
            return CatBoostModel.loadModel(temp.getAbsolutePath());
        } catch (Throwable th) {
            try {
                out.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public boolean isLoaded() {
        return (this.yawModel == null || this.pitchModel == null) ? false : true;
    }

    public void close() {
        if (this.yawModel != null) {
            this.yawModel.close();
        }
        if (this.pitchModel != null) {
            this.pitchModel.close();
        }
        this.yawModel = null;
        this.pitchModel = null;
    }

    public Rotation predict(class_1297 target, Rotation current, Rotation prev, class_5611 speed) {
        if (!isLoaded()) {
            return current;
        }
        class_243 playerPos = mc.field_1724.method_19538();
        class_243 targetPos = target.method_19538();
        class_243 playerVel = class_243.field_1353;
        class_243 targetVel = class_243.field_1353;
        Rotation atTarget = getRotationAt(targetPos);
        float yawDiff = class_3532.method_15393(atTarget.getYaw());
        float pitchDiff = class_3532.method_15363(atTarget.getPitch(), -90.0f, 90.0f);
        FeaturesData d = new FeaturesData(Math.abs(current.getYaw() - prev.getYaw()), Math.abs(current.getPitch() - prev.getPitch()), 0.0f, pitchDiff, this.timer.getElapsedTime(), (float) mc.field_1724.method_19538().method_1022(targetPos), mc.field_1724.method_24828() ? 1 : 0, (mc.field_1724.method_6128() || mc.field_1724.method_5869()) ? 1 : 0, class_3532.method_15393(current.getYaw()), current.getPitch(), (float) playerPos.field_1352, (float) playerPos.field_1351, (float) playerPos.field_1350, (float) playerVel.field_1352, (float) playerVel.field_1351, (float) playerVel.field_1350, (float) targetPos.field_1352, (float) targetPos.field_1351, (float) targetPos.field_1350, (float) targetVel.field_1352, (float) targetVel.field_1351, (float) targetVel.field_1350, 0.0f, 0.0f, 0.0f);
        float[] features = {d.yawDelta(), d.pitchDelta(), d.targetYaw(), d.targetPitch(), d.sinceAttack(), d.distance(), d.onGround(), d.miniHitbox(), d.playerYaw(), d.playerPitch(), d.playerPosX(), d.playerPosY(), d.playerPosZ(), d.playerVelX(), d.playerVelY(), d.playerVelZ(), d.targetPosX(), d.targetPosY(), d.targetPosZ(), d.targetVelX(), d.targetVelY(), d.targetVelZ(), d.deltaX(), d.deltaY(), d.deltaZ()};
        String[] crendil = {String.valueOf(d.onGround()), String.valueOf(d.miniHitbox())};
        CatBoostPredictions yawPred = this.yawModel.predict(features, crendil);
        CatBoostPredictions pitchPred = this.pitchModel.predict(features, crendil);
        float predictedYaw = (float) yawPred.get(0, 0);
        float predictedPitch = (float) pitchPred.get(0, 0);
        float shortestYawPath = (((((predictedYaw + yawDiff) - this.interpolation.getYaw()) % 360.0f) + 540.0f) % 360.0f) - 180.0f;
        float targetYaw = this.interpolation.getYaw() + shortestYawPath;
        float targetPitch = predictedPitch - this.interpolation.getPitch();
        if (mc.field_1724.method_23318() > target.method_23318() + ((double) target.method_17682())) {
            targetPitch = getRotationAt(target.method_19538().method_1031(0.0d, target.method_17682(), 0.0d)).getPitch();
        }
        if (mc.field_1724.method_23318() + 1.0d < target.method_23318()) {
            targetPitch = getRotationAt(target.method_19538().method_1031(0.0d, 0.5d, 0.0d)).getPitch();
        }
        if (mc.field_1724.method_5681()) {
            targetPitch = getRotationAt(target.method_19538().method_1031(0.0d, target.method_17682() / 2.0f, 0.0d)).getPitch();
            speed = new class_5611(MathUtil.randomInRange(200, 350), MathUtil.randomInRange(200, 350));
        }
        if ((PlayerUtil.hasCollisionWith(target) && (SpookyTimeRotation.stalin(target) || (PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10382 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10382))) || PlayerUtil.hasCollisionWith(target, -0.7f)) {
            speed = new class_5611(speed.method_32118() * 1.6f, speed.method_32119() * 1.6f);
        }
        this.interpolation.easing(Easing.LINEAR).animate(new Rotation(targetYaw, targetPitch), (int) speed.method_32118(), (int) speed.method_32119());
        float finalYaw = speed.method_32118() == 0.0f ? targetYaw : this.interpolation.getYaw();
        float finalPitch = speed.method_32119() == 0.0f ? targetPitch : this.interpolation.getPitch();
        return new Rotation(finalYaw, finalPitch);
    }

    private Rotation getRotationAt(class_243 pos) {
        return RotationUtil.rotationAt(pos);
    }
}
