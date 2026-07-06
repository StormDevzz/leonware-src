// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.io.InputStream;
import java.util.Objects;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.io.File;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;

@SuppressFBWarnings({ "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE" })
final class NativeLibraryLoader implements Closeable
{
    private static final String PROP_LIBRARY_DISABLE = "org.newsclub.net.unix.library.disable";
    private static final String PROP_LIBRARY_OVERRIDE = "org.newsclub.net.unix.library.override";
    private static final String PROP_LIBRARY_OVERRIDE_FORCE = "org.newsclub.net.unix.library.override.force";
    private static final String PROP_LIBRARY_TMPDIR = "org.newsclub.net.unix.library.tmpdir";
    private static final File TEMP_DIR;
    private static final String OS_NAME_SIMPLIFIED;
    private static final List<String> ARCHITECTURE_AND_OS;
    private static final String LIBRARY_NAME = "junixsocket-native";
    private static final AtomicBoolean LOADED;
    private static final boolean IS_ANDROID;
    
    static File tempDir() {
        return NativeLibraryLoader.TEMP_DIR;
    }
    
    private List<LibraryCandidate> tryProviderClass(final String providerClassname, final String artifactName) throws IOException, ClassNotFoundException {
        final Class<?> providerClass = Class.forName(providerClassname);
        final String version = getArtifactVersion(providerClass, artifactName);
        final String libraryNameAndVersion = "junixsocket-native-" + version;
        return this.findLibraryCandidates(artifactName, libraryNameAndVersion, providerClass);
    }
    
    public static String getJunixsocketVersion() throws IOException {
        final String v = BuildProperties.getBuildProperties().get("git.build.version");
        if (v != null && !v.startsWith("$")) {
            return v;
        }
        return getArtifactVersion(AFSocket.class, "junixsocket-common");
    }
    
    private static String getArtifactVersion(final Class<?> providerClass, final String... artifactNames) throws IOException {
        final int length = artifactNames.length;
        final int n = 0;
        if (n < length) {
            final String artifactName = artifactNames[n];
            final Properties p = new Properties();
            final String resource = "/META-INF/maven/com.kohlschutter.junixsocket/" + artifactName + "/pom.properties";
            try (final InputStream in = providerClass.getResourceAsStream(resource)) {
                if (in == null) {
                    throw new FileNotFoundException("Could not find resource " + resource + " relative to " + providerClass);
                }
                p.load(in);
                final String version = p.getProperty("version");
                Objects.requireNonNull(version, "Could not read version from pom.properties");
                return version;
            }
        }
        throw new IllegalStateException("No artifact names specified");
    }
    
    private synchronized void setLoaded(final String library) {
        setLoaded0(library);
    }
    
    @SuppressFBWarnings({ "THROWS_METHOD_THROWS_RUNTIMEEXCEPTION" })
    private static synchronized void setLoaded0(final String library) {
        if (NativeLibraryLoader.LOADED.compareAndSet(false, true)) {
            NativeUnixSocket.setLoaded(true);
            AFSocket.loadedLibrary = library;
            try {
                NativeUnixSocket.init();
            }
            catch (final RuntimeException e) {
                throw e;
            }
            catch (final Exception e2) {
                throw new IllegalStateException(e2);
            }
        }
    }
    
