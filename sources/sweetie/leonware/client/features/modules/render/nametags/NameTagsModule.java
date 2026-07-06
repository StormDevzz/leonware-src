package sweetie.leonware.client.features.modules.render.nametags;

import java.awt.Color;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.combat.TargetManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/nametags/NameTagsModule.class */
@ModuleRegister(name = "Name Tags", category = Category.RENDER)
public class NameTagsModule extends Module {
    private static final NameTagsModule instance = new NameTagsModule();
    public final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value((Boolean) false), new BooleanSetting("Players").value((Boolean) true), new BooleanSetting("Animals").value((Boolean) false), new BooleanSetting("Mobs").value((Boolean) false));
    public final ModeSetting textMode = new ModeSetting("Текст").values("Обычный", "Майнкрафт").value("Обычный");
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(1.0f)).range(0.1f, 2.0f).step(0.1f);
    public final MultiBooleanSetting information = new MultiBooleanSetting("Information").value(new BooleanSetting("Items").value((Boolean) true), new BooleanSetting("Potions").value((Boolean) true));
    private final Supplier<Boolean> itemsIsEnabled = () -> {
        return Boolean.valueOf(this.information.isEnabled("Items"));
    };
    public final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Special items").value((Boolean) false).setVisible(this.itemsIsEnabled), new BooleanSetting("Enchants").value((Boolean) true).setVisible(this.itemsIsEnabled), new BooleanSetting("Only hands").value((Boolean) false).setVisible(this.itemsIsEnabled));
    public final ModeSetting hpMode = new ModeSetting("Тип ХП").values("Полоска", "Цифры").value("Полоска");
    public final ModeSetting rectMode = new ModeSetting("Рект").values("Блюр", "Обычный").value("Блюр");
    public final SliderSetting glassy = new SliderSetting("Glassy").value(Float.valueOf(0.5f)).range(0.0f, 1.0f).step(0.1f).setVisible(() -> {
        return true;
    });
    public final ColorSetting textColor = new ColorSetting("Text color").value(new Color(255, 255, 255));
    public final ColorSetting color = new ColorSetting("Color").value(new Color(20, 20, 20));
    public final ColorSetting friendColor = new ColorSetting("Friend color").value(new Color(132, 229, 121)).setVisible(() -> {
        return Boolean.valueOf(this.targets.isEnabled("Players") || this.targets.isEnabled("Self"));
    });
    public final BooleanSetting zako64Spoof = new BooleanSetting("Zako64 Spoof").value((Boolean) false);
    public final BooleanSetting box3d = new BooleanSetting("Box 3D").value((Boolean) false);
    public final SliderSetting boxAlpha;
    public final TargetManager.EntityFilter entityFilter;
    private final NameTagsRender nameTagsRender;

    @Generated
    public static NameTagsModule getInstance() {
        return instance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v32, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    /* JADX WARN: Type inference failed for: r1v41, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public NameTagsModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Box Alpha").value(Float.valueOf(0.25f)).range(0.0f, 1.0f).step(0.05f);
        BooleanSetting booleanSetting = this.box3d;
        Objects.requireNonNull(booleanSetting);
        this.boxAlpha = sliderSettingStep.setVisible(booleanSetting::getValue);
        this.entityFilter = new TargetManager.EntityFilter(this.targets.getList());
        this.nameTagsRender = new NameTagsRender(this);
        addSettings(this.targets, this.rectMode, this.hpMode, this.textMode, this.scale, this.information, this.options, this.glassy, this.textColor, this.color, this.friendColor, this.zako64Spoof, this.box3d, this.boxAlpha);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener(1, event -> {
            this.entityFilter.targetSettings = this.targets.getList();
            this.entityFilter.needFriends = true;
            this.nameTagsRender.onRender(event);
        }));
        addEvents(render2DEvent);
    }
}
