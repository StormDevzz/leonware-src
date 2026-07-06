// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.nametags;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Objects;
import java.awt.Color;
import sweetie.leonware.api.utils.combat.TargetManager;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Name Tags", category = Category.RENDER)
public class NameTagsModule extends Module
{
    private static final NameTagsModule instance;
    public final MultiBooleanSetting targets;
    public final ModeSetting textMode;
    public final SliderSetting scale;
    public final MultiBooleanSetting information;
    private final Supplier<Boolean> itemsIsEnabled;
    public final MultiBooleanSetting options;
    public final ModeSetting hpMode;
    public final ModeSetting rectMode;
    public final SliderSetting glassy;
    public final ColorSetting textColor;
    public final ColorSetting color;
    public final ColorSetting friendColor;
    public final BooleanSetting zako64Spoof;
    public final BooleanSetting box3d;
    public final SliderSetting boxAlpha;
    public final TargetManager.EntityFilter entityFilter;
    private final NameTagsRender nameTagsRender;
    
    public NameTagsModule() {
        this.targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value(false), new BooleanSetting("Players").value(true), new BooleanSetting("Animals").value(false), new BooleanSetting("Mobs").value(false));
        this.textMode = new ModeSetting("\u0422\u0435\u043a\u0441\u0442").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
        this.scale = new SliderSetting("Scale").value(1.0f).range(0.1f, 2.0f).step(0.1f);
        this.information = new MultiBooleanSetting("Information").value(new BooleanSetting("Items").value(true), new BooleanSetting("Potions").value(true));
        this.itemsIsEnabled = (() -> this.information.isEnabled("Items"));
        this.options = new MultiBooleanSetting("Options").value(new BooleanSetting("Special items").value(false).setVisible(this.itemsIsEnabled), new BooleanSetting("Enchants").value(true).setVisible(this.itemsIsEnabled), new BooleanSetting("Only hands").value(false).setVisible(this.itemsIsEnabled));
        this.hpMode = new ModeSetting("\u0422\u0438\u043f \u0425\u041f").values("\u041f\u043e\u043b\u043e\u0441\u043a\u0430", "\u0426\u0438\u0444\u0440\u044b").value("\u041f\u043e\u043b\u043e\u0441\u043a\u0430");
        this.rectMode = new ModeSetting("\u0420\u0435\u043a\u0442").values("\u0411\u043b\u044e\u0440", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439").value("\u0411\u043b\u044e\u0440");
        this.glassy = new SliderSetting("Glassy").value(0.5f).range(0.0f, 1.0f).step(0.1f).setVisible(() -> true);
        this.textColor = new ColorSetting("Text color").value(new Color(255, 255, 255));
        this.color = new ColorSetting("Color").value(new Color(20, 20, 20));
        this.friendColor = new ColorSetting("Friend color").value(new Color(132, 229, 121)).setVisible(() -> this.targets.isEnabled("Players") || this.targets.isEnabled("Self"));
        this.zako64Spoof = new BooleanSetting("Zako64 Spoof").value(false);
        this.box3d = new BooleanSetting("Box 3D").value(false);
        final SliderSetting step = new SliderSetting("Box Alpha").value(0.25f).range(0.0f, 1.0f).step(0.05f);
        final BooleanSetting box3d = this.box3d;
        Objects.requireNonNull(box3d);
        this.boxAlpha = step.setVisible((Supplier<Boolean>)box3d::getValue);
        this.entityFilter = new TargetManager.EntityFilter(this.targets.getList());
        this.nameTagsRender = new NameTagsRender(this);
        this.addSettings(this.targets, this.rectMode, this.hpMode, this.textMode, this.scale, this.information, this.options, this.glassy, this.textColor, this.color, this.friendColor, this.zako64Spoof, this.box3d, this.boxAlpha);
    }
    
    @Override
    public void onEvent() {
        final EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(1, event -> {
            this.entityFilter.targetSettings = this.targets.getList();
            this.entityFilter.needFriends = true;
            this.nameTagsRender.onRender(event);
            return;
        }));
        this.addEvents(render2DEvent);
    }
    
    @Generated
    public static NameTagsModule getInstance() {
        return NameTagsModule.instance;
    }
    
    static {
        instance = new NameTagsModule();
    }
}
