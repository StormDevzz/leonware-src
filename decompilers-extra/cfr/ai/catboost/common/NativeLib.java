/*
 * Decompiled with CFR 0.152.
 */
package ai.catboost.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeLib {
    private static final Logger logger = LoggerFactory.getLogger(NativeLib.class);
    private static final String LOCK_EXT = ".lck";

    public static synchronized void smartLoad(@NotNull String libName) throws IOException {
        NativeLib.cleanup(libName);
        try {
            NativeLib.loadNativeLibraryFromJar(libName);
        }
        catch (IOException ioe) {
            logger.error("failed to load native library from both default location and JAR");
            throw ioe;
        }
    }

    @NotNull
    private static String[] getCurrentMachineResourcesDirs() {
        String osName;
        String osArch = System.getProperty("os.arch").toLowerCase();
        if (osArch.equals("amd64")) {
            osArch = "x86_64";
        }
        if ((osName = System.getProperty("os.name").toLowerCase()).contains("mac")) {
            osName = "darwin";
            if (osArch.equals("aarch64")) {
                osArch = "arm64";
            }
            return new String[]{osName + "-" + osArch, osName + "-universal2"};
        }
        if (osName.contains("win")) {
            osName = "win32";
        }
        return new String[]{osName + "-" + osArch};
    }

    private static void loadNativeLibraryFromJar(@NotNull String libName) throws IOException {
        for (String machineResourcesDir : NativeLib.getCurrentMachineResourcesDirs()) {
            String pathWithinJar = "/" + machineResourcesDir + "/lib/" + System.mapLibraryName(libName);
            if (NativeLib.class.getResource(pathWithinJar) == null) continue;
            String tempLibPath = NativeLib.createTemporaryFileFromJar(pathWithinJar);
            System.load(tempLibPath);
            return;
        }
        throw new IOException("Native library '" + libName + "' not found");
    }

    private static void copyFileFromJar(@NotNull String pathWithinJar, @NotNull String pathOnDisk) throws IOException {
        byte[] copyBuffer = new byte[4096];
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(pathOnDisk));
             InputStream in = NativeLib.class.getResourceAsStream(pathWithinJar);){
            int bytesRead;
            if (in == null) {
                throw new FileNotFoundException("File " + pathWithinJar + " was not found inside JAR.");
            }
            while ((bytesRead = in.read(copyBuffer)) != -1) {
                ((OutputStream)out).write(copyBuffer, 0, bytesRead);
            }
        }
    }

    @NotNull
    private static String createTemporaryFileFromJar(@NotNull String pathWithinJar) throws IOException, IllegalArgumentException {
        if (!pathWithinJar.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute (start with '/')");
        }
        if (pathWithinJar.endsWith("/")) {
            throw new IllegalArgumentException("Must be a path to file not directory (ends with '/')");
        }
        String[] parts = pathWithinJar.split("/");
        String filename = parts[parts.length - 1];
        parts = filename.split("\\.", 2);
        String prefix = parts[0] + "-";
        String suffix = parts.length > 1 ? "." + parts[parts.length - 1] : null;
        File libOnDisk = File.createTempFile(prefix, suffix);
        libOnDisk.deleteOnExit();
        File libOnDiskLck = new File(libOnDisk.getAbsolutePath() + LOCK_EXT);
        libOnDiskLck.createNewFile();
        libOnDiskLck.deleteOnExit();
        NativeLib.copyFileFromJar(pathWithinJar, libOnDisk.getPath());
        return libOnDisk.getAbsolutePath();
    }

    private static void cleanup(@NotNull String libName) {
        String searchPattern = libName + "-";
        try (Stream<Path> dirList = Files.list(new File(System.getProperty("java.io.tmpdir")).toPath());){
            dirList.filter(path -> !path.getFileName().toString().endsWith(LOCK_EXT) && path.getFileName().toString().startsWith(searchPattern)).forEach(nativeLib -> {
                Path lckFile = Paths.get(nativeLib + LOCK_EXT, new String[0]);
                if (Files.notExists(lckFile, new LinkOption[0])) {
                    try {
                        Files.delete(nativeLib);
                    }
                    catch (Exception e) {
                        logger.error("Failed to delete old native lib", e);
                    }
                }
            });
        }
        catch (IOException e) {
            logger.error("Failed to open directory", e);
        }
    }
}

