/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.client;

import lombok.Generated;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.task.TaskProcessor;
import sweetie.leonware.client.features.modules.player.TimerModule;

public class TimerManager {
    private static final TimerManager instance = new TimerManager();
    private final TaskProcessor<Float> taskProcessor = new TaskProcessor();

    public float getTimerSpeed() {
        return this.taskProcessor.fetchActiveTaskValue() != null ? this.taskProcessor.fetchActiveTaskValue().floatValue() : 1.0f;
    }

    public void addTimer(float timer, TaskPriority taskPriority, Module provider, int ticks) {
        this.taskProcessor.addTask(new TaskProcessor.Task<Float>(ticks, taskPriority.getPriority(), provider, Float.valueOf(timer)));
    }

    public static void setTimer(float timer) {
        TimerManager.getInstance().addTimer(timer, TaskPriority.NORMAL, TimerModule.getInstance(), 1);
    }

    @Generated
    public TaskProcessor<Float> getTaskProcessor() {
        return this.taskProcessor;
    }

    @Generated
    public static TimerManager getInstance() {
        return instance;
    }
}

