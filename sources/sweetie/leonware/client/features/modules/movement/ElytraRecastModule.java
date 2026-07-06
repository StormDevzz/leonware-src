package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1304;
import net.minecraft.class_1802;
import net.minecraft.class_2848;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/ElytraRecastModule.class */
@ModuleRegister(name = "Elytra Recast", category = Category.MOVEMENT)
public class ElytraRecastModule extends Module {
    private static final ElytraRecastModule instance = new ElytraRecastModule();
    public final ModeSetting mode = new ModeSetting("Mode").values("1.21.1", "1.21.4+").value("1.21.4+");
    public final SliderSetting groundDelay = new SliderSetting("Задержка").value(Float.valueOf(3.0f)).range(0.0f, 10.0f).step(1.0f);
    public final BooleanSetting autoWalk = new BooleanSetting("Auto Walk").value((Boolean) false);
    public final BooleanSetting exploit = new BooleanSetting("Exploit").value((Boolean) false);
    private int groundTick = 0;
    private int jump = 0;
    private boolean boostedLastTick = false;

    @Generated
    public static ElytraRecastModule getInstance() {
        return instance;
    }

    public ElytraRecastModule() {
        addSettings(this.mode, this.groundDelay, this.autoWalk, this.exploit);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            onTick();
        }));
        addEvents(tickEvent);
    }

    private void onTick() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        if (this.autoWalk.getValue().booleanValue()) {
            mc.field_1690.field_1894.method_23481(true);
        }
        if (this.mode.is("1.21.1")) {
            handleOldLogic();
        } else {
            handleNewLogic();
        }
    }

    private void handleOldLogic() {
        if (mc.field_1724.method_24828()) {
            this.groundTick = 5;
            return;
        }
        if (this.groundTick > 0) {
            this.groundTick--;
            return;
        }
        if (!mc.field_1724.method_6128() && mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833) && MoveUtil.isMoving()) {
            if (mc.field_1724.method_6115()) {
                mc.field_1724.method_6021();
            } else if (!mc.field_1724.method_6128()) {
                mc.field_1724.method_23669();
                mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
            }
        }
    }

    private void handleNewLogic() {
        if (mc.field_1724.method_24828()) {
            this.groundTick = this.groundDelay.getValue().intValue();
            this.jump = 0;
        }
        if (this.groundTick > 0) {
            this.groundTick--;
            return;
        }
        if (mc.field_1724.method_6128()) {
            this.jump = 0;
            return;
        }
        if (!mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)) {
            this.jump = 0;
            return;
        }
        switch (this.jump) {
            case 0:
                mc.field_1690.field_1903.method_23481(false);
                this.jump = 1;
                break;
            case 1:
                mc.field_1690.field_1903.method_23481(true);
                this.jump = 2;
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                mc.field_1690.field_1903.method_23481(false);
                this.jump = 0;
                break;
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.jump = 0;
        this.boostedLastTick = false;
        if (mc.field_1690 != null && mc.field_1724 != null) {
            mc.field_1690.field_1903.method_23481(false);
        }
        super.onDisable();
    }
}
