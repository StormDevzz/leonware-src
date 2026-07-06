package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/NoMagmaDamageModule.class */
@ModuleRegister(name = "No Magma Damage", category = Category.MOVEMENT)
public class NoMagmaDamageModule extends Module {
    private static final NoMagmaDamageModule instance = new NoMagmaDamageModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Обычный", "Спуф На Земле", "Сброс", "Скрытный Спуф").value("Обычный");
    private boolean wasOnMagma = false;
    private int jumpTicks = 0;

    @Generated
    public static NoMagmaDamageModule getInstance() {
        return instance;
    }

    public NoMagmaDamageModule() {
        addSettings(this.mode);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.wasOnMagma = false;
        this.jumpTicks = 0;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            boolean onMagma;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            class_2338 pos = mc.field_1724.method_24515();
            class_2338 posDown = pos.method_10074();
            class_2680 stateDown = mc.field_1687.method_8320(posDown);
            class_2680 stateAt = mc.field_1687.method_8320(pos);
            onMagma = stateDown.method_27852(class_2246.field_10092) || stateAt.method_27852(class_2246.field_10092);
            switch (this.mode.getValue()) {
                case "Обычный":
                    if (onMagma && mc.field_1724.method_24828()) {
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.1d, mc.field_1724.method_18798().field_1350);
                        mc.field_1724.method_24830(false);
                        break;
                    }
                    break;
                case "Спуф На Земле":
                    if (onMagma) {
                        this.wasOnMagma = true;
                        break;
                    } else if (this.wasOnMagma && !mc.field_1724.method_24828()) {
                        this.wasOnMagma = false;
                        break;
                    }
                    break;
                case "Сброс":
                    if (onMagma && mc.field_1724.method_24828() && this.jumpTicks == 0) {
                        mc.field_1724.method_6043();
                        this.jumpTicks = 10;
                    }
                    if (this.jumpTicks > 0) {
                        this.jumpTicks--;
                        break;
                    }
                    break;
                case "Скрытный Спуф":
                    if (onMagma && mc.field_1724.method_24828()) {
                        mc.field_1690.field_1832.method_23481(true);
                        break;
                    } else {
                        if (!onMagma) {
                            mc.field_1690.field_1832.method_23481(false);
                        }
                        break;
                    }
                    break;
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (event2.isSend()) {
                IPlayerMoveC2SPacket iPlayerMoveC2SPacketPacket = event2.packet();
                if (iPlayerMoveC2SPacketPacket instanceof class_2828) {
                    IPlayerMoveC2SPacket iPlayerMoveC2SPacket = (class_2828) iPlayerMoveC2SPacketPacket;
                    if (this.mode.is("Спуф На Земле") && this.wasOnMagma) {
                        iPlayerMoveC2SPacket.setOnGround(true);
                    } else if (this.mode.is("Скрытный Спуф")) {
                        class_2338 pos = mc.field_1724.method_24515().method_10074();
                        if (mc.field_1687.method_8320(pos).method_27852(class_2246.field_10092)) {
                            iPlayerMoveC2SPacket.setOnGround(true);
                        }
                    }
                }
            }
        }));
        addEvents(updateEvent, packetEvent);
    }
}
