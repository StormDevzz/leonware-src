/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.utils.math.MathUtil;

public class TargetMovementPrediction
extends Configurable {
    private PredictMode predictMode = PredictMode.SIMPLE;
    private final BooleanSetting prediction = new BooleanSetting("Prediction").value(true);
    private final Supplier<Boolean> isPredict = this.prediction::getValue;
    private final ModeSetting mode = new ModeSetting("Mode").value(PredictMode.SIMPLE).values(PredictMode.values()).onAction(() -> {
        this.predictMode = (PredictMode)Choice.getChoiceByName((String)((String)this.getMode().getValue()), (ModeSetting.NamedChoice[])PredictMode.values());
    });
    private final BooleanSetting glidingOnly = new BooleanSetting("Gliding only").value(true);
    private final SliderSetting multiplier = new SliderSetting("Multiplier").value(Float.valueOf(1.8f)).range(0.5f, 6.0f).step(0.1f);

    public TargetMovementPrediction() {
        this.addSettings(this.prediction, this.mode, this.glidingOnly, this.multiplier);
        for (Setting<?> setting : this.getSettings()) {
            if (setting == this.prediction) continue;
            setting.setVisible(this.isPredict);
        }
    }

    public class_243 predictPosition(class_1309 target, class_243 targetPosition) {
        if (!((Boolean)this.prediction.getValue()).booleanValue() || MathUtil.getEntityBPS((class_1297)target) < 13.0 || ((Boolean)this.glidingOnly.getValue()).booleanValue() && !target.method_6128()) {
            return targetPosition;
        }
        double mult = ((Float)this.multiplier.getValue()).floatValue();
        return this.predictMode.predict(target, targetPosition, mult);
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    private static enum PredictMode implements ModeSetting.NamedChoice
    {
        SIMPLE("Simple", (target, targetPosition, multiplier) -> targetPosition.method_1019(target.method_18798().method_1021(multiplier))),
        VELOCITY("Velocity", (target, targetPosition, multiplier) -> {
            class_243 simple = SIMPLE.predict(target, targetPosition, multiplier);
            return simple.method_1023(0.0, 0.025 * multiplier * multiplier, 0.0);
        });

        private final String name;
        private final PredictFunction predict;

        @Override
        public String getName() {
            return this.name;
        }

        private PredictMode(String name, PredictFunction predict) {
            this.name = name;
            this.predict = predict;
        }

        public class_243 predict(class_1309 target, class_243 targetPosition, double multiplier) {
            return this.predict.apply(target, targetPosition, multiplier);
        }

        @FunctionalInterface
        static interface PredictFunction {
            public class_243 apply(class_1309 var1, class_243 var2, double var3);
        }
    }
}

