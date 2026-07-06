package org.newsclub.net.unix;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/PathUtil.class */
final class PathUtil {
    private PathUtil() {
        throw new IllegalStateException("No instances");
    }

    static boolean isPathInDefaultFileSystem(Path p) {
        FileSystem fs = p.getFileSystem();
        if (fs != FileSystems.getDefault()) {
            return false;
        }
        return true;
    }
}
