package org.newsclub.net.unix;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/HostAndPort.class */
public final class HostAndPort {
    private static final Pattern PAT_HOST_AND_PORT = Pattern.compile("^//((?<userinfo>[^/\\@]*)\\@)?(?<host>[^/\\:]+)(?:\\:(?<port>[0-9]+))?");
    private final String hostname;
    private final int port;

    public HostAndPort(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public int hashCode() {
        int result = (31 * 1) + (getHostname() == null ? 0 : getHostname().hashCode());
        return (31 * result) + getPort();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAndPort)) {
            return false;
        }
        HostAndPort other = (HostAndPort) obj;
        if (getHostname() == null) {
            if (other.getHostname() != null) {
                return false;
            }
        } else if (!getHostname().equals(other.getHostname())) {
            return false;
        }
        return getPort() == other.getPort();
    }

    public String toString() {
        if (getPort() == -1) {
            return getHostname();
        }
        return getHostname() + ":" + getPort();
    }

    public static HostAndPort parseFrom(URI u) throws SocketException {
        int port;
        String host = u.getHost();
        if (host != null) {
            return new HostAndPort(host, u.getPort());
        }
        String raw = u.getRawSchemeSpecificPart();
        Matcher m = PAT_HOST_AND_PORT.matcher(raw);
        if (!m.find()) {
            throw new SocketException("Cannot parse URI: " + u);
        }
        try {
            String host2 = URLDecoder.decode(m.group("host"), "UTF-8");
            String portStr = m.group("port");
            if (portStr == null) {
                port = -1;
            } else {
                port = Integer.parseInt(portStr);
            }
            return new HostAndPort(host2, port);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public URI toURI(String scheme) {
        return toURI(scheme, null, null, null, null);
    }

    public URI toURI(String scheme, URI template) {
        if (template == null) {
            return toURI(scheme, null, null, null, null);
        }
        String rawAuthority = template.getRawAuthority();
        int at = rawAuthority.indexOf(64);
        if (at >= 0) {
            rawAuthority = rawAuthority.substring(0, at);
        } else if (rawAuthority.length() > 0 && template.getHost() == null) {
            rawAuthority = null;
        } else if (rawAuthority.length() > 0 && template.getAuthority().equals(template.getHost())) {
            rawAuthority = null;
        } else if (rawAuthority.length() > 0 && template.getAuthority().equals(template.getHost() + ":" + template.getPort())) {
            rawAuthority = null;
        }
        return toURI(scheme, rawAuthority, template.getRawPath(), template.getRawQuery(), template.getRawFragment());
    }

    public URI toURI(String scheme, String rawAuthority, String rawPath, String rawQuery, String rawFragment) {
        Objects.requireNonNull(scheme);
        if (rawPath != null && !rawPath.isEmpty() && !rawPath.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute: " + rawPath);
        }
        return URI.create(scheme + "://" + (rawAuthority == null ? "" : rawAuthority + "@") + urlEncode(getHostname()).replace("%2C", ",") + (this.port <= 0 ? "" : ":" + this.port) + (rawPath == null ? "" : rawPath) + (rawQuery == null ? "" : "?" + rawQuery) + (rawFragment == null ? "" : "#" + rawFragment));
    }
}
