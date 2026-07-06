/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_4587
 */
package sweetie.leonware.api.event.events.render;

import lombok.Generated;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.events.Event;

public class Render3DEvent
extends Event<Render3DEventData> {
    private static final Render3DEvent instance = new Render3DEvent();

    @Generated
    public static Render3DEvent getInstance() {
        return instance;
    }

    public record Render3DEventData(class_4587 matrixStack, float partialTicks) {
    }
}

