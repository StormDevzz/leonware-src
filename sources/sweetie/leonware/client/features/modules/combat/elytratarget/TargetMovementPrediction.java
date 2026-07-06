package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.Objects;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.utils.math.MathUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/TargetMovementPrediction.class */
public class TargetMovementPrediction extends Configurable {
    private PredictMode predictMode = PredictMode.SIMPLE;
    private final BooleanSetting prediction = new BooleanSetting("Prediction").value((Boolean) true);
    private final Supplier<Boolean> isPredict;
    private final ModeSetting mode;
    private final BooleanSetting glidingOnly;
    private final SliderSetting multiplier;

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public TargetMovementPrediction() {
        BooleanSetting booleanSetting = this.prediction;
        Objects.requireNonNull(booleanSetting);
        this.isPredict = booleanSetting::getValue;
        this.mode = new ModeSetting("Mode").value((Enum<?>) PredictMode.SIMPLE).values(PredictMode.values()).onAction2(() -> {
            this.predictMode = (PredictMode) Choice.getChoiceByName(getMode().getValue(), PredictMode.values());
        });
        this.glidingOnly = new BooleanSetting("Gliding only").value((Boolean) true);
        this.multiplier = new SliderSetting("Multiplier").value(Float.valueOf(1.8f)).range(0.5f, 6.0f).step(0.1f);
        addSettings(this.prediction, this.mode, this.glidingOnly, this.multiplier);
        for (Setting<?> setting : getSettings()) {
            if (setting != this.prediction) {
                setting.setVisible(this.isPredict);
            }
        }
    }

    public class_243 predictPosition(class_1309 target, class_243 targetPosition) {
        if (!this.prediction.getValue().booleanValue() || MathUtil.getEntityBPS(target) < 13.0d || (this.glidingOnly.getValue().booleanValue() && !target.method_6128())) {
            return targetPosition;
        }
        double mult = this.multiplier.getValue().floatValue();
        return this.predictMode.predict(target, targetPosition, mult);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/TargetMovementPrediction$PredictMode.class */
    private enum PredictMode implements ModeSetting.NamedChoice {
        SIMPLE("Simple", (target, targetPosition, multiplier) -> {
            return targetPosition.method_1019(target.method_18798().method_1021(multiplier));
        }),
        VELOCITY("Velocity", (target2, targetPosition2, multiplier2) -> {
            class_243 simple = SIMPLE.predict(target2, targetPosition2, multiplier2);
            return simple.method_1023(0.0d, 0.025d * multiplier2 * multiplier2, 0.0d);
        });

        private final String name;
        private final PredictFunction predict;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/TargetMovementPrediction$PredictMode$PredictFunction.class */
        @FunctionalInterface
        interface PredictFunction {
            class_243 apply(class_1309 class_1309Var, class_243 class_243Var, double d);
        }

        @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
        public String getName() {
            return this.name;
        }

        PredictMode(String name, PredictFunction predict) {
            this.name = name;
            this.predict = predict;
        }

        public class_243 predict(class_1309 target, class_243 targetPosition, double multiplier) {
            return this.predict.apply(target, targetPosition, multiplier);
        }
    }
}
