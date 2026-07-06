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

/* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/common/NativeLib.class */
public class NativeLib {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) NativeLib.class);
    private static final String LOCK_EXT = ".lck";

    public static synchronized void smartLoad(@NotNull String libName) throws IOException {
        cleanup(libName);
        try {
            loadNativeLibraryFromJar(libName);
        } catch (IOException ioe) {
            logger.error("failed to load native library from both default location and JAR");
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
            if (osArch.equals("aarch64")) {
                osArch = "arm64";
            }
            return new String[]{"darwin-" + osArch, "darwin-universal2"};
        }
        if (osName.contains("win")) {
            osName = "win32";
        }
        return new String[]{osName + "-" + osArch};
    }

    private static void loadNativeLibraryFromJar(@NotNull String libName) throws IOException {
        for (String machineResourcesDir : getCurrentMachineResourcesDirs()) {
            String pathWithinJar = "/" + machineResourcesDir + "/lib/" + System.mapLibraryName(libName);
            if (NativeLib.class.getResource(pathWithinJar) != null) {
                String tempLibPath = createTemporaryFileFromJar(pathWithinJar);
                System.load(tempLibPath);
                return;
            }
        }
        throw new IOException("Native library '" + libName + "' not found");
    }

    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0092: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('in' java.io.InputStream)]) A[TRY_LEAVE], block:B:23:0x0092 */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0097: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:25:0x0097 */
    private static void copyFileFromJar(@NotNull String pathWithinJar, @NotNull String pathOnDisk) throws IOException {
        InputStream in;
        Throwable th;
        byte[] copyBuffer = new byte[4096];
        OutputStream out = new BufferedOutputStream(new FileOutputStream(pathOnDisk));
        Throwable th2 = null;
        try {
            try {
                InputStream in2 = NativeLib.class.getResourceAsStream(pathWithinJar);
                Throwable th3 = null;
                if (in2 == null) {
                    throw new FileNotFoundException("File " + pathWithinJar + " was not found inside JAR.");
                }
                while (true) {
                    int bytesRead = in2.read(copyBuffer);
                    if (bytesRead == -1) {
                        break;
                    } else {
                        out.write(copyBuffer, 0, bytesRead);
                    }
                }
                if (in2 != null) {
                    if (0 != 0) {
                        try {
                            in2.close();
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                    } else {
                        in2.close();
                    }
                }
                if (out != null) {
                    if (0 == 0) {
                        out.close();
                        return;
                    }
                    try {
                        out.close();
                    } catch (Throwable th5) {
                        th2.addSuppressed(th5);
                    }
                }
            } catch (Throwable th6) {
                if (in != null) {
                    if (th != null) {
                        try {
                            in.close();
                        } catch (Throwable th7) {
                            th.addSuppressed(th7);
                        }
                    } else {
                        in.close();
                    }
                }
                throw th6;
            }
        } catch (Throwable th8) {
            if (out != null) {
                if (0 != 0) {
                    try {
                        out.close();
                    } catch (Throwable th9) {
                        th2.addSuppressed(th9);
                    }
                } else {
                    out.close();
                }
            }
            throw th8;
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
        String[] parts2 = filename.split("\\.", 2);
        String prefix = parts2[0] + "-";
        String suffix = parts2.length > 1 ? "." + parts2[parts2.length - 1] : null;
        File libOnDisk = File.createTempFile(prefix, suffix);
        libOnDisk.deleteOnExit();
        File libOnDiskLck = new File(libOnDisk.getAbsolutePath() + LOCK_EXT);
        libOnDiskLck.createNewFile();
        libOnDiskLck.deleteOnExit();
        copyFileFromJar(pathWithinJar, libOnDisk.getPath());
        return libOnDisk.getAbsolutePath();
    }

    private static void cleanup(@NotNull String libName) {
        String searchPattern = libName + "-";
        try {
            Stream<Path> dirList = Files.list(new File(System.getProperty("java.io.tmpdir")).toPath());
            Throwable th = null;
            try {
                try {
                    dirList.filter(path -> {
                        return !path.getFileName().toString().endsWith(LOCK_EXT) && path.getFileName().toString().startsWith(searchPattern);
                    }).forEach(nativeLib -> {
                        Path lckFile = Paths.get(nativeLib + LOCK_EXT, new String[0]);
                        if (Files.notExists(lckFile, new LinkOption[0])) {
                            try {
                                Files.delete(nativeLib);
                            } catch (Exception e) {
                                logger.error("Failed to delete old native lib", (Throwable) e);
                            }
                        }
                    });
                    if (dirList != null) {
                        if (0 != 0) {
                            try {
                                dirList.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            dirList.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (IOException e) {
            logger.error("Failed to open directory", (Throwable) e);
        }
    }
}
