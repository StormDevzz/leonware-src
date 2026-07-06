/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="Edge Stop", category=Category.PLAYER)
public class EdgeStopModule
extends Module {
    private static final EdgeStopModule instance = new EdgeStopModule();

    @Override
    public void onEvent() {
        EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            boolean atEdge;
            if (EdgeStopModule.mc.field_1724 == null || EdgeStopModule.mc.field_1687 == null) {
                return;
            }
            class_2338 pos = EdgeStopModule.mc.field_1724.method_24515().method_10074();
            boolean bl = atEdge = EdgeStopModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10124) && EdgeStopModule.mc.field_1724.method_24828();
            if (atEdge) {
                event.setSneak(true);
            }
        }));
        this.addEvents(moveInputEvent);
    }

    @Generated
    public static EdgeStopModule getInstance() {
        return instance;
    }
}

