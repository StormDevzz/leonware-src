// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

final class PathUtil
{
    private PathUtil() {
        throw new IllegalStateException("No instances");
    }
    
    static boolean isPathInDefaultFileSystem(final Path p) {
        final FileSystem fs = p.getFileSystem();
        return fs == FileSystems.getDefault();
    }
}
