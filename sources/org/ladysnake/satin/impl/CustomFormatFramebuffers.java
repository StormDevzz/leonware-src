package org.ladysnake.satin.impl;

import net.minecraft.class_276;
import net.minecraft.class_6367;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/CustomFormatFramebuffers.class */
public class CustomFormatFramebuffers {
    public static final String FORMAT_KEY = "satin:format";
    private static final ThreadLocal<TextureFormat> FORMAT = new ThreadLocal<>();

    @API(status = API.Status.EXPERIMENTAL)
    public static class_276 create(int width, int height, boolean useDepth, TextureFormat format) {
        try {
            FORMAT.set(format);
            class_6367 class_6367Var = new class_6367(width, height, useDepth);
            FORMAT.remove();
            return class_6367Var;
        } catch (Throwable th) {
            FORMAT.remove();
            throw th;
        }
    }

    public static void prepareCustomFormat(String formatString) {
        FORMAT.set(TextureFormat.decode(formatString));
    }

    @Nullable
    public static TextureFormat getCustomFormat() {
        return FORMAT.get();
    }

    public static void clearCustomFormat() {
        FORMAT.remove();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/CustomFormatFramebuffers$TextureFormat.class */
    public enum TextureFormat {
        RGBA8(32856),
        RGBA16(32859),
        RGBA16F(34842),
        RGBA32F(34836);

        public final int value;

        public static TextureFormat decode(String formatString) {
            switch (formatString) {
                case "RGBA8":
                    return RGBA8;
                case "RGBA16":
                    return RGBA16;
                case "RGBA16F":
                    return RGBA16F;
                case "RGBA32F":
                    return RGBA32F;
                default:
                    throw new IllegalArgumentException("Unsupported texture format " + formatString);
            }
        }

        TextureFormat(int value) {
            this.value = value;
        }
    }
}
