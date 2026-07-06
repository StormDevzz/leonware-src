package sweetie.leonware.api.utils.rotation.manager;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/manager/RotationMode.class */
public abstract class RotationMode implements QuickImports, ModeSetting.NamedChoice {
    private final String name;

    public abstract Rotation process(Rotation rotation, Rotation rotation2, class_243 class_243Var, class_1297 class_1297Var);

    public RotationMode(String name) {
        this.name = name;
    }

    @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return this.name;
    }

    public Rotation process(Rotation currentRotation, Rotation targetRotation) {
        return process(currentRotation, targetRotation, null, null);
    }

    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d) {
        return process(currentRotation, targetRotation, vec3d, null);
    }
}
