// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2886;
import net.minecraft.class_1268;
import net.minecraft.class_2596;
import net.minecraft.class_2868;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Middle APT", category = Category.PLAYER)
public class MiddleAPTModule extends Module
{
    private static final MiddleAPTModule instance;
    private long lastClickTime;
    
    public MiddleAPTModule() {
        this.lastClickTime = 0L;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (MiddleAPTModule.mc.field_1724 == null || MiddleAPTModule.mc.field_1687 == null) {
                return;
            }
            else {
                final boolean middlePressed = GLFW.glfwGetMouseButton(MiddleAPTModule.mc.method_22683().method_4490(), 2) == 1;
                if (middlePressed) {
                    final long currentTime = System.currentTimeMillis();
                    if (currentTime - this.lastClickTime >= 1500L) {
                        this.lastClickTime = currentTime;
                        this.useAptechka();
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private void useAptechka() {
        final int aptSlot = this.findAptSlot();
        if (aptSlot == -1) {
            return;
        }
        final int prevSlot = MiddleAPTModule.mc.field_1724.method_31548().field_7545;
        MiddleAPTModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(aptSlot));
        MiddleAPTModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2886(class_1268.field_5808, 0, MiddleAPTModule.mc.field_1724.method_36454(), MiddleAPTModule.mc.field_1724.method_36455()));
        MiddleAPTModule.mc.field_1724.method_31548().field_7545 = prevSlot;
        MiddleAPTModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(prevSlot));
    }
    
    private int findAptSlot() {
        if (MiddleAPTModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8601) {
            return MiddleAPTModule.mc.field_1724.method_31548().field_7545;
        }
        for (int i = 0; i < 9; ++i) {
            if (MiddleAPTModule.mc.field_1724.method_31548().method_5438(i).method_7909() == class_1802.field_8601) {
                return i;
            }
        }
        return -1;
    }
    
    @Generated
    public static MiddleAPTModule getInstance() {
        return MiddleAPTModule.instance;
    }
    
    static {
        instance = new MiddleAPTModule();
    }
}
