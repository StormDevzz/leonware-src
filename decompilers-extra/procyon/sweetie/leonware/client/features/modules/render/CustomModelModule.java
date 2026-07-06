// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom Model", category = Category.RENDER)
public class CustomModelModule extends Module
{
    private static final CustomModelModule instance;
    public final ModeSetting model;
    public final BooleanSetting friends;
    public final SliderSetting rightX;
    public final SliderSetting rightY;
    public final SliderSetting rightZ;
    public final SliderSetting rightRot;
    public final SliderSetting leftX;
    public final SliderSetting leftY;
    public final SliderSetting leftZ;
    public final SliderSetting leftRot;
    public final BooleanSetting amogusHideItems;
    public final BooleanSetting leonHideArms;
    public final SliderSetting leon2dX;
    public final SliderSetting leon2dY;
    public final SliderSetting leon2dZ;
    public final SliderSetting leon2dScale;
    public final SliderSetting leon2dRotateY;
    
    public CustomModelModule() {
        this.model = new ModeSetting("\u041c\u043e\u0434\u0435\u043b\u044c").value("CrazyRabbit").values("CrazyRabbit", "Freddy Bear", "Amogus", "Leon 2D");
        this.friends = new BooleanSetting("\u0414\u043b\u044f \u043a\u0435\u043d\u0442\u043e\u0432").value(true);
        this.rightX = new SliderSetting("Right X").value(-0.35f).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.rightY = new SliderSetting("Right Y").value(0.45f).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.rightZ = new SliderSetting("Right Z").value(0.05f).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.rightRot = new SliderSetting("Right Rotate").value(-15.0f).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> this.model.is("Freddy Bear"));
        this.leftX = new SliderSetting("Left X").value(0.55f).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.leftY = new SliderSetting("Left Y").value(0.5f).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.leftZ = new SliderSetting("Left Z").value(-0.15f).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
        this.leftRot = new SliderSetting("Left Rotate").value(15.0f).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> this.model.is("Freddy Bear"));
        this.amogusHideItems = new BooleanSetting("\u0421\u043a\u0440\u044b\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b").value(true).setVisible(() -> this.model.is("Amogus"));
        this.leonHideArms = new BooleanSetting("\u0421\u043a\u0440\u044b\u0442\u044c \u0440\u0443\u043a\u0438").value(true).setVisible(() -> this.model.is("Leon 2D"));
        this.leon2dX = new SliderSetting("2D X").value(0.0f).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
        this.leon2dY = new SliderSetting("2D Y").value(0.0f).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
        this.leon2dZ = new SliderSetting("2D Z").value(0.0f).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
        this.leon2dScale = new SliderSetting("2D Scale").value(1.0f).range(0.1f, 4.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
        this.leon2dRotateY = new SliderSetting("2D Rotate Y").value(0.0f).range(-180.0f, 180.0f).step(1.0f).setVisible(() -> this.model.is("Leon 2D"));
        this.addSettings(this.model, this.friends, this.rightX, this.rightY, this.rightZ, this.rightRot, this.leftX, this.leftY, this.leftZ, this.leftRot, this.amogusHideItems, this.leonHideArms, this.leon2dX, this.leon2dY, this.leon2dZ, this.leon2dScale, this.leon2dRotateY);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static CustomModelModule getInstance() {
        return CustomModelModule.instance;
    }
    
    static {
        instance = new CustomModelModule();
    }
}
