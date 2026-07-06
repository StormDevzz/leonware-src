package sweetie.leonware.api.utils.task;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/task/TaskPriority.class */
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
    TaskPriority(final int priority) {
        this.priority = priority;
    }

    @Generated
    public int getPriority() {
        return this.priority;
    }
}
