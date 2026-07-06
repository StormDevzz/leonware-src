package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.function.Function;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/TargetPosition.class */
public enum TargetPosition implements ModeSetting.NamedChoice {
    EYES("Eyes", target -> {
        return target.method_33571();
    }),
    CENTER("Center", target2 -> {
        return target2.method_19538().method_1031(0.0d, ((double) target2.method_17682()) / 2.0d, 0.0d);
    });

    private final String name;
    private final Function<class_1309, class_243> position;

    TargetPosition(String name, Function position) {
        this.name = name;
        this.position = position;
    }

    public class_243 getPosition(class_1309 target) {
        return this.position.apply(target);
    }

    @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return this.name;
    }
}
