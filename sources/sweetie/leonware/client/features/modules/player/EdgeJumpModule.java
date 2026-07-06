package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/EdgeJumpModule.class */
@ModuleRegister(name = "Edge Jump", category = Category.PLAYER)
public class EdgeJumpModule extends Module {
    private static final EdgeJumpModule instance = new EdgeJumpModule();
    private final BooleanSetting autoSprint = new BooleanSetting("Авто Спринт").value((Boolean) true);
    private final BooleanSetting onlyWhenMoving = new BooleanSetting("Только При Движении").value((Boolean) true);
    private final SliderSetting jumpDistance = new SliderSetting("Дистанция Прыжка").value(Float.valueOf(1.0f)).range(0.5f, 2.0f).step(0.1f);
    private int cooldown = 0;

    @Generated
    public static EdgeJumpModule getInstance() {
        return instance;
    }

    public EdgeJumpModule() {
        addSettings(this.autoSprint, this.onlyWhenMoving, this.jumpDistance);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.cooldown = 0;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.cooldown > 0) {
                this.cooldown--;
                return;
            }
            if (!mc.field_1724.method_24828() || mc.field_1724.method_5715()) {
                return;
            }
            if (!this.onlyWhenMoving.getValue().booleanValue() || isMoving()) {
                class_2338 pos = mc.field_1724.method_24515();
                class_2350 facing = mc.field_1724.method_5735();
                class_2338 forwardPos = pos.method_10093(facing);
                boolean isAtEdge = mc.field_1687.method_8320(pos.method_10074()).method_26215() || mc.field_1687.method_8320(forwardPos.method_10074()).method_26215();
                if (isAtEdge) {
                    class_2338 jumpTarget = pos.method_10079(facing, (int) this.jumpDistance.getValue().floatValue());
                    boolean canLand = !mc.field_1687.method_8320(jumpTarget.method_10074()).method_26215();
                    if (canLand) {
                        if (this.autoSprint.getValue().booleanValue()) {
                            mc.field_1724.method_5728(true);
                        }
                        mc.field_1724.method_6043();
                        this.cooldown = 10;
                    }
                }
            }
        }));
        addEvents(updateEvent);
    }

    private boolean isMoving() {
        return (mc.field_1724.field_3913.field_3905 == 0.0f && mc.field_1724.field_3913.field_3907 == 0.0f) ? false : true;
    }
}
