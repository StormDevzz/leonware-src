// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.elytratarget;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_243;
import net.minecraft.class_1309;
import java.util.Iterator;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.system.backend.Configurable;

public class TargetMovementPrediction extends Configurable
{
    private PredictMode predictMode;
    private final BooleanSetting prediction;
    private final Supplier<Boolean> isPredict;
    private final ModeSetting mode;
    private final BooleanSetting glidingOnly;
    private final SliderSetting multiplier;
    
    public TargetMovementPrediction() {
        this.predictMode = PredictMode.SIMPLE;
        this.prediction = new BooleanSetting("Prediction").value(true);
        final BooleanSetting prediction = this.prediction;
        Objects.requireNonNull(prediction);
        this.isPredict = (Supplier<Boolean>)prediction::getValue;
        this.mode = new ModeSetting("Mode").value(PredictMode.SIMPLE).values((Enum<?>[])PredictMode.values()).onAction(() -> this.predictMode = Choice.getChoiceByName(this.getMode().getValue(), PredictMode.values()));
        this.glidingOnly = new BooleanSetting("Gliding only").value(true);
        this.multiplier = new SliderSetting("Multiplier").value(1.8f).range(0.5f, 6.0f).step(0.1f);
        this.addSettings(this.prediction, this.mode, this.glidingOnly, this.multiplier);
        for (final Setting<?> setting : this.getSettings()) {
            if (setting == this.prediction) {
                continue;
            }
            setting.setVisible(this.isPredict);
        }
    }
    
    public class_243 predictPosition(final class_1309 target, final class_243 targetPosition) {
        if (!this.prediction.getValue() || MathUtil.getEntityBPS((class_1297)target) < 13.0 || (this.glidingOnly.getValue() && !target.method_6128())) {
            return targetPosition;
        }
        final double mult = this.multiplier.getValue();
        return this.predictMode.predict(target, targetPosition, mult);
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    private enum PredictMode implements ModeSetting.NamedChoice
    {
        SIMPLE("Simple", (target, targetPosition, multiplier) -> targetPosition.method_1019(target.method_18798().method_1021(multiplier))), 
        VELOCITY("Velocity", (target, targetPosition, multiplier) -> {
            final class_243 simple = PredictMode.SIMPLE.predict(target, targetPosition, multiplier);
            return simple.method_1023(0.0, 0.025 * multiplier * multiplier, 0.0);
        });
        
        private final String name;
        private final PredictFunction predict;
        
        @Override
        public String getName() {
            return this.name;
        }
        
        private PredictMode(final String name, final PredictFunction predict) {
            this.name = name;
            this.predict = predict;
        }
        
        public class_243 predict(final class_1309 target, final class_243 targetPosition, final double multiplier) {
            return this.predict.apply(target, targetPosition, multiplier);
        }
        
        @FunctionalInterface
        interface PredictFunction
        {
            class_243 apply(final class_1309 p0, final class_243 p1, final double p2);
        }
    }
}
