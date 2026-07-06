package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import net.minecraft.class_640;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/SpammerModule.class */
@ModuleRegister(name = "Spammer", category = Category.PLAYER)
public class SpammerModule extends Module {
    private static final SpammerModule instance = new SpammerModule();
    private final SliderSetting delay = new SliderSetting("Задержка (сек)").value(Float.valueOf(3.0f)).range(0.1f, 10.0f).step(0.1f);
    private final ModeSetting mode = new ModeSetting("Режим").values("Глобальный", "Локальный", "В лс").value("Глобальный");
    private String spamText = "LeonWare - лучший софт!";
    private final TimerUtil timer = new TimerUtil();
    private final Random random = new Random();

    @Generated
    public static SpammerModule getInstance() {
        return instance;
    }

    @Generated
    public String getSpamText() {
        return this.spamText;
    }

    @Generated
    public void setSpamText(String spamText) {
        this.spamText = spamText;
    }

    public SpammerModule() {
        addSettings(this.delay, this.mode);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.method_1562() == null || !this.timer.finished((long) (this.delay.getValue().floatValue() * 1000.0f)) || this.spamText == null || this.spamText.isEmpty()) {
                return;
            }
            switch (this.mode.getValue()) {
                case "Глобальный":
                    mc.method_1562().method_45729("!" + this.spamText);
                    break;
                case "Локальный":
                    mc.method_1562().method_45729(this.spamText);
                    break;
                case "В лс":
                    List<class_640> players = new ArrayList<>((Collection<? extends class_640>) mc.method_1562().method_2880());
                    players.removeIf(p -> {
                        return p.method_2966().getId().equals(mc.field_1724.method_5667());
                    });
                    if (!players.isEmpty()) {
                        class_640 target = players.get(this.random.nextInt(players.size()));
                        String name = target.method_2966().getName();
                        mc.method_1562().method_45731("m " + name + " " + this.spamText);
                        break;
                    }
                    break;
            }
            this.timer.reset();
        }));
        addEvents(updateEvent);
    }
}
