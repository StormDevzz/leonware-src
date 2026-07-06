package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CustomModelModule.class */
@ModuleRegister(name = "Custom Model", category = Category.RENDER)
public class CustomModelModule extends Module {
    private static final CustomModelModule instance = new CustomModelModule();
    public final ModeSetting model = new ModeSetting("Модель").value("CrazyRabbit").values("CrazyRabbit", "Freddy Bear", "Amogus", "Leon 2D");
    public final BooleanSetting friends = new BooleanSetting("Для кентов").value((Boolean) true);
    public final SliderSetting rightX = new SliderSetting("Right X").value(Float.valueOf(-0.35f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting rightY = new SliderSetting("Right Y").value(Float.valueOf(0.45f)).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting rightZ = new SliderSetting("Right Z").value(Float.valueOf(0.05f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting rightRot = new SliderSetting("Right Rotate").value(Float.valueOf(-15.0f)).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting leftX = new SliderSetting("Left X").value(Float.valueOf(0.55f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting leftY = new SliderSetting("Left Y").value(Float.valueOf(0.5f)).range(-1.0f, 1.5f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting leftZ = new SliderSetting("Left Z").value(Float.valueOf(-0.15f)).range(-1.0f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final SliderSetting leftRot = new SliderSetting("Left Rotate").value(Float.valueOf(15.0f)).range(-45.0f, 45.0f).step(0.5f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Freddy Bear"));
    });
    public final BooleanSetting amogusHideItems = new BooleanSetting("Скрыть предметы").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Amogus"));
    });
    public final BooleanSetting leonHideArms = new BooleanSetting("Скрыть руки").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });
    public final SliderSetting leon2dX = new SliderSetting("2D X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });
    public final SliderSetting leon2dY = new SliderSetting("2D Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });
    public final SliderSetting leon2dZ = new SliderSetting("2D Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });
    public final SliderSetting leon2dScale = new SliderSetting("2D Scale").value(Float.valueOf(1.0f)).range(0.1f, 4.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });
    public final SliderSetting leon2dRotateY = new SliderSetting("2D Rotate Y").value(Float.valueOf(0.0f)).range(-180.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.model.is("Leon 2D"));
    });

    @Generated
    public static CustomModelModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v29, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v34, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v39, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v44, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v47, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v50, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v55, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v60, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v65, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v70, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v75, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CustomModelModule() {
        addSettings(this.model, this.friends, this.rightX, this.rightY, this.rightZ, this.rightRot, this.leftX, this.leftY, this.leftZ, this.leftRot, this.amogusHideItems, this.leonHideArms, this.leon2dX, this.leon2dY, this.leon2dZ, this.leon2dScale, this.leon2dRotateY);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
