// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10017;
import net.minecraft.class_591;
import net.minecraft.class_5610;
import net.minecraft.class_5603;
import net.minecraft.class_5606;
import net.minecraft.class_5609;
import net.minecraft.class_5607;
import java.util.function.Function;
import net.minecraft.class_1921;
import net.minecraft.class_630;
import net.minecraft.class_10055;
import net.minecraft.class_583;

public class AmogusModel extends class_583<class_10055>
{
    public final class_630 body;
    public final class_630 eye;
    public final class_630 leftLeg;
    public final class_630 rightLeg;
    
    public AmogusModel(final class_630 root) {
        super(root, (Function)class_1921::method_23578);
        this.body = root.method_32086("body");
        this.eye = root.method_32086("eye");
        this.leftLeg = root.method_32086("leftLeg");
        this.rightLeg = root.method_32086("rightLeg");
    }
    
    public static class_5607 createTexturedModelData() {
        final class_5609 modelData = new class_5609();
        final class_5610 root = modelData.method_32111();
        root.method_32117("body", class_5606.method_32108().method_32101(34, 8).method_32097(-4.0f, 6.0f, -3.0f, 8.0f, 12.0f, 6.0f).method_32101(15, 10).method_32097(-3.0f, 9.0f, 3.0f, 6.0f, 8.0f, 3.0f).method_32101(26, 0).method_32097(-3.0f, 5.0f, -3.0f, 6.0f, 1.0f, 6.0f), class_5603.field_27701);
        root.method_32117("eye", class_5606.method_32108().method_32101(0, 10).method_32097(-3.0f, 7.0f, -4.0f, 6.0f, 4.0f, 1.0f), class_5603.field_27701);
        root.method_32117("leftLeg", class_5606.method_32108().method_32101(0, 0).method_32097(2.9f, 0.0f, -1.5f, 3.0f, 6.0f, 3.0f), class_5603.method_32090(-2.0f, 18.0f, 0.0f));
        root.method_32117("rightLeg", class_5606.method_32108().method_32101(13, 0).method_32097(-5.9f, 0.0f, -1.5f, 3.0f, 6.0f, 3.0f), class_5603.method_32090(2.0f, 18.0f, 0.0f));
        return class_5607.method_32110(modelData, 64, 64);
    }
    
    public void setAngles(final class_10055 state) {
    }
    
    public void copyPoseFromBase(final class_591 base) {
        this.leftLeg.field_3654 = base.field_3397.field_3654;
        this.leftLeg.field_3675 = base.field_3397.field_3675;
        this.leftLeg.field_3674 = base.field_3397.field_3674;
        this.rightLeg.field_3654 = base.field_3392.field_3654;
        this.rightLeg.field_3675 = base.field_3392.field_3675;
        this.rightLeg.field_3674 = base.field_3392.field_3674;
    }
}
