// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.notification;

import java.awt.Color;

public enum NotificationType
{
    SUCCESS(new Color(100, 255, 100)), 
    INFO(new Color(225, 225, 255)), 
    ERROR(new Color(255, 100, 100)), 
    WARNING(new Color(255, 211, 53));
    
    public final Color color;
    
    private NotificationType(final Color color) {
        this.color = color;
    }
}
