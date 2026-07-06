/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_640
 */
package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
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

@ModuleRegister(name="Spammer", category=Category.PLAYER)
public class SpammerModule
extends Module {
    private static final SpammerModule instance = new SpammerModule();
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u0441\u0435\u043a)").value(Float.valueOf(3.0f)).range(0.1f, 10.0f).step(0.1f);
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439", "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u044b\u0439", "\u0412 \u043b\u0441").value("\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439");
    private String spamText = "LeonWare - \u043b\u0443\u0447\u0448\u0438\u0439 \u0441\u043e\u0444\u0442!";
    private final TimerUtil timer = new TimerUtil();
    private final Random random = new Random();

    public SpammerModule() {
        this.addSettings(this.delay, this.mode);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SpammerModule.mc.field_1724 == null || mc.method_1562() == null) {
                return;
            }
            if (this.timer.finished((long)(((Float)this.delay.getValue()).floatValue() * 1000.0f))) {
                if (this.spamText == null || this.spamText.isEmpty()) {
                    return;
                }
                switch ((String)this.mode.getValue()) {
                    case "\u0413\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u044b\u0439": {
                        mc.method_1562().method_45729("!" + this.spamText);
                        break;
                    }
                    case "\u041b\u043e\u043a\u0430\u043b\u044c\u043d\u044b\u0439": {
                        mc.method_1562().method_45729(this.spamText);
                        break;
                    }
                    case "\u0412 \u043b\u0441": {
                        ArrayList<class_640> players = new ArrayList<class_640>(mc.method_1562().method_2880());
                        players.removeIf(p -> p.method_2966().getId().equals(SpammerModule.mc.field_1724.method_5667()));
                        if (players.isEmpty()) break;
                        class_640 target = (class_640)players.get(this.random.nextInt(players.size()));
                        String name = target.method_2966().getName();
                        mc.method_1562().method_45731("m " + name + " " + this.spamText);
                    }
                }
                this.timer.reset();
            }
        }));
        this.addEvents(updateEvent);
    }

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
}

