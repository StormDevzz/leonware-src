package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_332;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/interfaces/UIApi.class */
public interface UIApi {
    void render(class_332 class_332Var, int i, int i2, float f);

    void keyPressed(int i, int i2, int i3);

    void mouseClicked(double d, double d2, int i);

    void mouseReleased(double d, double d2, int i);

    void mouseScrolled(double d, double d2, double d3, double d4);
}
