package sweetie.leonware.client.features.modules.movement.spider.modes;

import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_2354;
import net.minecraft.class_2533;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_3481;
import net.minecraft.class_3749;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.movement.spider.SpiderMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/spider/modes/SpiderFunTime.class */
public class SpiderFunTime extends SpiderMode {
    private final TimerUtil timerUtil = new TimerUtil();

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Fun Time";
    }

    @Override // sweetie.leonware.client.features.modules.movement.spider.SpiderMode
    public void onMotion(MotionEvent.MotionEventData event) {
        class_2350 facing = mc.field_1724.method_5735();
        class_2338 posInFront = class_2338.method_49638(mc.field_1724.method_19538()).method_10093(facing);
        class_2680 stateInFront = mc.field_1687.method_8320(posInFront);
        class_2248 blockInFront = stateInFront.method_26204();
        class_2338 posBelow = class_2338.method_49638(mc.field_1724.method_19538());
        class_2680 stateBelow = mc.field_1687.method_8320(posBelow);
        class_2248 blockBelow = stateBelow.method_26204();
        boolean penisF = (blockInFront instanceof class_2533) && Boolean.TRUE.equals(stateInFront.method_11654(class_2741.field_12537)) && stateInFront.method_28498(class_2741.field_12481);
        boolean penisB = (blockBelow instanceof class_2533) && Boolean.TRUE.equals(stateBelow.method_11654(class_2741.field_12537)) && stateBelow.method_28498(class_2741.field_12481);
        boolean xuiF = (blockInFront instanceof class_2354) || stateInFront.method_26164(class_3481.field_15504) || (blockInFront instanceof class_2349) || (blockInFront instanceof class_3749) || penisF;
        boolean glubgseB = (blockBelow instanceof class_2354) || stateBelow.method_26164(class_3481.field_15504) || (blockBelow instanceof class_2349) || (blockBelow instanceof class_3749) || penisB;
        if (this.timerUtil.finished(240L) && hozColl()) {
            if (xuiF || glubgseB) {
                event.ground(true);
                mc.field_1724.method_24830(true);
                mc.field_1724.method_6043();
                mc.field_1724.field_6017 = 0.0f;
                this.timerUtil.reset();
            }
        }
    }
}
