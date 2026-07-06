package sweetie.leonware.client.features.modules.render.motionblur;

import net.minecraft.class_310;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/motionblur/MonitorInfoProvider.class */
public class MonitorInfoProvider {
    private static long lastMonitorHandle = 0;
    private static int lastRefreshRate = 60;
    private static long lastCheckTime = 0;
    private static final long CHECK_INTERVAL_NS = 1000000000;

    public static void updateDisplayInfo() {
        long now = System.nanoTime();
        if (now - lastCheckTime < CHECK_INTERVAL_NS) {
            return;
        }
        lastCheckTime = now;
        class_310 client = class_310.method_1551();
        if (client == null || client.method_22683() == null) {
            return;
        }
        long window = client.method_22683().method_4490();
        long monitor = GLFW.glfwGetWindowMonitor(window);
        if (monitor == 0) {
            monitor = getMonitorFromWindowPosition(window, client.method_22683().method_4480(), client.method_22683().method_4507());
        }
        if (monitor != lastMonitorHandle) {
            lastRefreshRate = detectRefreshRateFromMonitor(monitor);
            lastMonitorHandle = monitor;
        }
    }

    public static int getRefreshRate() {
        return lastRefreshRate;
    }

    private static long getMonitorFromWindowPosition(long window, int windowWidth, int windowHeight) {
        int[] winX = new int[1];
        int[] winY = new int[1];
        GLFW.glfwGetWindowPos(window, winX, winY);
        int windowCenterX = winX[0] + (windowWidth / 2);
        int windowCenterY = winY[0] + (windowHeight / 2);
        long monitorResult = GLFW.glfwGetPrimaryMonitor();
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        if (monitors != null) {
            int i = 0;
            while (true) {
                if (i >= monitors.limit()) {
                    break;
                }
                long m = monitors.get(i);
                int[] mx = new int[1];
                int[] my = new int[1];
                GLFW.glfwGetMonitorPos(m, mx, my);
                GLFWVidMode mode = GLFW.glfwGetVideoMode(m);
                if (mode != null) {
                    int mw = mode.width();
                    int mh = mode.height();
                    if (windowCenterX >= mx[0] && windowCenterX < mx[0] + mw && windowCenterY >= my[0] && windowCenterY < my[0] + mh) {
                        monitorResult = m;
                        break;
                    }
                }
                i++;
            }
        }
        return monitorResult;
    }

    private static int detectRefreshRateFromMonitor(long monitor) {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
        if (vidMode != null) {
            return vidMode.refreshRate();
        }
        return 60;
    }
}