    private Throwable loadLibraryOverride() {
        String libraryOverride = System.getProperty("org.newsclub.net.unix.library.override", "");
        String libraryOverrideForce = System.getProperty("org.newsclub.net.unix.library.override.force", "false");
        boolean overrideIsAbsolute;
        try {
            overrideIsAbsolute = (libraryOverrideForce.length() > 5 && new File(libraryOverrideForce).isAbsolute());
        }
        catch (final Exception e) {
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
                this.setLoaded(libraryOverride);
                return null;
            }
            catch (final Exception | LinkageError e2) {
                if (Boolean.parseBoolean(libraryOverrideForce)) {
                    throw e2;
                }
                return e2;
            }
        }
        return new Exception("No library specified with -Dorg.newsclub.net.unix.library.override=");
    }
    
    private static Object loadLibrarySyncMonitor() {
        final Object monitor = NativeLibraryLoader.class.getClassLoader();
        if (monitor == null) {
            return NativeLibraryLoader.class;
        }
        return monitor;
    }
    
    public synchronized void loadLibrary() {
        synchronized (loadLibrarySyncMonitor()) {
            if (NativeLibraryLoader.LOADED.get()) {
                return;
            }
            NativeUnixSocket.initPre();
            if ("provided".equals(System.getProperty("org.newsclub.net.unix.library.override.force", ""))) {
                this.setLoaded("provided");
                return;
            }
            boolean provided = false;
            try {
                NativeUnixSocket.noop();
                provided = true;
            }
            catch (final UnsatisfiedLinkError | Exception ex2) {}
            if (provided) {
                this.setLoaded("provided");
                return;
            }
            if (Boolean.parseBoolean(System.getProperty("org.newsclub.net.unix.library.disable", "false"))) {
                throw this.initCantLoadLibraryError((List<Throwable>)Collections.singletonList(new UnsupportedOperationException("junixsocket disabled by System.property org.newsclub.net.unix.library.disable")));
            }
            final List<Throwable> suppressedThrowables = new ArrayList<Throwable>();
            final Throwable ex = this.loadLibraryOverride();
            if (ex == null) {
                return;
            }
            suppressedThrowables.add(ex);
            final List<LibraryCandidate> candidates = this.initLibraryCandidates(suppressedThrowables);
            String loadedLibraryId = null;
            for (final LibraryCandidate candidate : candidates) {
                try {
                    if ((loadedLibraryId = candidate.load()) != null) {
                        break;
                    }
                    continue;
                }
                catch (final Exception | LinkageError e) {
                    suppressedThrowables.add(e);
                }
            }
            for (final LibraryCandidate candidate : candidates) {
                candidate.close();
            }
            if (loadedLibraryId == null) {
                throw this.initCantLoadLibraryError(suppressedThrowables);
            }
            this.setLoaded(loadedLibraryId);
        }
    }
    
    private UnsatisfiedLinkError initCantLoadLibraryError(final List<Throwable> suppressedThrowables) {
        String message = "Could not load native library junixsocket-native for architecture " + NativeLibraryLoader.ARCHITECTURE_AND_OS;
        final String cp = System.getProperty("java.class.path", "");
        if (cp.contains("junixsocket-native-custom/target-eclipse") || cp.contains("junixsocket-native-common/target-eclipse")) {
            message += "\n\n*** ECLIPSE USERS ***\nIf you're running from within Eclipse, please close the projects \"junixsocket-native-common\" and \"junixsocket-native-custom\"\n";
        }
        final UnsatisfiedLinkError e = new UnsatisfiedLinkError(message);
        if (suppressedThrowables != null) {
            for (final Throwable suppressed : suppressedThrowables) {
                e.addSuppressed(suppressed);
            }
        }
        throw e;
    }
    
    private List<LibraryCandidate> initLibraryCandidates(final List<Throwable> suppressedThrowables) {
        final List<LibraryCandidate> candidates = new ArrayList<LibraryCandidate>();
        try {
            final String version = getArtifactVersion(this.getClass(), "junixsocket-common", "junixsocket-core");
            if (version != null) {
                candidates.add(new StandardLibraryCandidate(version));
            }
        }
        catch (final Exception e) {
            suppressedThrowables.add(e);
        }
        try {
            candidates.addAll(this.tryProviderClass("org.newsclub.lib.junixsocket.custom.NarMetadata", "junixsocket-native-custom"));
        }
        catch (final Exception e) {
            suppressedThrowables.add(e);
        }
        try {
            candidates.addAll(this.tryProviderClass("org.newsclub.lib.junixsocket.common.NarMetadata", "junixsocket-native-common"));
        }
        catch (final Exception e) {
            suppressedThrowables.add(e);
        }
        candidates.add(new StandardLibraryCandidate(null));
        return candidates;
    }
    
    private static String lookupArchProperty(final String key, final String defaultVal) {
        return System.getProperty(key, defaultVal).replaceAll("[ /\\\\'\";:\\$]", "");
    }
    
    private static List<String> architectureAndOS() {
        final String arch = lookupArchProperty("os.arch", "UnknownArch");
        final List<String> list = new ArrayList<String>();
        if (NativeLibraryLoader.IS_ANDROID) {
            list.add(arch + "-Android");
        }
        list.add(arch + "-" + NativeLibraryLoader.OS_NAME_SIMPLIFIED);
        if (NativeLibraryLoader.OS_NAME_SIMPLIFIED.startsWith("Windows") && !"Windows10".equals(NativeLibraryLoader.OS_NAME_SIMPLIFIED)) {
            list.add(arch + "-Windows10");
        }
        if ("MacOSX".equals(NativeLibraryLoader.OS_NAME_SIMPLIFIED) && "x86_64".equals(arch)) {
            list.add("aarch64-MacOSX");
        }
        return list;
    }
    
    private static boolean checkAndroid() {
        final String vmName = lookupArchProperty("java.vm.name", "UnknownVM");
        final String vmSpecVendor = lookupArchProperty("java.vm.specification.vendor", "UnknownSpecificationVendor");
        return "Dalvik".equals(vmName) || vmSpecVendor.contains("Android");
    }
    
    static boolean isAndroid() {
        return NativeLibraryLoader.IS_ANDROID;
    }
    
    static List<String> getArchitectureAndOS() {
        return NativeLibraryLoader.ARCHITECTURE_AND_OS;
    }
    
    private static URL validateResourceURL(final URL url) {
        if (url == null) {
            return null;
        }
        try (final InputStream unused = url.openStream()) {
            return url;
        }
        catch (final IOException e) {
            return null;
        }
    }
    
    private static String mapLibraryName(final String libraryNameAndVersion) {
        String mappedName = System.mapLibraryName(libraryNameAndVersion);
        if (mappedName.endsWith(".so")) {
            final String os_NAME_SIMPLIFIED = NativeLibraryLoader.OS_NAME_SIMPLIFIED;
            switch (os_NAME_SIMPLIFIED) {
                case "AIX": {
                    mappedName = mappedName.substring(0, mappedName.length() - 3) + ".a";
                    break;
                }
                case "OS400": {
                    mappedName = mappedName.substring(0, mappedName.length() - 3) + ".srvpgm";
                    break;
                }
            }
        }
        return mappedName;
    }
    
    private List<LibraryCandidate> findLibraryCandidates(final String artifactName, final String libraryNameAndVersion, final Class<?> providerClass) {
        final String mappedName = mapLibraryName(libraryNameAndVersion);
        final String[] prefixes = mappedName.startsWith("lib") ? new String[] { "" } : new String[] { "", "lib" };
        final List<LibraryCandidate> list = new ArrayList<LibraryCandidate>();
        for (final String archOs : NativeLibraryLoader.ARCHITECTURE_AND_OS) {
            for (final String compiler : new String[] { "clang", "gcc" }) {
                for (final String prefix : prefixes) {
                    final String path = "/lib/" + archOs + "-" + compiler + "/jni/" + prefix + mappedName;
                    URL url = validateResourceURL(providerClass.getResource(path));
                    if (url != null) {
                        list.add(new ClasspathLibraryCandidate(artifactName, libraryNameAndVersion, path, url));
                    }
                    final String nodepsPath = this.nodepsPath(path);
                    if (nodepsPath != null) {
                        url = validateResourceURL(providerClass.getResource(nodepsPath));
                        if (url != null) {
                            list.add(new ClasspathLibraryCandidate(artifactName, libraryNameAndVersion, nodepsPath, url));
                        }
                    }
                }
            }
        }
        return list;
    }
    
    private String nodepsPath(final String path) {
        final int lastDot = path.lastIndexOf(46);
        if (lastDot == -1) {
            return null;
        }
        return path.substring(0, lastDot) + ".nodeps" + path.substring(lastDot);
    }
    
    @Override
    public void close() {
    }
    
    static {
        OS_NAME_SIMPLIFIED = lookupArchProperty("os.name", "UnknownOS");
        ARCHITECTURE_AND_OS = architectureAndOS();
        LOADED = new AtomicBoolean(false);
        IS_ANDROID = checkAndroid();
        final String dir = System.getProperty("org.newsclub.net.unix.library.tmpdir", null);
        TEMP_DIR = ((dir == null) ? null : new File(dir));
    }
    
    private abstract static class LibraryCandidate implements Closeable
    {
        protected final String libraryNameAndVersion;
        
        protected LibraryCandidate(final String libraryNameAndVersion) {
            this.libraryNameAndVersion = libraryNameAndVersion;
        }
        
        @SuppressFBWarnings({ "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION" })
        abstract String load() throws Exception;
        
        @Override
        public abstract void close();
        
        @Override
        public String toString() {
            return super.toString() + "[" + this.libraryNameAndVersion + "]";
        }
    }
    
    private static final class StandardLibraryCandidate extends LibraryCandidate
    {
        StandardLibraryCandidate(final String version) {
            super((version == null) ? "junixsocket-native" : ("junixsocket-native-" + version));
        }
        
        @SuppressFBWarnings({ "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION" })
        @Override
        String load() throws Exception, LinkageError {
            if (this.libraryNameAndVersion != null) {
                System.loadLibrary(this.libraryNameAndVersion);
                return this.libraryNameAndVersion;
            }
            return null;
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public String toString() {
            return super.toString() + "(standard library path)";
        }
    }
    
    private static final class ClasspathLibraryCandidate extends LibraryCandidate
    {
        private final String artifactName;
        private final URL library;
        private final String path;
        
        ClasspathLibraryCandidate(final String artifactName, final String libraryNameAndVersion, final String path, final URL library) {
            super(libraryNameAndVersion);
            this.artifactName = artifactName;
            this.path = path;
            this.library = library;
        }
        
        @Override
        synchronized String load() throws IOException, LinkageError {
            if (this.libraryNameAndVersion == null) {
                return null;
            }
            File libDir = NativeLibraryLoader.TEMP_DIR;
            int attempt = 0;
            while (attempt < 3) {
                File libFile;
                try {
                    libFile = File.createTempFile("libtmp", System.mapLibraryName(this.libraryNameAndVersion), libDir);
                    try (final InputStream libraryIn = this.library.openStream();
                         final OutputStream out = new FileOutputStream(libFile)) {
                        final byte[] buf = new byte[4096];
                        int read;
                        while ((read = libraryIn.read(buf)) >= 0) {
                            out.write(buf, 0, read);
                        }
                    }
                }
                catch (final IOException e) {
                    throw e;
                }
                Label_0288: {
                    try {
                        System.load(libFile.getAbsolutePath());
                    }
                    catch (final UnsatisfiedLinkError e2) {
                        switch (attempt) {
                            case 0: {
                                libDir = new File(System.getProperty("user.home", "."));
                                break;
                            }
                            case 1: {
                                libDir = new File(System.getProperty("user.dir", "."));
                                break;
                            }
                            default: {
                                throw e2;
                            }
                        }
                        break Label_0288;
                    }
                    finally {
                        if (!libFile.delete()) {
                            libFile.deleteOnExit();
                        }
                    }
                    break;
                }
                ++attempt;
                continue;
                break;
            }
            return this.artifactName + "/" + this.libraryNameAndVersion;
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public String toString() {
            return super.toString() + "(" + this.artifactName + ":" + this.path + ")";
        }
    }
}
