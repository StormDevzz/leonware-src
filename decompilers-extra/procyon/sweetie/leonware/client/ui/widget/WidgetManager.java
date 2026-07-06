// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.ui.widget.overlay.XYZWidget;
import sweetie.leonware.client.ui.widget.overlay.BPSWidget;
import sweetie.leonware.client.ui.widget.overlay.PlayerModelWidget;
import sweetie.leonware.client.ui.widget.overlay.AresMineWidget;
import sweetie.leonware.client.ui.widget.overlay.ArrayListWidget;
import sweetie.leonware.client.ui.widget.overlay.ScoreboardWidget;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import sweetie.leonware.client.ui.widget.overlay.FPSWidget;
import sweetie.leonware.client.ui.widget.overlay.TargetInfoWidget;
import sweetie.leonware.client.ui.widget.overlay.ArmorWidget;
import sweetie.leonware.client.ui.widget.overlay.BossBarWidget;
import sweetie.leonware.client.ui.widget.overlay.CooldownsWidget;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.overlay.PotionsWidget;
import sweetie.leonware.client.ui.widget.overlay.KeybindsWidget;
import sweetie.leonware.client.ui.widget.overlay.WatermarkWidget;
import java.util.ArrayList;
import java.util.List;

public class WidgetManager
{
    private static final WidgetManager instance;
    private final List<Widget> widgets;
    
    public WidgetManager() {
        this.widgets = new ArrayList<Widget>();
    }
    
    public void load() {
        this.register(new WatermarkWidget(), new KeybindsWidget(), new PotionsWidget(), new StaffsWidget(), new CooldownsWidget(), new BossBarWidget(), new ArmorWidget(), new TargetInfoWidget(), new FPSWidget(), new NotifWidget(), new ScoreboardWidget(), new ArrayListWidget(), new AresMineWidget(), new PlayerModelWidget(), new BPSWidget(), new XYZWidget());
        InterfaceModule.getInstance().init();
        Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            final InterfaceModule module = InterfaceModule.getInstance();
            if (!(!module.isEnabled())) {
                final List<Widget> list = this.widgets;
                for (int i = 0, n = list.size(); i < n; ++i) {
                    final Widget widget = (Widget)list.get(i);
                    if (widget.isEnabled()) {
                        widget.render(event);
                    }
                }
            }
        }));
    }
    
    public void register(final Widget... widgets) {
        for (final Widget widget : widgets) {
            this.widgets.add(widget);
        }
    }
    
    @Generated
    public List<Widget> getWidgets() {
        return this.widgets;
    }
    
    @Generated
    public static WidgetManager getInstance() {
        return WidgetManager.instance;
    }
    
    static {
        instance = new WidgetManager();
    }
}
