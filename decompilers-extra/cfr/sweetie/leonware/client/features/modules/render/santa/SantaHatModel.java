/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4608
 *  net.minecraft.class_5603
 *  net.minecraft.class_5605
 *  net.minecraft.class_5606
 *  net.minecraft.class_5609
 *  net.minecraft.class_5610
 *  net.minecraft.class_630
 */
package sweetie.leonware.client.features.modules.render.santa;

import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5603;
import net.minecraft.class_5605;
import net.minecraft.class_5606;
import net.minecraft.class_5609;
import net.minecraft.class_5610;
import net.minecraft.class_630;

public class SantaHatModel {
    private final class_630 root = SantaHatModel.createModel();
    private final class_630 santaHat = this.root.method_32086("santa_hat");

    private static class_630 createModel() {
        class_5609 modelData = new class_5609();
        class_5610 modelPartData = modelData.method_32111();
        class_5610 santaHat = modelPartData.method_32117("santa_hat", class_5606.method_32108(), class_5603.method_32091((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-1.5707964f, (float)0.0f));
        santaHat.method_32117("sant_hat_top2", class_5606.method_32108().method_32101(0, 0).method_32097(-0.5f, 2.0f, -1.5f, 3.0f, 3.0f, 3.0f), class_5603.method_32091((float)0.53024f, (float)10.61642f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-1.0471976f));
        santaHat.method_32117("sant_hat_top1", class_5606.method_32108().method_32101(0, 0).method_32097(-3.0f, -1.0f, -3.0f, 6.0f, 4.0f, 6.0f), class_5603.method_32091((float)1.03024f, (float)9.75039f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-0.87266463f));
        santaHat.method_32117("santa_hat_top0", class_5606.method_32108().method_32101(0, 0).method_32097(-4.0f, -1.0f, -4.0f, 9.0f, 3.0f, 8.0f), class_5603.method_32091((float)0.2892f, (float)8.72718f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-0.34906584f));
        santaHat.method_32117("sant_hat_top3", class_5606.method_32108().method_32101(0, 16).method_32097(-0.90192f, -1.83013f, -2.0f, 4.0f, 4.0f, 4.0f), class_5603.method_32091((float)5.78024f, (float)12.11642f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.2617994f));
        santaHat.method_32117("santa_hat_base", class_5606.method_32108().method_32101(0, 16).method_32098(-4.5f, -3.0f, -4.5f, 10.0f, 4.0f, 9.0f, new class_5605(0.1f)), class_5603.method_32091((float)0.2892f, (float)8.72718f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-0.2617994f));
        return modelPartData.method_32112(64, 32);
    }

    public void render(class_4587 matrices, class_4597 vertexConsumers, int light, class_2960 texture) {
        class_4588 vertexConsumer = vertexConsumers.getBuffer(class_1921.method_23578((class_2960)texture));
        this.root.method_22698(matrices, vertexConsumer, light, class_4608.field_21444);
    }
}

