/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.api.event.events.render;

import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.events.Event;

public class Render2DEvent
extends Event<Render2DEventData> {
    private static final Render2DEvent instance = new Render2DEvent();

    @Generated
    public static Render2DEvent getInstance() {
        return instance;
    }

    public record Render2DEventData(class_332 context, class_4587 matrixStack, float partialTicks) {
    }
}

