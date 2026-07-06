// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost.common;

import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;

public class NativeLib
{
    private static final Logger logger;
    private static final String LOCK_EXT = ".lck";
    
    public static synchronized void smartLoad(@NotNull final String libName) throws IOException {
        cleanup(libName);
        try {
            loadNativeLibraryFromJar(libName);
        }
        catch (final IOException ioe) {
            NativeLib.logger.error("failed to load native library from both default location and JAR");
            throw ioe;
        }
    }
    
    @NotNull
    private static String[] getCurrentMachineResourcesDirs() {
        String osArch = System.getProperty("os.arch").toLowerCase();
        if (osArch.equals("amd64")) {
            osArch = "x86_64";
        }
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            osName = "darwin";
            if (osArch.equals("aarch64")) {
                osArch = "arm64";
            }
            return new String[] { osName + "-" + osArch, osName + "-universal2" };
        }
        if (osName.contains("win")) {
            osName = "win32";
        }
        return new String[] { osName + "-" + osArch };
    }
    
    private static void loadNativeLibraryFromJar(@NotNull final String libName) throws IOException {
        for (final String machineResourcesDir : getCurrentMachineResourcesDirs()) {
            final String pathWithinJar = "/" + machineResourcesDir + "/lib/" + System.mapLibraryName(libName);
            if (NativeLib.class.getResource(pathWithinJar) != null) {
                final String tempLibPath = createTemporaryFileFromJar(pathWithinJar);
                System.load(tempLibPath);
                return;
            }
        }
        throw new IOException("Native library '" + libName + "' not found");
    }
    
    private static void copyFileFromJar(@NotNull final String pathWithinJar, @NotNull final String pathOnDisk) throws IOException {
        final byte[] copyBuffer = new byte[4096];
        try (final OutputStream out = new BufferedOutputStream(new FileOutputStream(pathOnDisk));
             final InputStream in = NativeLib.class.getResourceAsStream(pathWithinJar)) {
            if (in == null) {
                throw new FileNotFoundException("File " + pathWithinJar + " was not found inside JAR.");
            }
            int bytesRead;
            while ((bytesRead = in.read(copyBuffer)) != -1) {
                out.write(copyBuffer, 0, bytesRead);
            }
        }
    }
    
    @NotNull
    private static String createTemporaryFileFromJar(@NotNull final String pathWithinJar) throws IOException, IllegalArgumentException {
        if (!pathWithinJar.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute (start with '/')");
        }
        if (pathWithinJar.endsWith("/")) {
            throw new IllegalArgumentException("Must be a path to file not directory (ends with '/')");
        }
        String[] parts = pathWithinJar.split("/");
        final String filename = parts[parts.length - 1];
        parts = filename.split("\\.", 2);
        final String prefix = parts[0] + "-";
        final String suffix = (parts.length > 1) ? ("." + parts[parts.length - 1]) : null;
        final File libOnDisk = File.createTempFile(prefix, suffix);
        libOnDisk.deleteOnExit();
        final File libOnDiskLck = new File(libOnDisk.getAbsolutePath() + ".lck");
        libOnDiskLck.createNewFile();
        libOnDiskLck.deleteOnExit();
        copyFileFromJar(pathWithinJar, libOnDisk.getPath());
        return libOnDisk.getAbsolutePath();
    }
    
    private static void cleanup(@NotNull final String libName) {
        final String searchPattern = libName + "-";
        try (final Stream<Path> dirList = Files.list(new File(System.getProperty("java.io.tmpdir")).toPath())) {
            dirList.filter(path -> !path.getFileName().toString().endsWith(".lck") && path.getFileName().toString().startsWith(searchPattern)).forEach(nativeLib -> {
                final Path lckFile = Paths.get(nativeLib + ".lck", new String[0]);
                if (Files.notExists(lckFile, new LinkOption[0])) {
                    try {
                        Files.delete(nativeLib);
                    }
                    catch (final Exception e2) {
                        NativeLib.logger.error("Failed to delete old native lib", e2);
                    }
                }
                return;
            });
        }
        catch (final IOException e) {
            NativeLib.logger.error("Failed to open directory", e);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(NativeLib.class);
    }
}
