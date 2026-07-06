// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.motionblur;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import net.minecraft.class_310;

public class MonitorInfoProvider
{
    private static long lastMonitorHandle;
    private static int lastRefreshRate;
    private static long lastCheckTime;
    private static final long CHECK_INTERVAL_NS = 1000000000L;
    
    public static void updateDisplayInfo() {
        final long now = System.nanoTime();
        if (now - MonitorInfoProvider.lastCheckTime < 1000000000L) {
            return;
        }
        MonitorInfoProvider.lastCheckTime = now;
        final class_310 client = class_310.method_1551();
        if (client == null || client.method_22683() == null) {
            return;
        }
        final long window = client.method_22683().method_4490();
        long monitor = GLFW.glfwGetWindowMonitor(window);
        if (monitor == 0L) {
            monitor = getMonitorFromWindowPosition(window, client.method_22683().method_4480(), client.method_22683().method_4507());
        }
        if (monitor != MonitorInfoProvider.lastMonitorHandle) {
            MonitorInfoProvider.lastRefreshRate = detectRefreshRateFromMonitor(monitor);
            MonitorInfoProvider.lastMonitorHandle = monitor;
        }
    }
    
    public static int getRefreshRate() {
        return MonitorInfoProvider.lastRefreshRate;
    }
    
    private static long getMonitorFromWindowPosition(final long window, final int windowWidth, final int windowHeight) {
        final int[] winX = { 0 };
        final int[] winY = { 0 };
        GLFW.glfwGetWindowPos(window, winX, winY);
        final int windowCenterX = winX[0] + windowWidth / 2;
        final int windowCenterY = winY[0] + windowHeight / 2;
        long monitorResult = GLFW.glfwGetPrimaryMonitor();
        final PointerBuffer monitors = GLFW.glfwGetMonitors();
        if (monitors != null) {
            for (int i = 0; i < monitors.limit(); ++i) {
                final long m = monitors.get(i);
                final int[] mx = { 0 };
                final int[] my = { 0 };
                GLFW.glfwGetMonitorPos(m, mx, my);
                final GLFWVidMode mode = GLFW.glfwGetVideoMode(m);
                if (mode != null) {
                    final int mw = mode.width();
                    final int mh = mode.height();
                    if (windowCenterX >= mx[0] && windowCenterX < mx[0] + mw && windowCenterY >= my[0] && windowCenterY < my[0] + mh) {
                        monitorResult = m;
                        break;
                    }
                }
            }
        }
        return monitorResult;
    }
    
    private static int detectRefreshRateFromMonitor(final long monitor) {
        final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
        return (vidMode != null) ? vidMode.refreshRate() : 60;
    }
    
    static {
        MonitorInfoProvider.lastMonitorHandle = 0L;
        MonitorInfoProvider.lastRefreshRate = 60;
        MonitorInfoProvider.lastCheckTime = 0L;
    }
}
