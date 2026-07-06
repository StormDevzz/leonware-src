/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Custom Model", category=Category.RENDER)
public class CustomModelModule
extends Module {
    private static final CustomModelModule instance = new CustomModelModule();
    public final ModeSetting model = new ModeSetting("\u041c\u043e\u0434\u0435\u043b\u044c").value("CrazyRabbit").values("CrazyRabbit", "Freddy Bear", "Amogus", "Leon 2D");
    public final BooleanSetting friends = new BooleanSetting("\u0414\u043b\u044f \u043a\u0435\u043d\u0442\u043e\u0432").value(true);
    public final SliderSetting rightX = new SliderSetting("Right X").value(Float.valueOf(-0.35f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting rightY = new SliderSetting("Right Y").value(Float.valueOf(0.45f)).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting rightZ = new SliderSetting("Right Z").value(Float.valueOf(0.05f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting rightRot = new SliderSetting("Right Rotate").value(Float.valueOf(-15.0f)).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting leftX = new SliderSetting("Left X").value(Float.valueOf(0.55f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting leftY = new SliderSetting("Left Y").value(Float.valueOf(0.5f)).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting leftZ = new SliderSetting("Left Z").value(Float.valueOf(-0.15f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> this.model.is("Freddy Bear"));
    public final SliderSetting leftRot = new SliderSetting("Left Rotate").value(Float.valueOf(15.0f)).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> this.model.is("Freddy Bear"));
    public final BooleanSetting amogusHideItems = new BooleanSetting("\u0421\u043a\u0440\u044b\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b").value(true).setVisible(() -> this.model.is("Amogus"));
    public final BooleanSetting leonHideArms = new BooleanSetting("\u0421\u043a\u0440\u044b\u0442\u044c \u0440\u0443\u043a\u0438").value(true).setVisible(() -> this.model.is("Leon 2D"));
    public final SliderSetting leon2dX = new SliderSetting("2D X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
    public final SliderSetting leon2dY = new SliderSetting("2D Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
    public final SliderSetting leon2dZ = new SliderSetting("2D Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
    public final SliderSetting leon2dScale = new SliderSetting("2D Scale").value(Float.valueOf(1.0f)).range(0.1f, 4.0f).step(0.05f).setVisible(() -> this.model.is("Leon 2D"));
    public final SliderSetting leon2dRotateY = new SliderSetting("2D Rotate Y").value(Float.valueOf(0.0f)).range(-180.0f, 180.0f).step(1.0f).setVisible(() -> this.model.is("Leon 2D"));

    public CustomModelModule() {
        this.addSettings(this.model, this.friends, this.rightX, this.rightY, this.rightZ, this.rightRot, this.leftX, this.leftY, this.leftZ, this.leftRot, this.amogusHideItems, this.leonHideArms, this.leon2dX, this.leon2dY, this.leon2dZ, this.leon2dScale, this.leon2dRotateY);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static CustomModelModule getInstance() {
        return instance;
    }
}

