package sweetie.leonware.api.system.client;

import lombok.Generated;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.task.TaskProcessor;
import sweetie.leonware.client.features.modules.player.TimerModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/client/TimerManager.class */
public class TimerManager {
    private static final TimerManager instance = new TimerManager();
    private final TaskProcessor<Float> taskProcessor = new TaskProcessor<>();

    @Generated
    public static TimerManager getInstance() {
        return instance;
    }

    @Generated
    public TaskProcessor<Float> getTaskProcessor() {
        return this.taskProcessor;
    }

    public float getTimerSpeed() {
        if (this.taskProcessor.fetchActiveTaskValue() != null) {
            return this.taskProcessor.fetchActiveTaskValue().floatValue();
        }
        return 1.0f;
    }

    public void addTimer(float timer, TaskPriority taskPriority, Module provider, int ticks) {
        this.taskProcessor.addTask(new TaskProcessor.Task<>(ticks, taskPriority.getPriority(), provider, Float.valueOf(timer)));
    }

    public static void setTimer(float timer) {
        getInstance().addTimer(timer, TaskPriority.NORMAL, TimerModule.getInstance(), 1);
    }
}
