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
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="View Model", category=Category.RENDER)
public class ViewModelModule
extends Module {
    private static final ViewModelModule instance = new ViewModelModule();
    public final SliderSetting rightX = new SliderSetting("Right X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting rightY = new SliderSetting("Right Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting rightZ = new SliderSetting("Right Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftX = new SliderSetting("Left X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftY = new SliderSetting("Left Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftZ = new SliderSetting("Left Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final BooleanSetting applyToHand = new BooleanSetting("Apply to Hand").value(false);
    public final SliderSetting handX = new SliderSetting("Hand X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f).setVisible(this.applyToHand::getValue);
    public final SliderSetting handY = new SliderSetting("Hand Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f).setVisible(this.applyToHand::getValue);
    public final SliderSetting handZ = new SliderSetting("Hand Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f).setVisible(this.applyToHand::getValue);
    private final RunSetting reset = new RunSetting("Reset position").value(this::resetPos);

    public ViewModelModule() {
        this.addSettings(this.rightX, this.rightY, this.rightZ, this.leftX, this.leftY, this.leftZ, this.applyToHand, this.reset, this.handX, this.handY, this.handZ);
    }

    @Override
    public void onEvent() {
    }

    private void resetPos() {
        this.getSettings().forEach(setting -> {
            if (setting instanceof SliderSetting) {
                SliderSetting f = (SliderSetting)setting;
                f.setValue(Float.valueOf(0.0f));
            }
        });
    }

    @Generated
    public static ViewModelModule getInstance() {
        return instance;
    }
}

