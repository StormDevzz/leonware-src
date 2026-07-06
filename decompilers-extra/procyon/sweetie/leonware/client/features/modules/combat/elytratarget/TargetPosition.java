// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.elytratarget;

import net.minecraft.class_243;
import net.minecraft.class_1309;
import java.util.function.Function;
import sweetie.leonware.api.module.setting.ModeSetting;

public enum TargetPosition implements ModeSetting.NamedChoice
{
    EYES("Eyes", target -> target.method_33571()), 
    CENTER("Center", target -> target.method_19538().method_1031(0.0, target.method_17682() / 2.0, 0.0));
    
    private final String name;
    private final Function<class_1309, class_243> position;
    
    private TargetPosition(final String name, final Function<class_1309, class_243> position) {
        this.name = name;
        this.position = position;
    }
    
    public class_243 getPosition(final class_1309 target) {
        return this.position.apply(target);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
