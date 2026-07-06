// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.client;

import lombok.Generated;
import sweetie.leonware.client.features.modules.player.TimerModule;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.task.TaskProcessor;

public class TimerManager
{
    private static final TimerManager instance;
    private final TaskProcessor<Float> taskProcessor;
    
    public TimerManager() {
        this.taskProcessor = new TaskProcessor<Float>();
    }
    
    public float getTimerSpeed() {
        return (this.taskProcessor.fetchActiveTaskValue() != null) ? this.taskProcessor.fetchActiveTaskValue() : 1.0f;
    }
    
    public void addTimer(final float timer, final TaskPriority taskPriority, final Module provider, final int ticks) {
        this.taskProcessor.addTask(new TaskProcessor.Task<Float>(ticks, taskPriority.getPriority(), provider, timer));
    }
    
    public static void setTimer(final float timer) {
        getInstance().addTimer(timer, TaskPriority.NORMAL, TimerModule.getInstance(), 1);
    }
    
    @Generated
    public TaskProcessor<Float> getTaskProcessor() {
        return this.taskProcessor;
    }
    
    @Generated
    public static TimerManager getInstance() {
        return TimerManager.instance;
    }
    
    static {
        instance = new TimerManager();
    }
}
