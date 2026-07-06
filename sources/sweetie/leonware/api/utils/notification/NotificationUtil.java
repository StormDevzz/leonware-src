package sweetie.leonware.api.utils.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/notification/NotificationUtil.class */
public final class NotificationUtil implements QuickImports {
    private static final List<Notif> notifs = new ArrayList();
    private static final Map<String, Boolean> modStates = new HashMap();

    @Generated
    private NotificationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static List<Notif> getNotifs() {
        return notifs;
    }

    public static void update() {
        notifs.removeIf(n -> {
            return n.alpha <= 0.0f;
        });
        notifs.forEach((v0) -> {
            v0.update();
        });
        checkMods();
    }

    private static void checkMods() {
        NotifWidget widget = (NotifWidget) WidgetManager.getInstance().getWidgets().stream().filter(w -> {
            return w instanceof NotifWidget;
        }).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("Состояние модулей")) {
            return;
        }
        for (Module m : ModuleManager.getInstance().getModules()) {
            String name = m.getName();
            boolean was = modStates.getOrDefault(name, Boolean.valueOf(m.isEnabled())).booleanValue();
            boolean now = m.isEnabled();
            if (was != now) {
                add(name + (now ? " §aвключен" : " §cвыключен"));
                modStates.put(name, Boolean.valueOf(now));
            }
        }
    }

    public static void add(String title, String content, NotificationType type) {
        notifs.add(new Notif(title, content, type));
    }

    public static void add(String text) {
        notifs.add(new Notif(text, "", NotificationType.INFO));
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/notification/NotificationUtil$Notif.class */
    public static class Notif {
        public final String text;
        public final String title;
        public final String content;
        public final NotificationType type;
        private final long time;
        public float alpha;
        public float scale;

        public Notif(String text) {
            this(text, "", NotificationType.INFO);
        }

        public Notif(String title, String content, NotificationType type) {
            this.alpha = 0.0f;
            this.scale = 0.8f;
            this.text = title;
            this.title = title;
            this.content = content;
            this.type = type;
            this.time = System.currentTimeMillis();
        }

        public void update() {
            long elapsed = System.currentTimeMillis() - this.time;
            if (elapsed < 300) {
                float p = elapsed / 300.0f;
                this.alpha = p;
                this.scale = 0.8f + (p * 0.2f);
            } else if (elapsed < 3700) {
                this.alpha = 1.0f;
                this.scale = 1.0f;
            } else {
                if (elapsed < 4000) {
                    float p2 = (elapsed - 3700) / 300.0f;
                    this.alpha = 1.0f - p2;
                    this.scale = 1.0f - (p2 * 0.2f);
                    return;
                }
                this.alpha = 0.0f;
            }
        }
    }
}
