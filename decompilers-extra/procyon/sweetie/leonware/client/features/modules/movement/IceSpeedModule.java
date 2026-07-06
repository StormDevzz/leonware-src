// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Ice Speed", category = Category.MOVEMENT)
public class IceSpeedModule extends Module
{
    private static final IceSpeedModule instance;
    public final SliderSetting speed;
    
    public IceSpeedModule() {
        this.speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(0.4f).range(0.1f, 1.5f).step(0.01f);
        this.addSettings(this.speed);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static IceSpeedModule getInstance() {
        return IceSpeedModule.instance;
    }
    
    static {
        instance = new IceSpeedModule();
    }
}
