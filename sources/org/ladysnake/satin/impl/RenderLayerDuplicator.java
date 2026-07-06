package org.ladysnake.satin.impl;

import java.util.function.Consumer;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/RenderLayerDuplicator.class */
public final class RenderLayerDuplicator {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/RenderLayerDuplicator$SatinRenderLayer.class */
    public interface SatinRenderLayer {
        class_1921 satin$copy(String str, @Nullable class_293 class_293Var, Consumer<class_1921.class_4688.class_4689> consumer);

        class_1921.class_4688 satin$copyPhaseParameters(Consumer<class_1921.class_4688.class_4689> consumer);
    }

    public static class_1921 copy(class_1921 existing, String newName, Consumer<class_1921.class_4688.class_4689> op) {
        return copy(existing, newName, null, op);
    }

    public static class_1921 copy(class_1921 existing, String newName, @Nullable class_293 vertexFormat, Consumer<class_1921.class_4688.class_4689> op) {
        checkDefaultImpl(existing);
        return ((SatinRenderLayer) existing).satin$copy(newName, vertexFormat, op);
    }

    public static class_1921.class_4688 copyPhaseParameters(class_1921 existing, Consumer<class_1921.class_4688.class_4689> op) {
        checkDefaultImpl(existing);
        return ((SatinRenderLayer) existing).satin$copyPhaseParameters(op);
    }

    private static void checkDefaultImpl(class_1921 existing) {
        if (!(existing instanceof SatinRenderLayer)) {
            throw new IllegalArgumentException("Unrecognized RenderLayer implementation " + String.valueOf(existing.getClass()) + ". Layer duplication is only applicable to the default (MultiPhase) implementation");
        }
    }
}
