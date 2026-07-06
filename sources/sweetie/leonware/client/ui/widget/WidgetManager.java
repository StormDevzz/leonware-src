package sweetie.leonware.client.ui.widget;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.ui.widget.overlay.AresMineWidget;
import sweetie.leonware.client.ui.widget.overlay.ArmorWidget;
import sweetie.leonware.client.ui.widget.overlay.ArrayListWidget;
import sweetie.leonware.client.ui.widget.overlay.BPSWidget;
import sweetie.leonware.client.ui.widget.overlay.BossBarWidget;
import sweetie.leonware.client.ui.widget.overlay.CooldownsWidget;
import sweetie.leonware.client.ui.widget.overlay.FPSWidget;
import sweetie.leonware.client.ui.widget.overlay.KeybindsWidget;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import sweetie.leonware.client.ui.widget.overlay.PlayerModelWidget;
import sweetie.leonware.client.ui.widget.overlay.PotionsWidget;
import sweetie.leonware.client.ui.widget.overlay.ScoreboardWidget;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;
import sweetie.leonware.client.ui.widget.overlay.TargetInfoWidget;
import sweetie.leonware.client.ui.widget.overlay.WatermarkWidget;
import sweetie.leonware.client.ui.widget.overlay.XYZWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/WidgetManager.class */
public class WidgetManager {
    private static final WidgetManager instance = new WidgetManager();
    private final List<Widget> widgets = new ArrayList();

    @Generated
    public static WidgetManager getInstance() {
        return instance;
    }

    @Generated
    public List<Widget> getWidgets() {
        return this.widgets;
    }

    public void load() {
        register(new WatermarkWidget(), new KeybindsWidget(), new PotionsWidget(), new StaffsWidget(), new CooldownsWidget(), new BossBarWidget(), new ArmorWidget(), new TargetInfoWidget(), new FPSWidget(), new NotifWidget(), new ScoreboardWidget(), new ArrayListWidget(), new AresMineWidget(), new PlayerModelWidget(), new BPSWidget(), new XYZWidget());
        InterfaceModule.getInstance().init();
        Render2DEvent.getInstance().subscribe(new Listener(event -> {
            InterfaceModule module = InterfaceModule.getInstance();
            if (module.isEnabled()) {
                List<Widget> list = this.widgets;
                int n = list.size();
                for (int i = 0; i < n; i++) {
                    Widget widget = list.get(i);
                    if (widget.isEnabled()) {
                        widget.render(event);
                    }
                }
            }
        }));
    }

    public void register(Widget... widgets) {
        for (Widget widget : widgets) {
            this.widgets.add(widget);
        }
    }
}
