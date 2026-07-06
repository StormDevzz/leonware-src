package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashMap;
import java.util.Map;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.client.ui.widget.ContainerWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/KeybindsWidget.class */
public class KeybindsWidget extends ContainerWidget {
    public KeybindsWidget() {
        super(3.0f, 120.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Keybinds";
    }

    @Override // sweetie.leonware.client.ui.widget.ContainerWidget
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        Map<String, ContainerWidget.ContainerElement.ColoredString> map = new HashMap<>();
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (m.isEnabled() && m.hasBind()) {
                map.put(m.getName(), new ContainerWidget.ContainerElement.ColoredString(KeyStorage.getBind(m.getBind())));
            }
        }
        return map;
    }
}
