// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.Inet4Address;
import java.net.URLDecoder;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.net.InetAddress;
import java.util.Locale;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class AFInetAddress
{
    private static final byte[] LOCAL_AF;
    private static final char PREFIX = '[';
    private static final String MARKER_HEX_ENCODING = "%%";
    static final String INETADDR_SUFFIX = ".junixsocket";
    
    static final String createUnresolvedHostname(final byte[] socketAddress, final AFAddressFamily<?> af) {
        final StringBuilder sb = new StringBuilder(1 + socketAddress.length + ".junixsocket".length() + 8);
        sb.append('[');
        try {
            sb.append(URLEncoder.encode(new String(socketAddress, StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1.toString()));
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        sb.append('.');
        sb.append(af.getJuxString());
        sb.append(".junixsocket");
        final String str = sb.toString();
        if (str.length() < 64 || str.getBytes(StandardCharsets.UTF_8).length <= 255) {
            return str;
        }
        sb.setLength();
        sb.append('[');
        sb.append("%%");
        for (int i = 0, n = socketAddress.length; i < n; ++i) {
            sb.append(String.format(Locale.ENGLISH, "%02x", socketAddress[i]));
        }
        sb.append('.');
        sb.append(af.getJuxString());
        sb.append(".junixsocket");
        return sb.toString();
    }
    
    static final InetAddress wrapAddress(final byte[] socketAddress, final AFAddressFamily<?> af) {
        Objects.requireNonNull(af);
        if (socketAddress == null || socketAddress.length == 0) {
            return null;
        }
        final String hostname = createUnresolvedHostname(socketAddress, af);
        final byte[] bytes = hostname.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 255) {
            throw new IllegalStateException("junixsocket address is too long to wrap as InetAddress");
        }
        try {
            return InetAddress.getByAddress(hostname, AFInetAddress.LOCAL_AF);
        }
        catch (final UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
    
    static final byte[] unwrapAddress(final InetAddress addr, final AFAddressFamily<?> af) throws SocketException {
        Objects.requireNonNull(addr);
        if (!isSupportedAddress(addr, af)) {
            throw new SocketException("Unsupported address");
        }
        final String hostname = addr.getHostName();
        try {
            return unwrapAddress(hostname, af);
        }
        catch (final IllegalArgumentException e) {
            throw (SocketException)new SocketException("Unsupported address").initCause(e);
        }
    }
    
    static final byte[] unwrapAddress(final String hostname, final AFAddressFamily<?> af) throws SocketException {
        Objects.requireNonNull(hostname);
        if (!hostname.endsWith(".junixsocket")) {
            throw new SocketException("Unsupported address");
        }
        final int end = hostname.length() - ".junixsocket".length();
        int domDot = -1;
        for (int i = end - 1; i >= 0; --i) {
            final char c = hostname.charAt(i);
            if (c == '.') {
                domDot = i;
                break;
            }
        }
        final String juxString = hostname.substring(domDot + 1, end);
        if (AFAddressFamily.getAddressFamily(juxString) != af) {
            throw new SocketException("Incompatible address");
        }
        final String encodedHostname = hostname.substring(1, domDot);
        if (encodedHostname.startsWith("%%")) {
            final int len = encodedHostname.length();
            if ((len & 0x1) == 0x1) {
                throw new IllegalStateException("Length of hex-encoded wrapping must be even");
            }
            final byte[] unwrapped = new byte[(len - 2) / 2];
            for (int j = 2, n = encodedHostname.length(), o = 0; j < n; j += 2, ++o) {
                final int v = Integer.parseInt(encodedHostname.substring(j, j + 2), 16);
                unwrapped[o] = (byte)(v & 0xFF);
            }
            return unwrapped;
        }
        else {
            try {
                return URLDecoder.decode(encodedHostname, StandardCharsets.ISO_8859_1.toString()).getBytes(StandardCharsets.ISO_8859_1);
            }
            catch (final UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    static boolean isSupportedAddress(final InetAddress addr, final AFAddressFamily<?> af) {
        if (addr instanceof Inet4Address && addr.isLoopbackAddress()) {
            final String hostname = addr.getHostName();
            return hostname.endsWith(af.getJuxInetAddressSuffix());
        }
        return false;
    }
    
    static {
        LOCAL_AF = new byte[] { 127, 0, 0, -81 };
    }
}
