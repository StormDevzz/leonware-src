// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.Set;
import java.net.SocketOption;
import java.util.Map;

final class SocketOptionsMapper
{
    private static final Map<SocketOption<?>, SocketOptionRef> SOCKET_OPTIONS;
    static final Set<SocketOption<?>> SUPPORTED_SOCKET_OPTIONS;
    
    private static <T> void registerSocketOption(final SocketOption<T> option, final int socketOptionsId, final boolean supported) {
        SocketOptionsMapper.SOCKET_OPTIONS.put(option, new SocketOptionRef(socketOptionsId, supported));
    }
    
    static Integer resolve(final SocketOption<?> option) {
        final SocketOptionRef ref = SocketOptionsMapper.SOCKET_OPTIONS.get(option);
        if (ref == null) {
            return null;
        }
        return ref.optionId;
    }
    
    static {
        SOCKET_OPTIONS = new HashMap<SocketOption<?>, SocketOptionRef>();
        registerSocketOption(StandardSocketOptions.SO_KEEPALIVE, 8, false);
        registerSocketOption(StandardSocketOptions.SO_SNDBUF, 4097, true);
        registerSocketOption(StandardSocketOptions.SO_RCVBUF, 4098, true);
        registerSocketOption(StandardSocketOptions.SO_REUSEADDR, 4, true);
        registerSocketOption(StandardSocketOptions.SO_LINGER, 128, true);
        registerSocketOption(StandardSocketOptions.IP_TOS, 3, false);
        registerSocketOption(StandardSocketOptions.TCP_NODELAY, 1, false);
        final Set<SocketOption<?>> supportedOptions = new HashSet<SocketOption<?>>();
        for (final Map.Entry<SocketOption<?>, SocketOptionRef> en : SocketOptionsMapper.SOCKET_OPTIONS.entrySet()) {
            if (en.getValue().supported) {
                supportedOptions.add(en.getKey());
            }
        }
        SUPPORTED_SOCKET_OPTIONS = Collections.unmodifiableSet((Set<? extends SocketOption<?>>)supportedOptions);
    }
    
    private static final class SocketOptionRef
    {
        private final int optionId;
        private final boolean supported;
        
        SocketOptionRef(final int optionId, final boolean supported) {
            this.optionId = optionId;
            this.supported = supported;
        }
    }
}
