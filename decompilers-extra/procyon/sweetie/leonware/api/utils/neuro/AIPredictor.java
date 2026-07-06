// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.neuro;

import sweetie.leonware.api.utils.rotation.RotationUtil;
import ai.catboost.CatBoostPredictions;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_2246;
import sweetie.leonware.api.utils.rotation.rotations.SpookyTimeRotation;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_3532;
import net.minecraft.class_243;
import net.minecraft.class_5611;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_1297;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.utils.animation.wrap.infinity.RotationAnimation;
import sweetie.leonware.api.utils.math.TimerUtil;
import ai.catboost.CatBoostModel;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.system.backend.Configurable;

public class AIPredictor extends Configurable implements QuickImports
{
    private CatBoostModel yawModel;
    private CatBoostModel pitchModel;
    private final TimerUtil timer;
    public RotationAnimation interpolation;
    
    public AIPredictor() {
        this.timer = new TimerUtil();
        this.interpolation = new RotationAnimation();
    }
    
    @Override
    public void onEvent() {
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> this.timer.reset()));
        this.addEvents(attackEvent);
    }
    
    public void loadModel(final String model) {
        if (!model.equals("Default")) {
            try {
                final String pisun = ClientInfo.CONFIG_PATH_AI_MODELS + "/" + model;
                this.yawModel = CatBoostModel.loadModel(pisun + "_yaw.model");
                this.pitchModel = CatBoostModel.loadModel(pisun + "_pitch.model");
                return;
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.yawModel = this.loadModelFromResource(model.toLowerCase() + "_yaw.model");
            this.pitchModel = this.loadModelFromResource(model.toLowerCase() + "_pitch.model");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private CatBoostModel loadModelFromResource(final String resourceName) throws Exception {
        final InputStream is = FileUtil.getFromAssets("models/" + resourceName);
        if (is == null) {
            throw new FileNotFoundException(resourceName);
        }
        final File temp = File.createTempFile(resourceName, ".cbm");
        temp.deleteOnExit();
        final byte[] buffer = is.readAllBytes();
        try (final FileOutputStream out = new FileOutputStream(temp)) {
            out.write(buffer);
        }
        return CatBoostModel.loadModel(temp.getAbsolutePath());
    }
    
    public boolean isLoaded() {
        return this.yawModel != null && this.pitchModel != null;
    }
    
    public void close() {
        try {
            if (this.yawModel != null) {
                this.yawModel.close();
            }
            if (this.pitchModel != null) {
                this.pitchModel.close();
            }
            this.yawModel = null;
            this.pitchModel = null;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public Rotation predict(final class_1297 target, final Rotation current, final Rotation prev, class_5611 speed) {
        try {
            if (!this.isLoaded()) {
                return current;
            }
            final class_243 playerPos = AIPredictor.mc.field_1724.method_19538();
            final class_243 targetPos = target.method_19538();
            final class_243 playerVel = class_243.field_1353;
            final class_243 targetVel = class_243.field_1353;
            final Rotation atTarget = this.getRotationAt(targetPos);
            final float yawDiff = class_3532.method_15393(atTarget.getYaw());
            final float pitchDiff = class_3532.method_15363(atTarget.getPitch(), -90.0f, 90.0f);
            final FeaturesData d = new FeaturesData(Math.abs(current.getYaw() - prev.getYaw()), Math.abs(current.getPitch() - prev.getPitch()), 0.0f, pitchDiff, (float)this.timer.getElapsedTime(), (float)AIPredictor.mc.field_1724.method_19538().method_1022(targetPos), AIPredictor.mc.field_1724.method_24828() ? 1 : 0, (AIPredictor.mc.field_1724.method_6128() || AIPredictor.mc.field_1724.method_5869()) ? 1 : 0, class_3532.method_15393(current.getYaw()), current.getPitch(), (float)playerPos.field_1352, (float)playerPos.field_1351, (float)playerPos.field_1350, (float)playerVel.field_1352, (float)playerVel.field_1351, (float)playerVel.field_1350, (float)targetPos.field_1352, (float)targetPos.field_1351, (float)targetPos.field_1350, (float)targetVel.field_1352, (float)targetVel.field_1351, (float)targetVel.field_1350, 0.0f, 0.0f, 0.0f);
            final float[] features = { d.yawDelta(), d.pitchDelta(), d.targetYaw(), d.targetPitch(), d.sinceAttack(), d.distance(), (float)d.onGround(), (float)d.miniHitbox(), d.playerYaw(), d.playerPitch(), d.playerPosX(), d.playerPosY(), d.playerPosZ(), d.playerVelX(), d.playerVelY(), d.playerVelZ(), d.targetPosX(), d.targetPosY(), d.targetPosZ(), d.targetVelX(), d.targetVelY(), d.targetVelZ(), d.deltaX(), d.deltaY(), d.deltaZ() };
            final String[] crendil = { String.valueOf(d.onGround()), String.valueOf(d.miniHitbox()) };
            final CatBoostPredictions yawPred = this.yawModel.predict(features, crendil);
            final CatBoostPredictions pitchPred = this.pitchModel.predict(features, crendil);
            final float predictedYaw = (float)yawPred.get(0, 0);
            final float predictedPitch = (float)pitchPred.get(0, 0);
            final float shortestYawPath = ((predictedYaw + yawDiff - this.interpolation.getYaw()) % 360.0f + 540.0f) % 360.0f - 180.0f;
            final float targetYaw = this.interpolation.getYaw() + shortestYawPath;
            float targetPitch = predictedPitch - this.interpolation.getPitch();
            if (AIPredictor.mc.field_1724.method_23318() > target.method_23318() + target.method_17682()) {
                targetPitch = this.getRotationAt(target.method_19538().method_1031(0.0, (double)target.method_17682(), 0.0)).getPitch();
            }
            if (AIPredictor.mc.field_1724.method_23318() + 1.0 < target.method_23318()) {
                targetPitch = this.getRotationAt(target.method_19538().method_1031(0.0, 0.5, 0.0)).getPitch();
            }
            if (AIPredictor.mc.field_1724.method_5681()) {
                targetPitch = this.getRotationAt(target.method_19538().method_1031(0.0, (double)(target.method_17682() / 2.0f), 0.0)).getPitch();
                speed = new class_5611((float)MathUtil.randomInRange(200, 350), (float)MathUtil.randomInRange(200, 350));
            }
            if ((PlayerUtil.hasCollisionWith(target) && (SpookyTimeRotation.stalin(target) || (PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10382 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10382))) || PlayerUtil.hasCollisionWith(target, -0.7f)) {
                speed = new class_5611(speed.method_32118() * 1.6f, speed.method_32119() * 1.6f);
            }
            this.interpolation.easing(Easing.LINEAR).animate(new Rotation(targetYaw, targetPitch), (int)speed.method_32118(), (int)speed.method_32119());
            final float finalYaw = (speed.method_32118() == 0.0f) ? targetYaw : this.interpolation.getYaw();
            final float finalPitch = (speed.method_32119() == 0.0f) ? targetPitch : this.interpolation.getPitch();
            return new Rotation(finalYaw, finalPitch);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private Rotation getRotationAt(final class_243 pos) {
        return RotationUtil.rotationAt(pos);
    }
}
