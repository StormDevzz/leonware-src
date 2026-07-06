// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_2596;
import java.util.Iterator;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2828;
import net.minecraft.class_1802;
import net.minecraft.class_2824;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Mace Damage", category = Category.COMBAT)
public class MaceDamageModule extends Module
{
    private static final MaceDamageModule instance;
    private final SliderSetting fallHeight;
    
    public MaceDamageModule() {
        this.fallHeight = new SliderSetting("FallHeight").value(22.0f).range(1.0f, 170.0f).step(1.0f);
        this.addSettings(this.fallHeight);
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (event.isSend() && event.packet() instanceof class_2824 && MaceDamageModule.mc.field_1724 != null && MaceDamageModule.mc.field_1687 != null) {
                final class_1799 mainHand = MaceDamageModule.mc.field_1724.method_6047();
                if (mainHand.method_7909() == class_1802.field_49814) {
                    if (!MaceDamageModule.mc.field_1724.field_5976) {
                        final int height = this.determineHeight();
                        final double x = MaceDamageModule.mc.field_1724.method_23317();
                        final double y = MaceDamageModule.mc.field_1724.method_23318();
                        final double z = MaceDamageModule.mc.field_1724.method_23321();
                        if (height > 10) {
                            for (int times = (int)Math.ceil(Math.abs(height / 10.0)), i = 0; i < times; ++i) {
                                this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, false, false));
                            }
                        }
                        else {
                            for (int j = 0; j < 2; ++j) {
                                this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, MaceDamageModule.mc.field_1724.method_24828(), false));
                            }
                        }
                        this.sendPacket((class_2828)new class_2828.class_2829(x, y + height, z, false, false));
                        this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, false, false));
                    }
                }
            }
            return;
        }));
        this.addEvents(packetEvent);
    }
    
    private int determineHeight() {
        final class_238 boundingBox = MaceDamageModule.mc.field_1724.method_5829();
        for (int i = (int)(float)this.fallHeight.getValue(); i >= 1; --i) {
            final class_238 newBB = boundingBox.method_989(0.0, (double)i, 0.0);
            boolean noCollision = true;
            for (final class_265 shape : MaceDamageModule.mc.field_1687.method_20812((class_1297)MaceDamageModule.mc.field_1724, newBB)) {
                if (!shape.method_1110()) {
                    noCollision = false;
                    break;
                }
            }
            if (noCollision) {
                return i;
            }
        }
        return 0;
    }
    
    private void sendPacket(final class_2828 packet) {
        if (MaceDamageModule.mc.method_1562() != null) {
            MaceDamageModule.mc.method_1562().method_52787((class_2596)packet);
        }
    }
    
    @Generated
    public static MaceDamageModule getInstance() {
        return MaceDamageModule.instance;
    }
    
    static {
        instance = new MaceDamageModule();
    }
}
