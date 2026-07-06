// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_124;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_2561;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Extra Tab", category = Category.RENDER)
public class ExtraTabModule extends Module
{
    private static final ExtraTabModule instance;
    public final SliderSetting tabSize;
    public final BooleanSetting friends;
    public final BooleanSetting yourself;
    
    public ExtraTabModule() {
        this.tabSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0442\u0430\u0431\u0430").value(200.0f).range(80.0f, 1000.0f).step(10.0f);
        this.friends = new BooleanSetting("\u0414\u0440\u0443\u0437\u044c\u044f").value(true);
        this.yourself = new BooleanSetting("\u0412\u044b\u0434\u0435\u043b\u0438\u0442\u044c \u0441\u0435\u0431\u044f").value(true);
        this.addSettings(this.tabSize, this.friends, this.yourself);
    }
    
    public class_2561 getModifiedName(final class_2561 originalText, final String name) {
        if (this.friends.getValue() && FriendManager.getInstance().contains(name)) {
            return (class_2561)class_2561.method_43470(name).method_27692(class_124.field_1060);
        }
        if (this.yourself.getValue() && ExtraTabModule.mc.field_1724 != null && ExtraTabModule.mc.field_1724.method_7334().getName().equals(name)) {
            return (class_2561)class_2561.method_43470(name).method_27692(class_124.field_1075);
        }
        return originalText;
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static ExtraTabModule getInstance() {
        return ExtraTabModule.instance;
    }
    
    static {
        instance = new ExtraTabModule();
    }
}
