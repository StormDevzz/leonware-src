/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_238
 *  net.minecraft.class_2596
 *  net.minecraft.class_265
 *  net.minecraft.class_2824
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2829
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_265;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Mace Damage", category=Category.COMBAT)
public class MaceDamageModule
extends Module {
    private static final MaceDamageModule instance = new MaceDamageModule();
    private final SliderSetting fallHeight = new SliderSetting("FallHeight").value(Float.valueOf(22.0f)).range(1.0f, 170.0f).step(1.0f);

    public MaceDamageModule() {
        this.addSettings(this.fallHeight);
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_1799 mainHand;
            if (event.isSend() && event.packet() instanceof class_2824 && MaceDamageModule.mc.field_1724 != null && MaceDamageModule.mc.field_1687 != null && (mainHand = MaceDamageModule.mc.field_1724.method_6047()).method_7909() == class_1802.field_49814) {
                if (MaceDamageModule.mc.field_1724.field_5976) {
                    return;
                }
                int height = this.determineHeight();
                double x = MaceDamageModule.mc.field_1724.method_23317();
                double y = MaceDamageModule.mc.field_1724.method_23318();
                double z = MaceDamageModule.mc.field_1724.method_23321();
                if (height > 10) {
                    int times = (int)Math.ceil(Math.abs((double)height / 10.0));
                    for (int i = 0; i < times; ++i) {
                        this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, false, false));
                    }
                } else {
                    for (int i = 0; i < 2; ++i) {
                        this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, MaceDamageModule.mc.field_1724.method_24828(), false));
                    }
                }
                this.sendPacket((class_2828)new class_2828.class_2829(x, y + (double)height, z, false, false));
                this.sendPacket((class_2828)new class_2828.class_2829(x, y, z, false, false));
            }
        }));
        this.addEvents(packetEvent);
    }

    private int determineHeight() {
        class_238 boundingBox = MaceDamageModule.mc.field_1724.method_5829();
        for (int i = (int)((Float)this.fallHeight.getValue()).floatValue(); i >= 1; --i) {
            class_238 newBB = boundingBox.method_989(0.0, (double)i, 0.0);
            boolean noCollision = true;
            for (class_265 shape : MaceDamageModule.mc.field_1687.method_20812((class_1297)MaceDamageModule.mc.field_1724, newBB)) {
                if (shape.method_1110()) continue;
                noCollision = false;
                break;
            }
            if (!noCollision) continue;
            return i;
        }
        return 0;
    }

    private void sendPacket(class_2828 packet) {
        if (mc.method_1562() != null) {
            mc.method_1562().method_52787((class_2596)packet);
        }
    }

    @Generated
    public static MaceDamageModule getInstance() {
        return instance;
    }
}

