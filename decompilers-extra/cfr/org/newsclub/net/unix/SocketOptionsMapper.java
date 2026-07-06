/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class SocketOptionsMapper {
    private static final Map<SocketOption<?>, SocketOptionRef> SOCKET_OPTIONS = new HashMap();
    static final Set<SocketOption<?>> SUPPORTED_SOCKET_OPTIONS;

    SocketOptionsMapper() {
    }

    private static <T> void registerSocketOption(SocketOption<T> option, int socketOptionsId, boolean supported) {
        SOCKET_OPTIONS.put(option, new SocketOptionRef(socketOptionsId, supported));
    }

    static Integer resolve(SocketOption<?> option) {
        SocketOptionRef ref = SOCKET_OPTIONS.get(option);
        if (ref == null) {
            return null;
        }
        return ref.optionId;
    }

    static {
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.SO_KEEPALIVE, 8, false);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.SO_SNDBUF, 4097, true);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.SO_RCVBUF, 4098, true);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.SO_REUSEADDR, 4, true);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.SO_LINGER, 128, true);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.IP_TOS, 3, false);
        SocketOptionsMapper.registerSocketOption(StandardSocketOptions.TCP_NODELAY, 1, false);
        HashSet supportedOptions = new HashSet();
        for (Map.Entry<SocketOption<?>, SocketOptionRef> en : SOCKET_OPTIONS.entrySet()) {
            if (!en.getValue().supported) continue;
            supportedOptions.add(en.getKey());
        }
        SUPPORTED_SOCKET_OPTIONS = Collections.unmodifiableSet(supportedOptions);
    }

    private static final class SocketOptionRef {
        private final int optionId;
        private final boolean supported;

        SocketOptionRef(int optionId, boolean supported) {
            this.optionId = optionId;
            this.supported = supported;
        }
    }
}

