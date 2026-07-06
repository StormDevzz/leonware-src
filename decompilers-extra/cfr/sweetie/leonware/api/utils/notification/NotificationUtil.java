/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.notification.NotificationType;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;

public final class NotificationUtil
implements QuickImports {
    private static final List<Notif> notifs = new ArrayList<Notif>();
    private static final Map<String, Boolean> modStates = new HashMap<String, Boolean>();

    public static void update() {
        notifs.removeIf(n -> n.alpha <= 0.0f);
        notifs.forEach(Notif::update);
        NotificationUtil.checkMods();
    }

    private static void checkMods() {
        NotifWidget widget = WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439")) {
            return;
        }
        for (Module m : ModuleManager.getInstance().getModules()) {
            boolean now;
            String name = m.getName();
            boolean was = modStates.getOrDefault(name, m.isEnabled());
            if (was == (now = m.isEnabled())) continue;
            NotificationUtil.add(name + (now ? " \u00a7a\u0432\u043a\u043b\u044e\u0447\u0435\u043d" : " \u00a7c\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d"));
            modStates.put(name, now);
        }
    }

    public static void add(String title, String content, NotificationType type) {
        notifs.add(new Notif(title, content, type));
    }

    public static void add(String text) {
        notifs.add(new Notif(text, "", NotificationType.INFO));
    }

    @Generated
    private NotificationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static List<Notif> getNotifs() {
        return notifs;
    }

    public static class Notif {
        public final String text;
        public final String title;
        public final String content;
        public final NotificationType type;
        private final long time;
        public float alpha = 0.0f;
        public float scale = 0.8f;

        public Notif(String text) {
            this(text, "", NotificationType.INFO);
        }

        public Notif(String title, String content, NotificationType type) {
            this.text = title;
            this.title = title;
            this.content = content;
            this.type = type;
            this.time = System.currentTimeMillis();
        }

        public void update() {
            long elapsed = System.currentTimeMillis() - this.time;
            if (elapsed < 300L) {
                float p;
                this.alpha = p = (float)elapsed / 300.0f;
                this.scale = 0.8f + p * 0.2f;
            } else if (elapsed < 3700L) {
                this.alpha = 1.0f;
                this.scale = 1.0f;
            } else if (elapsed < 4000L) {
                float p = (float)(elapsed - 3700L) / 300.0f;
                this.alpha = 1.0f - p;
                this.scale = 1.0f - p * 0.2f;
            } else {
                this.alpha = 0.0f;
            }
        }
    }
}

