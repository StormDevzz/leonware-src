package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NativeLibraryLoader.class */
@SuppressFBWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"})
final class NativeLibraryLoader implements Closeable {
    private static final String PROP_LIBRARY_DISABLE = "org.newsclub.net.unix.library.disable";
    private static final String PROP_LIBRARY_OVERRIDE = "org.newsclub.net.unix.library.override";
    private static final String PROP_LIBRARY_OVERRIDE_FORCE = "org.newsclub.net.unix.library.override.force";
    private static final String PROP_LIBRARY_TMPDIR = "org.newsclub.net.unix.library.tmpdir";
    private static final File TEMP_DIR;
    private static final String LIBRARY_NAME = "junixsocket-native";
    private static final String OS_NAME_SIMPLIFIED = lookupArchProperty("os.name", "UnknownOS");
    private static final List<String> ARCHITECTURE_AND_OS = architectureAndOS();
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);
    private static final boolean IS_ANDROID = checkAndroid();

    static {
        String dir = System.getProperty(PROP_LIBRARY_TMPDIR, null);
        TEMP_DIR = dir == null ? null : new File(dir);
    }

    NativeLibraryLoader() {
    }

    static File tempDir() {
        return TEMP_DIR;
    }

    private List<LibraryCandidate> tryProviderClass(String providerClassname, String artifactName) throws ClassNotFoundException, IOException {
        Class<?> providerClass = Class.forName(providerClassname);
        String version = getArtifactVersion(providerClass, artifactName);
        String libraryNameAndVersion = "junixsocket-native-" + version;
        return findLibraryCandidates(artifactName, libraryNameAndVersion, providerClass);
    }

    public static String getJunixsocketVersion() throws IOException {
        String v = BuildProperties.getBuildProperties().get("git.build.version");
        if (v != null && !v.startsWith("$")) {
            return v;
        }
        return getArtifactVersion(AFSocket.class, "junixsocket-common");
    }

    private static String getArtifactVersion(Class<?> providerClass, String... artifactNames) throws IOException {
        if (0 < artifactNames.length) {
            String artifactName = artifactNames[0];
            Properties p = new Properties();
            String resource = "/META-INF/maven/com.kohlschutter.junixsocket/" + artifactName + "/pom.properties";
            InputStream in = providerClass.getResourceAsStream(resource);
            try {
                if (in == null) {
                    throw new FileNotFoundException("Could not find resource " + resource + " relative to " + providerClass);
                }
                p.load(in);
                String version = p.getProperty("version");
                Objects.requireNonNull(version, "Could not read version from pom.properties");
                if (in != null) {
                    in.close();
                }
                return version;
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
        throw new IllegalStateException("No artifact names specified");
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NativeLibraryLoader$LibraryCandidate.class */
    private static abstract class LibraryCandidate implements Closeable {
        protected final String libraryNameAndVersion;

        @SuppressFBWarnings({"THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION"})
        abstract String load() throws Exception;

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public abstract void close();

        protected LibraryCandidate(String libraryNameAndVersion) {
            this.libraryNameAndVersion = libraryNameAndVersion;
        }

        public String toString() {
            return super.toString() + "[" + this.libraryNameAndVersion + "]";
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NativeLibraryLoader$StandardLibraryCandidate.class */
    private static final class StandardLibraryCandidate extends LibraryCandidate {
        StandardLibraryCandidate(String version) {
            super(version == null ? NativeLibraryLoader.LIBRARY_NAME : "junixsocket-native-" + version);
        }

        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate
        @SuppressFBWarnings({"THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION"})
        String load() throws Exception {
            if (this.libraryNameAndVersion != null) {
                System.loadLibrary(this.libraryNameAndVersion);
                return this.libraryNameAndVersion;
            }
            return null;
        }

        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate
        public String toString() {
            return super.toString() + "(standard library path)";
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NativeLibraryLoader$ClasspathLibraryCandidate.class */
    private static final class ClasspathLibraryCandidate extends LibraryCandidate {
        private final String artifactName;
        private final URL library;
        private final String path;

        ClasspathLibraryCandidate(String artifactName, String libraryNameAndVersion, String path, URL library) {
            super(libraryNameAndVersion);
            this.artifactName = artifactName;
            this.path = path;
            this.library = library;
        }

        /* JADX WARN: Code restructure failed: missing block: B:63:0x0143, code lost:
        
            return r5.artifactName + "/" + r5.libraryNameAndVersion;
         */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0120 A[SYNTHETIC] */
        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        synchronized java.lang.String load() throws java.lang.LinkageError, java.io.IOException {
            /*
                Method dump skipped, instruction units count: 324
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.newsclub.net.unix.NativeLibraryLoader.ClasspathLibraryCandidate.load():java.lang.String");
        }

        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // org.newsclub.net.unix.NativeLibraryLoader.LibraryCandidate
        public String toString() {
            return super.toString() + "(" + this.artifactName + ":" + this.path + ")";
        }
    }

    private synchronized void setLoaded(String library) {
        setLoaded0(library);
    }

    @SuppressFBWarnings({"THROWS_METHOD_THROWS_RUNTIMEEXCEPTION"})
    private static synchronized void setLoaded0(String library) {
        if (LOADED.compareAndSet(false, true)) {
            NativeUnixSocket.setLoaded(true);
            AFSocket.loadedLibrary = library;
            try {
                NativeUnixSocket.init();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    private Throwable loadLibraryOverride() {
        boolean overrideIsAbsolute;
        String libraryOverride = System.getProperty(PROP_LIBRARY_OVERRIDE, "");
        String libraryOverrideForce = System.getProperty(PROP_LIBRARY_OVERRIDE_FORCE, "false");
        try {
            if (libraryOverrideForce.length() <= 5) {
                overrideIsAbsolute = false;
            } else {
                overrideIsAbsolute = new File(libraryOverrideForce).isAbsolute();
            }
        } catch (Exception e) {
            overrideIsAbsolute = false;
            e.printStackTrace();
        }
        if (libraryOverride.isEmpty() && overrideIsAbsolute) {
            libraryOverride = libraryOverrideForce;
            libraryOverrideForce = "true";
        }
        if (!libraryOverride.isEmpty()) {
            try {
                System.load(libraryOverride);
                setLoaded(libraryOverride);
                return null;
            } catch (Exception | LinkageError e2) {
                if (Boolean.parseBoolean(libraryOverrideForce)) {
                    throw e2;
                }
                return e2;
            }
        }
        return new Exception("No library specified with -Dorg.newsclub.net.unix.library.override=");
    }

    private static Object loadLibrarySyncMonitor() {
        Object monitor = NativeLibraryLoader.class.getClassLoader();
        if (monitor == null) {
            return NativeLibraryLoader.class;
        }
        return monitor;
    }

    public synchronized void loadLibrary() {
        String strLoad;
        synchronized (loadLibrarySyncMonitor()) {
            if (LOADED.get()) {
                return;
            }
            NativeUnixSocket.initPre();
            if ("provided".equals(System.getProperty(PROP_LIBRARY_OVERRIDE_FORCE, ""))) {
                setLoaded("provided");
                return;
            }
            boolean provided = false;
            try {
                NativeUnixSocket.noop();
                provided = true;
            } catch (Exception | UnsatisfiedLinkError e) {
            }
            if (provided) {
                setLoaded("provided");
                return;
            }
            if (Boolean.parseBoolean(System.getProperty(PROP_LIBRARY_DISABLE, "false"))) {
                throw initCantLoadLibraryError(Collections.singletonList(new UnsupportedOperationException("junixsocket disabled by System.property org.newsclub.net.unix.library.disable")));
            }
            List<Throwable> suppressedThrowables = new ArrayList<>();
            Throwable ex = loadLibraryOverride();
            if (ex == null) {
                return;
            }
            suppressedThrowables.add(ex);
            List<LibraryCandidate> candidates = initLibraryCandidates(suppressedThrowables);
            String loadedLibraryId = null;
            for (LibraryCandidate candidate : candidates) {
                try {
                    strLoad = candidate.load();
                    loadedLibraryId = strLoad;
                } catch (Exception | LinkageError e2) {
                    suppressedThrowables.add(e2);
                }
                if (strLoad != null) {
                    break;
                }
            }
            for (LibraryCandidate candidate2 : candidates) {
                candidate2.close();
            }
            if (loadedLibraryId == null) {
                throw initCantLoadLibraryError(suppressedThrowables);
            }
            setLoaded(loadedLibraryId);
        }
    }

    private UnsatisfiedLinkError initCantLoadLibraryError(List<Throwable> suppressedThrowables) {
        String message = "Could not load native library junixsocket-native for architecture " + ARCHITECTURE_AND_OS;
        String cp = System.getProperty("java.class.path", "");
        if (cp.contains("junixsocket-native-custom/target-eclipse") || cp.contains("junixsocket-native-common/target-eclipse")) {
            message = message + "\n\n*** ECLIPSE USERS ***\nIf you're running from within Eclipse, please close the projects \"junixsocket-native-common\" and \"junixsocket-native-custom\"\n";
        }
        UnsatisfiedLinkError e = new UnsatisfiedLinkError(message);
        if (suppressedThrowables != null) {
            for (Throwable suppressed : suppressedThrowables) {
                e.addSuppressed(suppressed);
            }
        }
        throw e;
    }

    private List<LibraryCandidate> initLibraryCandidates(List<Throwable> suppressedThrowables) {
        List<LibraryCandidate> candidates = new ArrayList<>();
        try {
            String version = getArtifactVersion(getClass(), "junixsocket-common", "junixsocket-core");
            if (version != null) {
                candidates.add(new StandardLibraryCandidate(version));
            }
        } catch (Exception e) {
            suppressedThrowables.add(e);
        }
        try {
            candidates.addAll(tryProviderClass("org.newsclub.lib.junixsocket.custom.NarMetadata", "junixsocket-native-custom"));
        } catch (Exception e2) {
            suppressedThrowables.add(e2);
        }
        try {
            candidates.addAll(tryProviderClass("org.newsclub.lib.junixsocket.common.NarMetadata", "junixsocket-native-common"));
        } catch (Exception e3) {
            suppressedThrowables.add(e3);
        }
        candidates.add(new StandardLibraryCandidate(null));
        return candidates;
    }

    private static String lookupArchProperty(String key, String defaultVal) {
        return System.getProperty(key, defaultVal).replaceAll("[ /\\\\'\";:\\$]", "");
    }

    private static List<String> architectureAndOS() {
        String arch = lookupArchProperty("os.arch", "UnknownArch");
        List<String> list = new ArrayList<>();
        if (IS_ANDROID) {
            list.add(arch + "-Android");
        }
        list.add(arch + "-" + OS_NAME_SIMPLIFIED);
        if (OS_NAME_SIMPLIFIED.startsWith("Windows") && !"Windows10".equals(OS_NAME_SIMPLIFIED)) {
            list.add(arch + "-Windows10");
        }
        if ("MacOSX".equals(OS_NAME_SIMPLIFIED) && "x86_64".equals(arch)) {
            list.add("aarch64-MacOSX");
        }
        return list;
    }

    private static boolean checkAndroid() {
        String vmName = lookupArchProperty("java.vm.name", "UnknownVM");
        String vmSpecVendor = lookupArchProperty("java.vm.specification.vendor", "UnknownSpecificationVendor");
        return "Dalvik".equals(vmName) || vmSpecVendor.contains("Android");
    }

    static boolean isAndroid() {
        return IS_ANDROID;
    }

    static List<String> getArchitectureAndOS() {
        return ARCHITECTURE_AND_OS;
    }

    /* JADX WARN: Unreachable blocks removed: 8, instructions: 12 */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:16:0x0023
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    private static java.net.URL validateResourceURL(java.net.URL r3) {
        /*
            r0 = r3
            if (r0 != 0) goto L6
            r0 = 0
            return r0
        L6:
            r0 = r3
            java.io.InputStream r0 = r0.openStream()     // Catch: java.io.IOException -> L2b
            r4 = r0
            r0 = r3
            r5 = r0
            r0 = r4
            if (r0 == 0) goto L15
            r0 = r4
            r0.close()     // Catch: java.io.IOException -> L2b
        L15:
            r0 = r5
            return r0
        L17:
            r5 = move-exception
            r0 = r4
            if (r0 == 0) goto L29
            r0 = r4
            r0.close()     // Catch: java.lang.Throwable -> L23 java.io.IOException -> L2b
            goto L29
        L23:
            r6 = move-exception
            r0 = r5
            r1 = r6
            r0.addSuppressed(r1)     // Catch: java.io.IOException -> L2b
        L29:
            r0 = r5
            throw r0     // Catch: java.io.IOException -> L2b
        L2b:
            r4 = move-exception
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.newsclub.net.unix.NativeLibraryLoader.validateResourceURL(java.net.URL):java.net.URL");
    }

    private static String mapLibraryName(String libraryNameAndVersion) {
        String mappedName = System.mapLibraryName(libraryNameAndVersion);
        if (mappedName.endsWith(".so")) {
            switch (OS_NAME_SIMPLIFIED) {
                case "AIX":
                    mappedName = mappedName.substring(0, mappedName.length() - 3) + ".a";
                    break;
                case "OS400":
                    mappedName = mappedName.substring(0, mappedName.length() - 3) + ".srvpgm";
                    break;
            }
        }
        return mappedName;
    }

    private List<LibraryCandidate> findLibraryCandidates(String artifactName, String libraryNameAndVersion, Class<?> providerClass) {
        URL url;
        String mappedName = mapLibraryName(libraryNameAndVersion);
        String[] prefixes = mappedName.startsWith("lib") ? new String[]{""} : new String[]{"", "lib"};
        List<LibraryCandidate> list = new ArrayList<>();
        for (String archOs : ARCHITECTURE_AND_OS) {
            for (String compiler : new String[]{"clang", "gcc"}) {
                for (String prefix : prefixes) {
                    String path = "/lib/" + archOs + "-" + compiler + "/jni/" + prefix + mappedName;
                    URL url2 = validateResourceURL(providerClass.getResource(path));
                    if (url2 != null) {
                        list.add(new ClasspathLibraryCandidate(artifactName, libraryNameAndVersion, path, url2));
                    }
                    String nodepsPath = nodepsPath(path);
                    if (nodepsPath != null && (url = validateResourceURL(providerClass.getResource(nodepsPath))) != null) {
                        list.add(new ClasspathLibraryCandidate(artifactName, libraryNameAndVersion, nodepsPath, url));
                    }
                }
            }
        }
        return list;
    }

    private String nodepsPath(String path) {
        int lastDot = path.lastIndexOf(46);
        if (lastDot == -1) {
            return null;
        }
        return path.substring(0, lastDot) + ".nodeps" + path.substring(lastDot);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
