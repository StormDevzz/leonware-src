/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

final class PathUtil {
    private PathUtil() {
        throw new IllegalStateException("No instances");
    }

    static boolean isPathInDefaultFileSystem(Path p) {
        FileSystem fs = p.getFileSystem();
        return fs == FileSystems.getDefault();
    }
}

