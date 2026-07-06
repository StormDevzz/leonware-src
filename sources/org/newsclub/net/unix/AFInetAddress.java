package org.newsclub.net.unix;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFInetAddress.class */
class AFInetAddress {
    private static final byte[] LOCAL_AF = {127, 0, 0, -81};
    private static final char PREFIX = '[';
    private static final String MARKER_HEX_ENCODING = "%%";
    static final String INETADDR_SUFFIX = ".junixsocket";

    AFInetAddress() {
    }

    static final String createUnresolvedHostname(byte[] socketAddress, AFAddressFamily<?> af) {
        StringBuilder sb = new StringBuilder(1 + socketAddress.length + INETADDR_SUFFIX.length() + 8);
        sb.append('[');
        try {
            sb.append(URLEncoder.encode(new String(socketAddress, StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1.toString()));
            sb.append('.');
            sb.append(af.getJuxString());
            sb.append(INETADDR_SUFFIX);
            String str = sb.toString();
            if (str.length() < 64 || str.getBytes(StandardCharsets.UTF_8).length <= 255) {
                return str;
            }
            sb.setLength(0);
            sb.append('[');
            sb.append(MARKER_HEX_ENCODING);
            for (byte b : socketAddress) {
                sb.append(String.format(Locale.ENGLISH, "%02x", Byte.valueOf(b)));
            }
            sb.append('.');
            sb.append(af.getJuxString());
            sb.append(INETADDR_SUFFIX);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    static final InetAddress wrapAddress(byte[] socketAddress, AFAddressFamily<?> af) {
        Objects.requireNonNull(af);
        if (socketAddress == null || socketAddress.length == 0) {
            return null;
        }
        String hostname = createUnresolvedHostname(socketAddress, af);
        byte[] bytes = hostname.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 255) {
            throw new IllegalStateException("junixsocket address is too long to wrap as InetAddress");
        }
        try {
            return InetAddress.getByAddress(hostname, LOCAL_AF);
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    static final byte[] unwrapAddress(InetAddress addr, AFAddressFamily<?> af) throws SocketException {
        Objects.requireNonNull(addr);
        if (!isSupportedAddress(addr, af)) {
            throw new SocketException("Unsupported address");
        }
        String hostname = addr.getHostName();
        try {
            return unwrapAddress(hostname, af);
        } catch (IllegalArgumentException e) {
            throw ((SocketException) new SocketException("Unsupported address").initCause(e));
        }
    }

    static final byte[] unwrapAddress(String hostname, AFAddressFamily<?> af) throws SocketException {
        Objects.requireNonNull(hostname);
        if (!hostname.endsWith(INETADDR_SUFFIX)) {
            throw new SocketException("Unsupported address");
        }
        int end = hostname.length() - INETADDR_SUFFIX.length();
        int domDot = -1;
        int i = end - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            char c = hostname.charAt(i);
            if (c != '.') {
                i--;
            } else {
                domDot = i;
                break;
            }
        }
        String juxString = hostname.substring(domDot + 1, end);
        if (AFAddressFamily.getAddressFamily(juxString) != af) {
            throw new SocketException("Incompatible address");
        }
        String encodedHostname = hostname.substring(1, domDot);
        if (encodedHostname.startsWith(MARKER_HEX_ENCODING)) {
            int len = encodedHostname.length();
            if ((len & 1) == 1) {
                throw new IllegalStateException("Length of hex-encoded wrapping must be even");
            }
            byte[] unwrapped = new byte[(len - 2) / 2];
            int i2 = 2;
            int n = encodedHostname.length();
            int o = 0;
            while (i2 < n) {
                int v = Integer.parseInt(encodedHostname.substring(i2, i2 + 2), 16);
                unwrapped[o] = (byte) (v & 255);
                i2 += 2;
                o++;
            }
            return unwrapped;
        }
        try {
            return URLDecoder.decode(encodedHostname, StandardCharsets.ISO_8859_1.toString()).getBytes(StandardCharsets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    static boolean isSupportedAddress(InetAddress addr, AFAddressFamily<?> af) {
        if ((addr instanceof Inet4Address) && addr.isLoopbackAddress()) {
            String hostname = addr.getHostName();
            return hostname.endsWith(af.getJuxInetAddressSuffix());
        }
        return false;
    }
}
