// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.fly.modes;

import net.minecraft.class_2596;
import net.minecraft.class_2799;
import net.minecraft.class_243;
import java.util.function.Supplier;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

public class FlightFakeAltTab extends FlightMode
{
    @Override
    public String getName() {
        return "FakeAltTab";
    }
    
    public FlightFakeAltTab(final Supplier<Boolean> condition) {
    }
    
    @Override
    public void onEnable() {
        if (FlightFakeAltTab.mc.field_1724 == null) {
            return;
        }
        this.sendRespawnPacket();
        this.resetKeys();
        FlightFakeAltTab.mc.field_1724.method_31549().field_7479 = true;
        FlightFakeAltTab.mc.field_1724.method_31549().method_7248(0.0f);
    }
    
    @Override
    public void onDisable() {
        if (FlightFakeAltTab.mc.field_1724 == null) {
            return;
        }
        this.sendRespawnPacket();
        FlightFakeAltTab.mc.field_1724.method_31549().field_7479 = false;
        FlightFakeAltTab.mc.field_1724.method_31549().method_7248(0.05f);
        FlightFakeAltTab.mc.field_1724.method_18799(class_243.field_1353);
    }
    
    @Override
    public void onUpdate() {
        if (FlightFakeAltTab.mc.field_1724 == null) {
            return;
        }
        FlightFakeAltTab.mc.field_1724.method_18800(0.0, 0.0, 0.0);
        FlightFakeAltTab.mc.field_1724.method_31549().field_7479 = true;
        FlightFakeAltTab.mc.field_1724.method_31549().method_7248(0.0f);
    }
    
    private void sendRespawnPacket() {
        if (FlightFakeAltTab.mc.method_1562() != null) {
            FlightFakeAltTab.mc.method_1562().method_52787((class_2596)new class_2799(class_2799.class_2800.field_12774));
        }
    }
    
    private void resetKeys() {
        FlightFakeAltTab.mc.field_1690.field_1890.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1822.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1886.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1904.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1903.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1832.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1867.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1894.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1881.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1913.method_23481(false);
        FlightFakeAltTab.mc.field_1690.field_1849.method_23481(false);
    }
}
