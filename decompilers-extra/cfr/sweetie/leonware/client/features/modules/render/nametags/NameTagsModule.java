/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render.nametags;

import java.awt.Color;
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
import sweetie.leonware.client.features.modules.render.nametags.NameTagsRender;

@ModuleRegister(name="Name Tags", category=Category.RENDER)
public class NameTagsModule
extends Module {
    private static final NameTagsModule instance = new NameTagsModule();
    public final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value(false), new BooleanSetting("Players").value(true), new BooleanSetting("Animals").value(false), new BooleanSetting("Mobs").value(false));
    public final ModeSetting textMode = new ModeSetting("\u0422\u0435\u043a\u0441\u0442").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041c\u0430\u0439\u043d\u043a\u0440\u0430\u0444\u0442").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    public final SliderSetting scale = new SliderSetting("Scale").value(Float.valueOf(1.0f)).range(0.1f, 2.0f).step(0.1f);
    public final MultiBooleanSetting information = new MultiBooleanSetting("Information").value(new BooleanSetting("Items").value(true), new BooleanSetting("Potions").value(true));
    private final Supplier<Boolean> itemsIsEnabled = () -> this.information.isEnabled("Items");
    public final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting[]{new BooleanSetting("Special items").value(false).setVisible((Supplier)this.itemsIsEnabled), new BooleanSetting("Enchants").value(true).setVisible((Supplier)this.itemsIsEnabled), new BooleanSetting("Only hands").value(false).setVisible((Supplier)this.itemsIsEnabled)});
    public final ModeSetting hpMode = new ModeSetting("\u0422\u0438\u043f \u0425\u041f").values("\u041f\u043e\u043b\u043e\u0441\u043a\u0430", "\u0426\u0438\u0444\u0440\u044b").value("\u041f\u043e\u043b\u043e\u0441\u043a\u0430");
    public final ModeSetting rectMode = new ModeSetting("\u0420\u0435\u043a\u0442").values("\u0411\u043b\u044e\u0440", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439").value("\u0411\u043b\u044e\u0440");
    public final SliderSetting glassy = new SliderSetting("Glassy").value(Float.valueOf(0.5f)).range(0.0f, 1.0f).step(0.1f).setVisible(() -> true);
    public final ColorSetting textColor = new ColorSetting("Text color").value(new Color(255, 255, 255));
    public final ColorSetting color = new ColorSetting("Color").value(new Color(20, 20, 20));
    public final ColorSetting friendColor = new ColorSetting("Friend color").value(new Color(132, 229, 121)).setVisible(() -> this.targets.isEnabled("Players") || this.targets.isEnabled("Self"));
    public final BooleanSetting zako64Spoof = new BooleanSetting("Zako64 Spoof").value(false);
    public final BooleanSetting box3d = new BooleanSetting("Box 3D").value(false);
    public final SliderSetting boxAlpha = new SliderSetting("Box Alpha").value(Float.valueOf(0.25f)).range(0.0f, 1.0f).step(0.05f).setVisible(this.box3d::getValue);
    public final TargetManager.EntityFilter entityFilter = new TargetManager.EntityFilter(this.targets.getList());
    private final NameTagsRender nameTagsRender = new NameTagsRender(this);

    public NameTagsModule() {
        this.addSettings(this.targets, this.rectMode, this.hpMode, this.textMode, this.scale, this.information, this.options, this.glassy, this.textColor, this.color, this.friendColor, this.zako64Spoof, this.box3d, this.boxAlpha);
    }

    @Override
    public void onEvent() {
        EventListener render2DEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(1, event -> {
            this.entityFilter.targetSettings = this.targets.getList();
            this.entityFilter.needFriends = true;
            this.nameTagsRender.onRender((Render2DEvent.Render2DEventData)event);
        }));
        this.addEvents(render2DEvent);
    }

    @Generated
    public static NameTagsModule getInstance() {
        return instance;
    }
}

