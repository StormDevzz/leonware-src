package sweetie.leonware.client.features.modules.movement.nitrofirework.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/nitrofirework/modes/NitroFireworkLG.class */
public class NitroFireworkLG extends NitroFireworkMode {
    private final ModeSetting mode = new ModeSetting("Grim boost").value("Legends Grief").values("Legends Grief", "Really World");
    private final SliderSetting inTargetValue = new SliderSetting("In target value").value(Float.valueOf(3.0f)).range(0.1f, 6.0f).step(0.1f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Vertical boost").value((Boolean) true), new BooleanSetting("Extra speed").value((Boolean) false));
    private final BooleanSetting speedPlus = new BooleanSetting("Speed plus").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(!this.mode.is("Custom") && this.options.isEnabled("Extra speed"));
    });
    private final BooleanSetting distanceBasedSpeed = new BooleanSetting("Distance based speed").value((Boolean) true);

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Grim";
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public NitroFireworkLG(Supplier<Boolean> condition) {
        addSettings(this.mode, this.inTargetValue, this.options, this.speedPlus, this.distanceBasedSpeed);
        getSettings().forEach(setting -> {
            setting.setVisible(condition);
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:54:0x04d1  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04e8  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x058e  */
    /* JADX WARN: Type inference failed for: r0v102 */
    /* JADX WARN: Type inference failed for: r0v103 */
    /* JADX WARN: Type inference failed for: r0v104 */
    /* JADX WARN: Type inference failed for: r0v114 */
    /* JADX WARN: Type inference failed for: r0v118 */
    /* JADX WARN: Type inference failed for: r0v122 */
    /* JADX WARN: Type inference failed for: r0v124, types: [float[]] */
    /* JADX WARN: Type inference failed for: r0v22, types: [int[]] */
    /* JADX WARN: Type inference failed for: r0v27, types: [float[]] */
    /* JADX WARN: Type inference failed for: r0v51 */
    /* JADX WARN: Type inference failed for: r0v52 */
    /* JADX WARN: Type inference failed for: r0v54 */
    /* JADX WARN: Type inference failed for: r19v0 */
    /* JADX WARN: Type inference failed for: r19v1 */
    /* JADX WARN: Type inference failed for: r19v10 */
    /* JADX WARN: Type inference failed for: r19v11 */
    /* JADX WARN: Type inference failed for: r19v12 */
    /* JADX WARN: Type inference failed for: r19v13 */
    /* JADX WARN: Type inference failed for: r19v14 */
    /* JADX WARN: Type inference failed for: r19v15 */
    /* JADX WARN: Type inference failed for: r19v16 */
    /* JADX WARN: Type inference failed for: r19v17 */
    /* JADX WARN: Type inference failed for: r19v18 */
    /* JADX WARN: Type inference failed for: r19v19 */
    /* JADX WARN: Type inference failed for: r19v2 */
    /* JADX WARN: Type inference failed for: r19v20 */
    /* JADX WARN: Type inference failed for: r19v21 */
    /* JADX WARN: Type inference failed for: r19v22 */
    /* JADX WARN: Type inference failed for: r19v23 */
    /* JADX WARN: Type inference failed for: r19v24 */
    /* JADX WARN: Type inference failed for: r19v25 */
    /* JADX WARN: Type inference failed for: r19v26 */
    /* JADX WARN: Type inference failed for: r19v27 */
    /* JADX WARN: Type inference failed for: r19v28 */
    /* JADX WARN: Type inference failed for: r19v3 */
    /* JADX WARN: Type inference failed for: r19v4 */
    /* JADX WARN: Type inference failed for: r19v5 */
    /* JADX WARN: Type inference failed for: r19v6 */
    /* JADX WARN: Type inference failed for: r19v7 */
    /* JADX WARN: Type inference failed for: r19v8 */
    /* JADX WARN: Type inference failed for: r19v9 */
    /* JADX WARN: Type inference failed for: r1v24 */
    /* JADX WARN: Type inference failed for: r22v0 */
    /* JADX WARN: Type inference failed for: r22v1 */
    /* JADX WARN: Type inference failed for: r22v2 */
    /* JADX WARN: Type inference failed for: r25v0 */
    /* JADX WARN: Type inference failed for: r3v32, types: [int] */
    /* JADX WARN: Type inference failed for: r3v35, types: [int] */
    /* JADX WARN: Type inference failed for: r3v38, types: [int] */
    @Override // sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public sweetie.leonware.api.system.backend.Pair<java.lang.Float, java.lang.Float> velocityValues() {
        /*
            Method dump skipped, instruction units count: 1564
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkLG.velocityValues():sweetie.leonware.api.system.backend.Pair");
    }

    public boolean isYawInRange(float yaw, float firstValue, float radiusValue) {
        float yaw2 = ((yaw % 360.0f) + 360.0f) % 360.0f;
        float firstValue2 = ((firstValue % 360.0f) + 360.0f) % 360.0f;
        float minValue = ((firstValue2 - radiusValue) + 360.0f) % 360.0f;
        float maxValue = (firstValue2 + radiusValue) % 360.0f;
        return minValue < maxValue ? yaw2 >= minValue && yaw2 <= maxValue : yaw2 >= minValue || yaw2 <= maxValue;
    }
}
