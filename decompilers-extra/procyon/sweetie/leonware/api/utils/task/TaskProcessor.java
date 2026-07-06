// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.task;

import sweetie.leonware.api.module.Module;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskProcessor<T>
{
    private int tickCounter;
    private final PriorityQueue<Task<T>> activeTasks;
    
    public TaskProcessor() {
        this.tickCounter = 0;
        this.activeTasks = new PriorityQueue<Task<T>>(Comparator.comparingInt(task -> task.priority).reversed());
    }
    
    public void tick(final int deltaTime) {
        this.tickCounter += deltaTime;
        while (!this.activeTasks.isEmpty() && this.activeTasks.peek().expiresIn <= this.tickCounter) {
            final Task<T> task = this.activeTasks.poll();
            task.run();
        }
    }
    
    public void addTask(final Task<T> task) {
        this.activeTasks.removeIf(t -> t.provider == task.provider);
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
        return (T)this.activeTasks.peek().value;
    }
    
    public static class Task<T>
    {
        public int expiresIn;
        public final int priority;
        public final Module provider;
        public final T value;
        
        public Task(final int expiresIn, final int priority, final Module provider, final T value) {
            this.expiresIn = expiresIn;
            this.priority = priority;
            this.provider = provider;
            this.value = value;
        }
        
        public void run() {
        }
        
        @Override
        public String toString() {
            return "Task(expiresIn=" + this.expiresIn + ", priority=" + this.priority + ", provider=" + String.valueOf(this.provider) + ", value=" + String.valueOf(this.value);
        }
    }
}
