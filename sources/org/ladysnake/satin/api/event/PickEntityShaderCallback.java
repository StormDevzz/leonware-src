package org.ladysnake.satin.api.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1297;
import net.minecraft.class_279;
import net.minecraft.class_2960;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/PickEntityShaderCallback.class */
public interface PickEntityShaderCallback {
    public static final Event<PickEntityShaderCallback> EVENT = EventFactory.createArrayBacked(PickEntityShaderCallback.class, listeners -> {
        return (entity, loadShaderFunc, appliedShaderGetter) -> {
            for (PickEntityShaderCallback handler : listeners) {
                handler.pickEntityShader(entity, loadShaderFunc, appliedShaderGetter);
                if (appliedShaderGetter.get() != null) {
                    return;
                }
            }
        };
    });

    void pickEntityShader(@Nullable class_1297 class_1297Var, Consumer<class_2960> consumer, Supplier<class_279> supplier);
}
