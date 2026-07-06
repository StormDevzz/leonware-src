package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_266;
import net.minecraft.class_5250;
import net.minecraft.class_746;
import net.minecraft.class_8646;
import net.minecraft.class_9013;
import net.minecraft.class_9025;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.player.PlayerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/HealthResolverModule.class */
@ModuleRegister(name = "Health Resolver", category = Category.OTHER)
public class HealthResolverModule extends Module {
    private static final HealthResolverModule instance = new HealthResolverModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Fun Time").values("Really World", "Fun Time");

    @Generated
    public static HealthResolverModule getInstance() {
        return instance;
    }

    public HealthResolverModule() {
        addSettings(this.mode);
    }

    public boolean isRW() {
        return this.mode.is("Really World") && isEnabled();
    }

    public boolean isFT() {
        return this.mode.is("Fun Time") && isEnabled();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            class_266 scoreboard;
            if (isFT()) {
                if (mc.method_1562() == null && mc.method_1562().method_45734() == null) {
                    return;
                }
                for (class_746 class_746Var : mc.field_1687.method_18456()) {
                    if (class_746Var != mc.field_1724 && PlayerUtil.isValidName(class_746Var.method_5477().getString())) {
                        String parsedHealth = "";
                        if (class_746Var.method_7327().method_1189(class_8646.field_45158) != null && (scoreboard = class_746Var.method_7327().method_1189(class_8646.field_45158)) != null) {
                            class_9013 readableScoreboardScore = class_746Var.method_7327().method_55430(class_746Var, scoreboard);
                            class_5250 mutableText = class_9013.method_55398(readableScoreboardScore, scoreboard.method_55380(class_9025.field_47566));
                            parsedHealth = mutableText.getString();
                        }
                        float resolvedHealth = 0.0f;
                        try {
                            resolvedHealth = Float.parseFloat(parsedHealth);
                        } catch (NumberFormatException e) {
                        }
                        if (!parsedHealth.isEmpty() && !parsedHealth.equals("0")) {
                            class_746Var.method_6033(resolvedHealth);
                        }
                    }
                }
            }
        }));
        addEvents(tickEvent);
    }
}
