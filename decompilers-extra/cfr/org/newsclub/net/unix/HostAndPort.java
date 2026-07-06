/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HostAndPort {
    private static final Pattern PAT_HOST_AND_PORT = Pattern.compile("^//((?<userinfo>[^/\\@]*)\\@)?(?<host>[^/\\:]+)(?:\\:(?<port>[0-9]+))?");
    private final String hostname;
    private final int port;

    public HostAndPort(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.getHostname() == null ? 0 : this.getHostname().hashCode());
        result = 31 * result + this.getPort();
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAndPort)) {
            return false;
        }
        HostAndPort other = (HostAndPort)obj;
        if (this.getHostname() == null ? other.getHostname() != null : !this.getHostname().equals(other.getHostname())) {
            return false;
        }
        return this.getPort() == other.getPort();
    }

    public String toString() {
        if (this.getPort() == -1) {
            return this.getHostname();
        }
        return this.getHostname() + ":" + this.getPort();
    }

    public static HostAndPort parseFrom(URI u) throws SocketException {
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
            host = URLDecoder.decode(m.group("host"), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        String portStr = m.group("port");
        int port = portStr == null ? -1 : Integer.parseInt(portStr);
        return new HostAndPort(host, port);
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
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
        return this.toURI(scheme, null, null, null, null);
    }

    public URI toURI(String scheme, URI template) {
        if (template == null) {
            return this.toURI(scheme, null, null, null, null);
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
        return this.toURI(scheme, rawAuthority, template.getRawPath(), template.getRawQuery(), template.getRawFragment());
    }

    public URI toURI(String scheme, String rawAuthority, String rawPath, String rawQuery, String rawFragment) {
        Objects.requireNonNull(scheme);
        if (rawPath != null && !rawPath.isEmpty() && !rawPath.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute: " + rawPath);
        }
        return URI.create(scheme + "://" + (rawAuthority == null ? "" : rawAuthority + "@") + HostAndPort.urlEncode(this.getHostname()).replace("%2C", ",") + (this.port <= 0 ? "" : ":" + this.port) + (rawPath == null ? "" : rawPath) + (rawQuery == null ? "" : "?" + rawQuery) + (rawFragment == null ? "" : "#" + rawFragment));
    }
}

