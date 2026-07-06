// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.leon;

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

public class LeonHeadModel
{
    private static final float DEG = 0.017453292f;
    private final class_630 root;
    
    public LeonHeadModel() {
        this.root = createModel();
    }
    
    private static class_630 createModel() {
        final class_5609 modelData = new class_5609();
        final class_5610 rootData = modelData.method_32111();
        final class_5610 leon = rootData.method_32117("leon", class_5606.method_32108(), class_5603.field_27701);
        leon.method_32117("headband_front", class_5606.method_32108().method_32101(0, 0).method_32097(-5.0f, -1.0f, -5.2f, 10.0f, 2.0f, 1.0f), class_5603.method_32090(0.0f, 6.0f, 0.0f));
        leon.method_32117("headband_back", class_5606.method_32108().method_32101(0, 4).method_32097(-5.0f, -1.0f, 4.2f, 10.0f, 2.0f, 1.0f), class_5603.method_32090(0.0f, 6.0f, 0.0f));
        leon.method_32117("headband_left", class_5606.method_32108().method_32101(22, 0).method_32097(-5.2f, -1.0f, -4.2f, 1.0f, 2.0f, 8.0f), class_5603.method_32090(0.0f, 6.0f, 0.0f));
        leon.method_32117("headband_right", class_5606.method_32108().method_32101(22, 4).method_32097(4.2f, -1.0f, -4.2f, 1.0f, 2.0f, 8.0f), class_5603.method_32090(0.0f, 6.0f, 0.0f));
        leon.method_32117("hair_top", class_5606.method_32108().method_32101(0, 10).method_32098(-4.5f, 0.0f, -4.5f, 9.0f, 2.0f, 9.0f, new class_5605(0.05f)), class_5603.method_32090(0.0f, 8.0f, 0.0f));
        leon.method_32117("hair_vol_left", class_5606.method_32108().method_32101(0, 22).method_32097(-1.3f, -3.0f, -4.0f, 2.0f, 5.0f, 8.0f), class_5603.method_32090(-4.0f, 8.0f, 0.0f));
        leon.method_32117("hair_vol_right", class_5606.method_32108().method_32101(14, 22).method_32097(-0.7f, -3.0f, -4.0f, 2.0f, 5.0f, 8.0f), class_5603.method_32090(4.0f, 8.0f, 0.0f));
        leon.method_32117("hair_vol_back", class_5606.method_32108().method_32101(28, 22).method_32097(-4.5f, -2.5f, 0.2f, 9.0f, 4.0f, 2.0f), class_5603.method_32090(0.0f, 8.0f, 4.0f));
        leon.method_32117("spike_c", class_5606.method_32108().method_32101(0, 37).method_32097(-1.5f, 0.0f, -1.5f, 3.0f, 9.0f, 3.0f), class_5603.method_32091(0.0f, 9.0f, 0.5f, -0.17453292f, 0.0f, 0.0f));
        leon.method_32117("spike_fl", class_5606.method_32108().method_32101(12, 37).method_32097(-1.0f, 0.0f, -1.0f, 2.0f, 7.0f, 2.0f), class_5603.method_32091(-2.0f, 9.0f, -2.5f, -0.38397244f, -0.20943952f, -0.34906584f));
        leon.method_32117("spike_fr", class_5606.method_32108().method_32101(20, 37).method_32097(-1.0f, 0.0f, -1.0f, 2.0f, 7.0f, 2.0f), class_5603.method_32091(2.0f, 9.0f, -2.5f, -0.38397244f, 0.20943952f, 0.34906584f));
        leon.method_32117("spike_l", class_5606.method_32108().method_32101(28, 37).method_32097(-1.0f, 0.0f, -1.5f, 2.0f, 8.0f, 3.0f), class_5603.method_32091(-3.0f, 9.0f, 0.5f, -0.08726646f, 0.0f, -0.5235988f));
        leon.method_32117("spike_r", class_5606.method_32108().method_32101(38, 37).method_32097(-1.0f, 0.0f, -1.5f, 2.0f, 8.0f, 3.0f), class_5603.method_32091(3.0f, 9.0f, 0.5f, -0.08726646f, 0.0f, 0.5235988f));
        leon.method_32117("spike_bl", class_5606.method_32108().method_32101(48, 37).method_32097(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f), class_5603.method_32091(-2.5f, 9.0f, 2.5f, 0.34906584f, -0.2617994f, -0.17453292f));
        leon.method_32117("spike_br", class_5606.method_32108().method_32101(56, 37).method_32097(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f), class_5603.method_32091(2.5f, 9.0f, 2.5f, 0.34906584f, 0.2617994f, 0.17453292f));
        leon.method_32117("spike_fc", class_5606.method_32108().method_32101(64, 37).method_32097(-1.0f, 0.0f, -1.0f, 2.0f, 5.0f, 2.0f), class_5603.method_32091(0.0f, 9.0f, -3.5f, -0.55850536f, 0.0f, 0.0f));
        leon.method_32117("goggle_l_frame", class_5606.method_32108().method_32101(64, 0).method_32097(-2.0f, -2.0f, -0.8f, 4.0f, 4.0f, 1.0f), class_5603.method_32090(-2.5f, 7.5f, -4.5f));
        leon.method_32117("goggle_l_lens", class_5606.method_32108().method_32101(74, 0).method_32097(-1.5f, -1.5f, -1.1f, 3.0f, 3.0f, 1.0f), class_5603.method_32090(-2.5f, 7.5f, -4.5f));
        leon.method_32117("goggle_r_frame", class_5606.method_32108().method_32101(64, 6).method_32097(-2.0f, -2.0f, -0.8f, 4.0f, 4.0f, 1.0f), class_5603.method_32090(2.5f, 7.5f, -4.5f));
        leon.method_32117("goggle_r_lens", class_5606.method_32108().method_32101(74, 6).method_32097(-1.5f, -1.5f, -1.1f, 3.0f, 3.0f, 1.0f), class_5603.method_32090(2.5f, 7.5f, -4.5f));
        leon.method_32117("goggle_bridge", class_5606.method_32108().method_32101(86, 0).method_32097(-0.5f, -0.5f, -0.8f, 1.0f, 1.0f, 1.0f), class_5603.method_32090(0.0f, 7.5f, -4.5f));
        leon.method_32117("goggle_strap_l", class_5606.method_32108().method_32101(86, 3).method_32097(0.0f, -0.5f, 0.0f, 2.0f, 1.0f, 1.0f), class_5603.method_32090(-4.5f, 7.5f, -4.2f));
        leon.method_32117("goggle_strap_r", class_5606.method_32108().method_32101(86, 6).method_32097(-2.0f, -0.5f, 0.0f, 2.0f, 1.0f, 1.0f), class_5603.method_32090(4.5f, 7.5f, -4.2f));
        return rootData.method_32112(128, 64);
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_2960 texture) {
        final class_4588 vertexConsumer = vertexConsumers.getBuffer(class_1921.method_23578(texture));
        this.root.method_22698(matrices, vertexConsumer, light, class_4608.field_21444);
    }
}
