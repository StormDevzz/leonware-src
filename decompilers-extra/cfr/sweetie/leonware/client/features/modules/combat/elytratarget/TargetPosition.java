/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.function.Function;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.ModeSetting;

public enum TargetPosition implements ModeSetting.NamedChoice
{
    EYES("Eyes", target -> target.method_33571()),
    CENTER("Center", target -> target.method_19538().method_1031(0.0, (double)target.method_17682() / 2.0, 0.0));

    private final String name;
    private final Function<class_1309, class_243> position;

    private TargetPosition(String name, Function<class_1309, class_243> position) {
        this.name = name;
        this.position = position;
    }

    public class_243 getPosition(class_1309 target) {
        return this.position.apply(target);
    }

    @Override
    public String getName() {
        return this.name;
    }
}

