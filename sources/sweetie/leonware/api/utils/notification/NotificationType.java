package sweetie.leonware.api.utils.notification;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/notification/NotificationType.class */
public enum NotificationType {
    SUCCESS(new Color(100, 255, 100)),
    INFO(new Color(225, 225, 255)),
    ERROR(new Color(255, 100, 100)),
    WARNING(new Color(255, 211, 53));

    public final Color color;

    NotificationType(Color color) {
        this.color = color;
    }
}
