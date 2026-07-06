// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import java.util.Iterator;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import java.util.HashMap;
import java.util.Map;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class KeybindsWidget extends ContainerWidget
{
    public KeybindsWidget() {
        super(3.0f, 120.0f);
    }
    
    @Override
    public String getName() {
        return "Keybinds";
    }
    
    @Override
    protected Map<String, ContainerElement.ColoredString> getCurrentData() {
        final Map<String, ContainerElement.ColoredString> map = new HashMap<String, ContainerElement.ColoredString>();
        for (final Module m : ModuleManager.getInstance().getModules()) {
            if (m.isEnabled() && m.hasBind()) {
                map.put(m.getName(), new ContainerElement.ColoredString(KeyStorage.getBind(m.getBind())));
            }
        }
        return map;
    }
}
