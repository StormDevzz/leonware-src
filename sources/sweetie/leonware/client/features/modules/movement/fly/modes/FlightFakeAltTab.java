package sweetie.leonware.client.features.modules.movement.fly.modes;

import java.util.function.Supplier;
import net.minecraft.class_243;
import net.minecraft.class_2799;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/fly/modes/FlightFakeAltTab.class */
public class FlightFakeAltTab extends FlightMode {
    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "FakeAltTab";
    }

    public FlightFakeAltTab(Supplier<Boolean> condition) {
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onEnable() {
        if (mc.field_1724 == null) {
            return;
        }
        sendRespawnPacket();
        resetKeys();
        mc.field_1724.method_31549().field_7479 = true;
        mc.field_1724.method_31549().method_7248(0.0f);
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onDisable() {
        if (mc.field_1724 == null) {
            return;
        }
        sendRespawnPacket();
        mc.field_1724.method_31549().field_7479 = false;
        mc.field_1724.method_31549().method_7248(0.05f);
        mc.field_1724.method_18799(class_243.field_1353);
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onUpdate() {
        if (mc.field_1724 == null) {
            return;
        }
        mc.field_1724.method_18800(0.0d, 0.0d, 0.0d);
        mc.field_1724.method_31549().field_7479 = true;
        mc.field_1724.method_31549().method_7248(0.0f);
    }

    private void sendRespawnPacket() {
        if (mc.method_1562() != null) {
            mc.method_1562().method_52787(new class_2799(class_2799.class_2800.field_12774));
        }
    }

    private void resetKeys() {
        mc.field_1690.field_1890.method_23481(false);
        mc.field_1690.field_1822.method_23481(false);
        mc.field_1690.field_1886.method_23481(false);
        mc.field_1690.field_1904.method_23481(false);
        mc.field_1690.field_1903.method_23481(false);
        mc.field_1690.field_1832.method_23481(false);
        mc.field_1690.field_1867.method_23481(false);
        mc.field_1690.field_1894.method_23481(false);
        mc.field_1690.field_1881.method_23481(false);
        mc.field_1690.field_1913.method_23481(false);
        mc.field_1690.field_1849.method_23481(false);
    }
}
