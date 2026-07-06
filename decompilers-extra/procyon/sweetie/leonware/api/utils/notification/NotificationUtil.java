// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.notification;

import java.util.HashMap;
import java.util.ArrayList;
import sweetie.leonware.client.ui.widget.Widget;
import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import java.util.Map;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class NotificationUtil implements QuickImports
{
    private static final List<Notif> notifs;
    private static final Map<String, Boolean> modStates;
    
    public static void update() {
        NotificationUtil.notifs.removeIf(n -> n.alpha <= 0.0f);
        NotificationUtil.notifs.forEach(Notif::update);
        checkMods();
    }
    
    private static void checkMods() {
        final NotifWidget widget = WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439")) {
            return;
        }
        for (Module m : ModuleManager.getInstance().getModules()) {
            final String name = m.getName();
            final boolean was = NotificationUtil.modStates.getOrDefault(name, m.isEnabled());
            final boolean now = m.isEnabled();
            if (was != now) {
                add(name + (now ? " §a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " §c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
                NotificationUtil.modStates.put(name, now);
            }
        }
    }
    
    public static void add(final String title, final String content, final NotificationType type) {
        NotificationUtil.notifs.add(new Notif(title, content, type));
    }
    
    public static void add(final String text) {
        NotificationUtil.notifs.add(new Notif(text, "", NotificationType.INFO));
    }
    
    @Generated
    private NotificationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    @Generated
    public static List<Notif> getNotifs() {
        return NotificationUtil.notifs;
    }
    
    static {
        notifs = new ArrayList<Notif>();
        modStates = new HashMap<String, Boolean>();
    }
    
    public static class Notif
    {
        public final String text;
        public final String title;
        public final String content;
        public final NotificationType type;
        private final long time;
        public float alpha;
        public float scale;
        
        public Notif(final String text) {
            this(text, "", NotificationType.INFO);
        }
        
        public Notif(final String title, final String content, final NotificationType type) {
            this.alpha = 0.0f;
            this.scale = 0.8f;
            this.text = title;
            this.title = title;
            this.content = content;
            this.type = type;
            this.time = System.currentTimeMillis();
        }
        
        public void update() {
            final long elapsed = System.currentTimeMillis() - this.time;
            if (elapsed < 300L) {
                final float p = elapsed / 300.0f;
                this.alpha = p;
                this.scale = 0.8f + p * 0.2f;
            }
            else if (elapsed < 3700L) {
                this.alpha = 1.0f;
                this.scale = 1.0f;
            }
            else if (elapsed < 4000L) {
                final float p = (elapsed - 3700L) / 300.0f;
                this.alpha = 1.0f - p;
                this.scale = 1.0f - p * 0.2f;
            }
            else {
                this.alpha = 0.0f;
            }
        }
    }
}
