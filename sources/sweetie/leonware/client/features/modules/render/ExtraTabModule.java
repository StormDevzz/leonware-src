package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ExtraTabModule.class */
@ModuleRegister(name = "Extra Tab", category = Category.RENDER)
public class ExtraTabModule extends Module {
    private static final ExtraTabModule instance = new ExtraTabModule();
    public final SliderSetting tabSize = new SliderSetting("Размер таба").value(Float.valueOf(200.0f)).range(80.0f, 1000.0f).step(10.0f);
    public final BooleanSetting friends = new BooleanSetting("Друзья").value((Boolean) true);
    public final BooleanSetting yourself = new BooleanSetting("Выделить себя").value((Boolean) true);

    @Generated
    public static ExtraTabModule getInstance() {
        return instance;
    }

    public ExtraTabModule() {
        addSettings(this.tabSize, this.friends, this.yourself);
    }

    public class_2561 getModifiedName(class_2561 originalText, String name) {
        if (this.friends.getValue().booleanValue() && FriendManager.getInstance().contains(name)) {
            return class_2561.method_43470(name).method_27692(class_124.field_1060);
        }
        if (this.yourself.getValue().booleanValue() && mc.field_1724 != null && mc.field_1724.method_7334().getName().equals(name)) {
            return class_2561.method_43470(name).method_27692(class_124.field_1075);
        }
        return originalText;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
