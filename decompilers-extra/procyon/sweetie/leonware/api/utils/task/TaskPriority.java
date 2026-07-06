// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.task;

import lombok.Generated;

public enum TaskPriority
{
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
    private TaskPriority(final int priority) {
        this.priority = priority;
    }
}
