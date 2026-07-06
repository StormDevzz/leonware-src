// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10017;
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

public class Leon2DModel extends class_583<class_10055>
{
    public final class_630 plane;
    
    public Leon2DModel(final class_630 root) {
        super(root, (Function)class_1921::method_23578);
        this.plane = root.method_32086("plane");
    }
    
    public static class_5607 createTexturedModelData() {
        final class_5609 modelData = new class_5609();
        final class_5610 root = modelData.method_32111();
        root.method_32117("plane", class_5606.method_32108().method_32101(0, 0).method_32097(-8.0f, -16.0f, -0.5f, 16.0f, 32.0f, 1.0f), class_5603.method_32090(0.0f, 8.0f, 0.0f));
        return class_5607.method_32110(modelData, 32, 64);
    }
    
    public void setAngles(final class_10055 state) {
    }
}
