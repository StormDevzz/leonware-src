// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.santa;

import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_5610;
import net.minecraft.class_5605;
import net.minecraft.class_5603;
import net.minecraft.class_5606;
import net.minecraft.class_5609;
import net.minecraft.class_630;

public class SantaHatModel
{
    private final class_630 root;
    private final class_630 santaHat;
    
    public SantaHatModel() {
        this.root = createModel();
        this.santaHat = this.root.method_32086("santa_hat");
    }
    
    private static class_630 createModel() {
        final class_5609 modelData = new class_5609();
        final class_5610 modelPartData = modelData.method_32111();
        final class_5610 santaHat = modelPartData.method_32117("santa_hat", class_5606.method_32108(), class_5603.method_32091(0.0f, 0.0f, 0.0f, 0.0f, -1.5707964f, 0.0f));
        santaHat.method_32117("sant_hat_top2", class_5606.method_32108().method_32101(0, 0).method_32097(-0.5f, 2.0f, -1.5f, 3.0f, 3.0f, 3.0f), class_5603.method_32091(0.53024f, 10.61642f, 0.0f, 0.0f, 0.0f, -1.0471976f));
        santaHat.method_32117("sant_hat_top1", class_5606.method_32108().method_32101(0, 0).method_32097(-3.0f, -1.0f, -3.0f, 6.0f, 4.0f, 6.0f), class_5603.method_32091(1.03024f, 9.75039f, 0.0f, 0.0f, 0.0f, -0.87266463f));
        santaHat.method_32117("santa_hat_top0", class_5606.method_32108().method_32101(0, 0).method_32097(-4.0f, -1.0f, -4.0f, 9.0f, 3.0f, 8.0f), class_5603.method_32091(0.2892f, 8.72718f, 0.0f, 0.0f, 0.0f, -0.34906584f));
        santaHat.method_32117("sant_hat_top3", class_5606.method_32108().method_32101(0, 16).method_32097(-0.90192f, -1.83013f, -2.0f, 4.0f, 4.0f, 4.0f), class_5603.method_32091(5.78024f, 12.11642f, 0.0f, 0.0f, 0.0f, 0.2617994f));
        santaHat.method_32117("santa_hat_base", class_5606.method_32108().method_32101(0, 16).method_32098(-4.5f, -3.0f, -4.5f, 10.0f, 4.0f, 9.0f, new class_5605(0.1f)), class_5603.method_32091(0.2892f, 8.72718f, 0.0f, 0.0f, 0.0f, -0.2617994f));
        return modelPartData.method_32112(64, 32);
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_2960 texture) {
        final class_4588 vertexConsumer = vertexConsumers.getBuffer(class_1921.method_23578(texture));
        this.root.method_22698(matrices, vertexConsumer, light, class_4608.field_21444);
    }
}
