// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Web", category = Category.MOVEMENT)
public class NoWebModule extends Module
{
    private static final NoWebModule instance;
    private final ModeSetting mode;
    private final SliderSetting customHorizontalSpeed;
    private final SliderSetting customVerticalSpeed;
    
    public NoWebModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0424\u0438\u043a\u0441\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0439", "\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439").value("\u0424\u0438\u043a\u0441\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0439");
        this.customHorizontalSpeed = new SliderSetting("\u0413\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(0.19175f).range(0.01f, 1.5f).step(0.001f).setVisible(() -> this.mode.is("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439"));
        this.customVerticalSpeed = new SliderSetting("\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(0.995f).range(0.01f, 1.5f).step(0.001f).setVisible(() -> this.mode.is("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439"));
        this.addSettings(this.mode, this.customHorizontalSpeed, this.customVerticalSpeed);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (PlayerUtil.isInWeb()) {
                NoWebModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
                double verticalSpeed;
                double horizontalSpeed;
                if (this.mode.is("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439")) {
                    verticalSpeed = this.customVerticalSpeed.getValue();
                    horizontalSpeed = this.customHorizontalSpeed.getValue();
                }
                else {
                    verticalSpeed = 0.995;
                    horizontalSpeed = 0.19175;
                }
                if (NoWebModule.mc.field_1690.field_1903.method_1434()) {
                    NoWebModule.mc.field_1724.method_18798().field_1351 = verticalSpeed;
                }
                else if (NoWebModule.mc.field_1690.field_1832.method_1434()) {
                    NoWebModule.mc.field_1724.method_18798().field_1351 = -verticalSpeed;
                }
                MoveUtil.setSpeed(horizontalSpeed);
            }
            return;
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static NoWebModule getInstance() {
        return NoWebModule.instance;
    }
    
    static {
        instance = new NoWebModule();
    }
}
