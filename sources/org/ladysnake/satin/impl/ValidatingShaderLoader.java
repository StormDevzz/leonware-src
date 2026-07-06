package org.ladysnake.satin.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import org.ladysnake.satin.Satin;
import org.ladysnake.satin.api.util.ShaderLinkException;
import org.ladysnake.satin.api.util.ShaderLoader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/ValidatingShaderLoader.class */
public final class ValidatingShaderLoader implements ShaderLoader {
    public static final ShaderLoader INSTANCE = new ValidatingShaderLoader();

    @Override // org.ladysnake.satin.api.util.ShaderLoader
    public int loadShader(class_3300 resourceManager, @Nullable class_2960 vertexLocation, @Nullable class_2960 fragmentLocation) throws IOException {
        int programId = GlStateManager.glCreateProgram();
        int vertexShaderId = 0;
        int fragmentShaderId = 0;
        if (vertexLocation != null) {
            vertexShaderId = GlStateManager.glCreateShader(35633);
            ARBShaderObjects.glShaderSourceARB(vertexShaderId, fromFile(resourceManager, vertexLocation));
            ARBShaderObjects.glCompileShaderARB(vertexShaderId);
            ARBShaderObjects.glAttachObjectARB(programId, vertexShaderId);
            String log = GL20.glGetShaderInfoLog(vertexShaderId, 1024);
            if (!log.isEmpty()) {
                Satin.LOGGER.error("Could not compile vertex shader {}: {}", vertexLocation, log);
            }
        }
        if (fragmentLocation != null) {
            fragmentShaderId = GlStateManager.glCreateShader(35632);
            ARBShaderObjects.glShaderSourceARB(fragmentShaderId, fromFile(resourceManager, fragmentLocation));
            ARBShaderObjects.glCompileShaderARB(fragmentShaderId);
            ARBShaderObjects.glAttachObjectARB(programId, fragmentShaderId);
            String log2 = GL20.glGetShaderInfoLog(fragmentShaderId, 1024);
            if (!log2.isEmpty()) {
                Satin.LOGGER.error("Could not compile fragment shader {}: {}", fragmentLocation, log2);
            }
        }
        GlStateManager.glLinkProgram(programId);
        if (GL20.glGetProgrami(programId, 35714) == 0) {
            throw new ShaderLinkException("Error linking Shader code: " + GL20.glGetProgramInfoLog(programId, 1024));
        }
        if (vertexShaderId != 0) {
            GL20.glDetachShader(programId, vertexShaderId);
            GL20.glDeleteShader(vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL20.glDetachShader(programId, fragmentShaderId);
            GL20.glDeleteShader(fragmentShaderId);
        }
        GL20.glValidateProgram(programId);
        if (GL20.glGetProgrami(programId, 35715) == 0) {
            Satin.LOGGER.warn("Warning validating Shader code: {}", GL20.glGetProgramInfoLog(programId, 1024));
        }
        return programId;
    }

    private String fromFile(class_3300 resourceManager, class_2960 fileLocation) throws IOException {
        StringBuilder source = new StringBuilder();
        InputStream in = resourceManager.getResourceOrThrow(fileLocation).method_14482();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    source.append(line).append('\n');
                } finally {
                }
            }
            reader.close();
            if (in != null) {
                in.close();
            }
            return source.toString();
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
