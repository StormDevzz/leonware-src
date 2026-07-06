package sweetie.leonware.api.utils.task;

import java.util.Comparator;
import java.util.PriorityQueue;
import sweetie.leonware.api.module.Module;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/task/TaskProcessor.class */
public class TaskProcessor<T> {
    private int tickCounter = 0;
    private final PriorityQueue<Task<T>> activeTasks = new PriorityQueue<>(Comparator.comparingInt(task -> {
        return task.priority;
    }).reversed());

    public void tick(int deltaTime) {
        this.tickCounter += deltaTime;
        while (!this.activeTasks.isEmpty() && this.activeTasks.peek().expiresIn <= this.tickCounter) {
            Task<T> task = this.activeTasks.poll();
            task.run();
        }
    }

    public void addTask(Task<T> task) {
        this.activeTasks.removeIf(t -> {
            return t.provider == task.provider;
        });
        task.expiresIn += this.tickCounter;
        this.activeTasks.add(task);
    }

    public T fetchActiveTaskValue() {
        while (!this.activeTasks.isEmpty() && (this.activeTasks.peek().expiresIn <= this.tickCounter || !this.activeTasks.peek().provider.isEnabled())) {
            this.activeTasks.poll();
        }
        if (this.activeTasks.isEmpty()) {
            return null;
        }
        return this.activeTasks.peek().value;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/task/TaskProcessor$Task.class */
    public static class Task<T> {
        public int expiresIn;
        public final int priority;
        public final Module provider;
        public final T value;

        public Task(int expiresIn, int priority, Module provider, T value) {
            this.expiresIn = expiresIn;
            this.priority = priority;
            this.provider = provider;
            this.value = value;
        }

        public void run() {
        }

        public String toString() {
            return "Task(expiresIn=" + this.expiresIn + ", priority=" + this.priority + ", provider=" + String.valueOf(this.provider) + ", value=" + String.valueOf(this.value) + ")";
        }
    }
}
