/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2828
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;

@ModuleRegister(name="No Magma Damage", category=Category.MOVEMENT)
public class NoMagmaDamageModule
extends Module {
    private static final NoMagmaDamageModule instance = new NoMagmaDamageModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u0421\u043f\u0443\u0444 \u041d\u0430 \u0417\u0435\u043c\u043b\u0435", "\u0421\u0431\u0440\u043e\u0441", "\u0421\u043a\u0440\u044b\u0442\u043d\u044b\u0439 \u0421\u043f\u0443\u0444").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    private boolean wasOnMagma = false;
    private int jumpTicks = 0;

    public NoMagmaDamageModule() {
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (NoMagmaDamageModule.mc.field_1724 == null || NoMagmaDamageModule.mc.field_1687 == null) {
                return;
            }
            class_2338 pos = NoMagmaDamageModule.mc.field_1724.method_24515();
            class_2338 posDown = pos.method_10074();
            class_2680 stateDown = NoMagmaDamageModule.mc.field_1687.method_8320(posDown);
            class_2680 stateAt = NoMagmaDamageModule.mc.field_1687.method_8320(pos);
            boolean onMagma = stateDown.method_27852(class_2246.field_10092) || stateAt.method_27852(class_2246.field_10092);
            switch ((String)this.mode.getValue()) {
                case "\u041e\u0431\u044b\u0447\u043d\u044b\u0439": {
                    if (!onMagma || !NoMagmaDamageModule.mc.field_1724.method_24828()) break;
                    NoMagmaDamageModule.mc.field_1724.method_18800(NoMagmaDamageModule.mc.field_1724.method_18798().field_1352, 0.1, NoMagmaDamageModule.mc.field_1724.method_18798().field_1350);
                    NoMagmaDamageModule.mc.field_1724.method_24830(false);
                    break;
                }
                case "\u0421\u043f\u0443\u0444 \u041d\u0430 \u0417\u0435\u043c\u043b\u0435": {
                    if (onMagma) {
                        this.wasOnMagma = true;
                        break;
                    }
                    if (!this.wasOnMagma || NoMagmaDamageModule.mc.field_1724.method_24828()) break;
                    this.wasOnMagma = false;
                    break;
                }
                case "\u0421\u0431\u0440\u043e\u0441": {
                    if (onMagma && NoMagmaDamageModule.mc.field_1724.method_24828() && this.jumpTicks == 0) {
                        NoMagmaDamageModule.mc.field_1724.method_6043();
                        this.jumpTicks = 10;
                    }
                    if (this.jumpTicks <= 0) break;
                    --this.jumpTicks;
                    break;
                }
                case "\u0421\u043a\u0440\u044b\u0442\u043d\u044b\u0439 \u0421\u043f\u0443\u0444": {
                    if (onMagma && NoMagmaDamageModule.mc.field_1724.method_24828()) {
                        NoMagmaDamageModule.mc.field_1690.field_1832.method_23481(true);
                        break;
                    }
                    if (onMagma) break;
                    NoMagmaDamageModule.mc.field_1690.field_1832.method_23481(false);
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_2338 pos;
            if (!event.isSend()) {
                return;
            }
            class_2596<?> patt0$temp = event.packet();
            if (!(patt0$temp instanceof class_2828)) {
                return;
            }
            class_2828 packet = (class_2828)patt0$temp;
            if (this.mode.is("\u0421\u043f\u0443\u0444 \u041d\u0430 \u0417\u0435\u043c\u043b\u0435") && this.wasOnMagma) {
                ((IPlayerMoveC2SPacket)packet).setOnGround(true);
            } else if (this.mode.is("\u0421\u043a\u0440\u044b\u0442\u043d\u044b\u0439 \u0421\u043f\u0443\u0444") && NoMagmaDamageModule.mc.field_1687.method_8320(pos = NoMagmaDamageModule.mc.field_1724.method_24515().method_10074()).method_27852(class_2246.field_10092)) {
                ((IPlayerMoveC2SPacket)packet).setOnGround(true);
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    @Generated
    public static NoMagmaDamageModule getInstance() {
        return instance;
    }
}

