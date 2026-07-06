package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/MiddleAPTModule.class */
@ModuleRegister(name = "Middle APT", category = Category.PLAYER)
public class MiddleAPTModule extends Module {
    private static final MiddleAPTModule instance = new MiddleAPTModule();
    private long lastClickTime = 0;

    @Generated
    public static MiddleAPTModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            boolean middlePressed = GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 2) == 1;
            if (middlePressed) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - this.lastClickTime >= 1500) {
                    this.lastClickTime = currentTime;
                    useAptechka();
                }
            }
        }));
        addEvents(updateEvent);
    }

    private void useAptechka() {
        int aptSlot = findAptSlot();
        if (aptSlot == -1) {
            return;
        }
        int prevSlot = mc.field_1724.method_31548().field_7545;
        mc.field_1724.field_3944.method_52787(new class_2868(aptSlot));
        mc.field_1724.field_3944.method_52787(new class_2886(class_1268.field_5808, 0, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
        mc.field_1724.method_31548().field_7545 = prevSlot;
        mc.field_1724.field_3944.method_52787(new class_2868(prevSlot));
    }

    private int findAptSlot() {
        if (mc.field_1724.method_6047().method_7909() == class_1802.field_8601) {
            return mc.field_1724.method_31548().field_7545;
        }
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8601) {
                return i;
            }
        }
        return -1;
    }
}
