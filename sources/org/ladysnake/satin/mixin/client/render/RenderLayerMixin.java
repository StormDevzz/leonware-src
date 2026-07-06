package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_1921;
import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/render/RenderLayerMixin.class */
@Mixin({class_1921.class})
public abstract class RenderLayerMixin extends class_4668 {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/render/RenderLayerMixin$MultiPhaseParametersAccessor.class */
    @Mixin({class_1921.class_4688.class})
    public interface MultiPhaseParametersAccessor {
        @Accessor
        class_4668.class_5939 getTexture();

        @Accessor
        class_4668.class_5942 getProgram();

        @Accessor
        class_4668.class_4685 getTransparency();

        @Accessor
        class_4668.class_4672 getDepthTest();

        @Accessor
        class_4668.class_4671 getCull();

        @Accessor
        class_4668.class_4676 getLightmap();

        @Accessor
        class_4668.class_4679 getOverlay();

        @Accessor
        class_4668.class_4675 getLayering();

        @Accessor
        class_4668.class_4678 getTarget();

        @Accessor
        class_4668.class_4684 getTexturing();

        @Accessor
        class_4668.class_4686 getWriteMaskState();

        @Accessor
        class_4668.class_4677 getLineWidth();

        @Accessor
        class_1921.class_4750 getOutlineMode();
    }

    public RenderLayerMixin(String name, Runnable beginAction, Runnable endAction) {
        super(name, beginAction, endAction);
    }
}
