/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.task;

import lombok.Generated;

public enum TaskPriority {
    CRITICAL(5),
    REQUIRED(4),
    HIGH(3),
    MEDIUM_HIGH(2),
    MEDIUM(1),
    NORMAL(0),
    LOW(-1),
    LOWEST(-2);

    private final int priority;

    @Generated
    public int getPriority() {
        return this.priority;
    }

    @Generated
    private TaskPriority(int priority) {
        this.priority = priority;
    }
}

