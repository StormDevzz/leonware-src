// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2246;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "High Jump", category = Category.MOVEMENT)
public class HighJumpModule extends Module
{
    private static final HighJumpModule instance;
    private final ModeSetting mode;
    private final SliderSetting vanillaHeight;
    private int jumpCounter;
    private boolean wasOnSlimeBlock;
    
    public HighJumpModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0421\u043b\u0430\u0439\u043c", "\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439", "\u041f\u043e\u043b\u044f\u0440", "\u0421\u043b\u0430\u0439\u043c \u041c\u0430\u0442\u0440\u0438\u043a\u0441").value("\u0421\u043b\u0430\u0439\u043c");
        this.vanillaHeight = new SliderSetting("\u0412\u044b\u0441\u043e\u0442\u0430 \u043f\u0440\u044b\u0436\u043a\u0430").value(0.6f).range(0.1f, 2.0f).step(0.1f).setVisible(() -> this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439"));
        this.jumpCounter = 0;
        this.wasOnSlimeBlock = false;
        this.addSettings(this.mode, this.vanillaHeight);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.jumpCounter = 0;
        this.wasOnSlimeBlock = false;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.jumpCounter = 0;
        this.wasOnSlimeBlock = false;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (HighJumpModule.mc.field_1724 == null || HighJumpModule.mc.field_1687 == null) {
                return;
            }
            else if (HighJumpModule.mc.field_1724.method_52535() || HighJumpModule.mc.field_1724.method_31549().field_7479 || HighJumpModule.mc.field_1724.method_3144()) {
                return;
            }
            else {
                if (this.mode.is("\u0412\u0430\u043d\u0438\u043b\u044c\u043d\u044b\u0439")) {
                    if (HighJumpModule.mc.field_1690.field_1903.method_1434() && HighJumpModule.mc.field_1724.method_24828()) {
                        HighJumpModule.mc.field_1724.method_18800(HighJumpModule.mc.field_1724.method_18798().field_1352, (double)this.vanillaHeight.getValue(), HighJumpModule.mc.field_1724.method_18798().field_1350);
                    }
                }
                else if (this.mode.is("\u0421\u043b\u0430\u0439\u043c")) {
                    final class_2338 playerPos = HighJumpModule.mc.field_1724.method_24515();
                    final class_2338 belowPlayer = playerPos.method_10074();
                    if (HighJumpModule.mc.field_1724.method_24828() && !HighJumpModule.mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                        this.jumpCounter = 0;
                    }
                    if (HighJumpModule.mc.field_1724.method_24828() && HighJumpModule.mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                        HighJumpModule.mc.field_1690.field_1903.method_23481(true);
                    }
                    if (HighJumpModule.mc.field_1690.field_1903.method_1434() && HighJumpModule.mc.field_1724.method_24828() && HighJumpModule.mc.field_1687.method_8320(belowPlayer).method_27852(class_2246.field_10030)) {
                        HighJumpModule.mc.field_1690.field_1894.method_23481(false);
                        HighJumpModule.mc.field_1690.field_1913.method_23481(false);
                        HighJumpModule.mc.field_1690.field_1849.method_23481(false);
                        HighJumpModule.mc.field_1690.field_1881.method_23481(false);
                        float jumpVelocity = 0.3f;
                        if (this.jumpCounter >= 1) {
                            final float jumpVelocity2 = 0.6f;
                            jumpVelocity = jumpVelocity2 + (this.jumpCounter - 1) * 0.1f;
                            if (jumpVelocity > 1.1f) {
                                this.jumpCounter = 0;
                                jumpVelocity = 0.6f;
                            }
                        }
                        HighJumpModule.mc.field_1724.method_18800(HighJumpModule.mc.field_1724.method_18798().field_1352, (double)jumpVelocity, HighJumpModule.mc.field_1724.method_18798().field_1350);
                        ++this.jumpCounter;
                    }
                }
                else if (this.mode.is("\u041f\u043e\u043b\u044f\u0440")) {
                    if (HighJumpModule.mc.field_1724.method_5799() && !HighJumpModule.mc.field_1724.method_5869()) {
                        HighJumpModule.mc.field_1724.method_5762(0.0, 0.56, 0.0);
                    }
                }
                else if (this.mode.is("\u0421\u043b\u0430\u0439\u043c \u041c\u0430\u0442\u0440\u0438\u043a\u0441")) {
                    final class_2338 belowPos = HighJumpModule.mc.field_1724.method_24515().method_10074();
                    if (HighJumpModule.mc.field_1724.method_24828() && HighJumpModule.mc.field_1687.method_8320(belowPos).method_27852(class_2246.field_10030)) {
                        this.wasOnSlimeBlock = true;
                    }
                    else if (this.wasOnSlimeBlock && !HighJumpModule.mc.field_1724.method_24828() && HighJumpModule.mc.field_1724.method_18798().field_1351 > 0.0) {
                        HighJumpModule.mc.field_1724.method_5762(0.0, 1.35, 0.0);
                        this.wasOnSlimeBlock = false;
                    }
                    else if (!HighJumpModule.mc.field_1687.method_8320(belowPos).method_27852(class_2246.field_10030)) {
                        this.wasOnSlimeBlock = false;
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static HighJumpModule getInstance() {
        return HighJumpModule.instance;
    }
    
    static {
        instance = new HighJumpModule();
    }
}
