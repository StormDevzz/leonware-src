// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1294;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Levitation Control", category = Category.PLAYER)
public class LevitationControlModule extends Module
{
    private static final LevitationControlModule instance;
    private final SliderSetting up;
    private final SliderSetting down;
    private final BooleanSetting stuck;
    
    public LevitationControlModule() {
        this.up = new SliderSetting("Up Speed").value(0.5f).range(0.0f, 2.0f).step(0.1f);
        this.down = new SliderSetting("Down Speed").value(0.5f).range(0.0f, 2.0f).step(0.1f);
        this.stuck = new BooleanSetting("Freeze").value(true);
        this.addSettings(this.up, this.down, this.stuck);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (LevitationControlModule.mc.field_1724 == null || LevitationControlModule.mc.field_1687 == null) {
                return;
            }
            else if (!LevitationControlModule.mc.field_1724.method_6059(class_1294.field_5902)) {
                return;
            }
            else {
                double verticalVelocity;
                if (LevitationControlModule.mc.field_1690.field_1903.method_1434()) {
                    verticalVelocity = this.up.getValue();
                }
                else if (LevitationControlModule.mc.field_1690.field_1832.method_1434()) {
                    verticalVelocity = -this.down.getValue();
                }
                else if (this.stuck.getValue()) {
                    verticalVelocity = 0.0;
                }
                else {
                    verticalVelocity = LevitationControlModule.mc.field_1724.method_18798().field_1351;
                }
                if (Math.abs(LevitationControlModule.mc.field_1724.method_18798().field_1351 - verticalVelocity) > 0.001) {
                    final class_243 currentVel = LevitationControlModule.mc.field_1724.method_18798();
                    LevitationControlModule.mc.field_1724.method_18800(currentVel.field_1352, verticalVelocity, currentVel.field_1350);
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static LevitationControlModule getInstance() {
        return LevitationControlModule.instance;
    }
    
    static {
        instance = new LevitationControlModule();
    }
}
