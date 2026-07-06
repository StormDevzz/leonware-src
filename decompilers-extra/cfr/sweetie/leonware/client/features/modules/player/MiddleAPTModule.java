/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1802
 *  net.minecraft.class_2596
 *  net.minecraft.class_2868
 *  net.minecraft.class_2886
 *  org.lwjgl.glfw.GLFW
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="Middle APT", category=Category.PLAYER)
public class MiddleAPTModule
extends Module {
    private static final MiddleAPTModule instance = new MiddleAPTModule();
    private long lastClickTime = 0L;

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            long currentTime;
            boolean middlePressed;
            if (MiddleAPTModule.mc.field_1724 == null || MiddleAPTModule.mc.field_1687 == null) {
                return;
            }
            boolean bl = middlePressed = GLFW.glfwGetMouseButton((long)mc.method_22683().method_4490(), (int)2) == 1;
            if (middlePressed && (currentTime = System.currentTimeMillis()) - this.lastClickTime >= 1500L) {
                this.lastClickTime = currentTime;
                this.useAptechka();
            }
        }));
        this.addEvents(updateEvent);
    }

    private void useAptechka() {
        int aptSlot = this.findAptSlot();
        if (aptSlot == -1) {
            return;
        }
        int prevSlot = MiddleAPTModule.mc.field_1724.method_31548().field_7545;
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
            if (MiddleAPTModule.mc.field_1724.method_31548().method_5438(i).method_7909() != class_1802.field_8601) continue;
            return i;
        }
        return -1;
    }

    @Generated
    public static MiddleAPTModule getInstance() {
        return instance;
    }
}

