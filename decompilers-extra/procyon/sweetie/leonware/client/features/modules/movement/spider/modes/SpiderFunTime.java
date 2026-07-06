// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.spider.modes;

import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2350;
import net.minecraft.class_3749;
import net.minecraft.class_2349;
import net.minecraft.class_3481;
import net.minecraft.class_2354;
import net.minecraft.class_2769;
import net.minecraft.class_2741;
import net.minecraft.class_2533;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.movement.spider.SpiderMode;

public class SpiderFunTime extends SpiderMode
{
    private final TimerUtil timerUtil;
    
    public SpiderFunTime() {
        this.timerUtil = new TimerUtil();
    }
    
    @Override
    public String getName() {
        return "Fun Time";
    }
    
    @Override
    public void onMotion(final MotionEvent.MotionEventData event) {
        final class_2350 facing = SpiderFunTime.mc.field_1724.method_5735();
        final class_2338 posInFront = class_2338.method_49638((class_2374)SpiderFunTime.mc.field_1724.method_19538()).method_10093(facing);
        final class_2680 stateInFront = SpiderFunTime.mc.field_1687.method_8320(posInFront);
        final class_2248 blockInFront = stateInFront.method_26204();
        final class_2338 posBelow = class_2338.method_49638((class_2374)SpiderFunTime.mc.field_1724.method_19538());
        final class_2680 stateBelow = SpiderFunTime.mc.field_1687.method_8320(posBelow);
        final class_2248 blockBelow = stateBelow.method_26204();
        final boolean penisF = blockInFront instanceof class_2533 && Boolean.TRUE.equals(stateInFront.method_11654((class_2769)class_2741.field_12537)) && stateInFront.method_28498((class_2769)class_2741.field_12481);
        final boolean penisB = blockBelow instanceof class_2533 && Boolean.TRUE.equals(stateBelow.method_11654((class_2769)class_2741.field_12537)) && stateBelow.method_28498((class_2769)class_2741.field_12481);
        final boolean xuiF = blockInFront instanceof class_2354 || stateInFront.method_26164(class_3481.field_15504) || blockInFront instanceof class_2349 || blockInFront instanceof class_3749 || penisF;
        final boolean glubgseB = blockBelow instanceof class_2354 || stateBelow.method_26164(class_3481.field_15504) || blockBelow instanceof class_2349 || blockBelow instanceof class_3749 || penisB;
        if (this.timerUtil.finished(240L) && this.hozColl() && (xuiF || glubgseB)) {
            event.ground(true);
            SpiderFunTime.mc.field_1724.method_24830(true);
            SpiderFunTime.mc.field_1724.method_6043();
            SpiderFunTime.mc.field_1724.field_6017 = 0.0f;
            this.timerUtil.reset();
        }
    }
}
