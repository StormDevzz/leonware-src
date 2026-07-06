/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashMap;
import java.util.Map;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class KeybindsWidget
extends ContainerWidget {
    public KeybindsWidget() {
        super(3.0f, 120.0f);
    }

    @Override
    public String getName() {
        return "Keybinds";
    }

    @Override
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        HashMap<String, ContainerWidget.ContainerElement.ColoredString> map = new HashMap<String, ContainerWidget.ContainerElement.ColoredString>();
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (!m.isEnabled() || !m.hasBind()) continue;
            map.put(m.getName(), new ContainerWidget.ContainerElement.ColoredString(KeyStorage.getBind(m.getBind())));
        }
        return map;
    }
}

