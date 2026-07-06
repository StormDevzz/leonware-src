// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Objects;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.SocketException;
import java.net.URI;
import java.util.regex.Pattern;

public final class HostAndPort
{
    private static final Pattern PAT_HOST_AND_PORT;
    private final String hostname;
    private final int port;
    
    public HostAndPort(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.getHostname() == null) ? 0 : this.getHostname().hashCode());
        result = 31 * result + this.getPort();
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAndPort)) {
            return false;
        }
        final HostAndPort other = (HostAndPort)obj;
        if (this.getHostname() == null) {
            if (other.getHostname() != null) {
                return false;
            }
        }
        else if (!this.getHostname().equals(other.getHostname())) {
            return false;
        }
        return this.getPort() == other.getPort();
    }
    
    @Override
    public String toString() {
        if (this.getPort() == -1) {
            return this.getHostname();
        }
        return this.getHostname() + ":" + this.getPort();
    }
    
    public static HostAndPort parseFrom(final URI u) throws SocketException {
        String host = u.getHost();
        if (host != null) {
            return new HostAndPort(host, u.getPort());
        }
        final String raw = u.getRawSchemeSpecificPart();
        final Matcher m = HostAndPort.PAT_HOST_AND_PORT.matcher(raw);
        if (!m.find()) {
            throw new SocketException("Cannot parse URI: " + u);
        }
        try {
            host = URLDecoder.decode(m.group("host"), "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        final String portStr = m.group("port");
        int port;
        if (portStr == null) {
            port = -1;
        }
        else {
            port = Integer.parseInt(portStr);
        }
        return new HostAndPort(host, port);
    }
    
    private static String urlEncode(final String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public String getHostname() {
        return this.hostname;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public URI toURI(final String scheme) {
        return this.toURI(scheme, null, null, null, null);
    }
    
    public URI toURI(final String scheme, final URI template) {
        if (template == null) {
            return this.toURI(scheme, null, null, null, null);
        }
        String rawAuthority = template.getRawAuthority();
        final int at = rawAuthority.indexOf(64);
        if (at >= 0) {
            rawAuthority = rawAuthority.substring(0, at);
        }
        else if (rawAuthority.length() > 0 && template.getHost() == null) {
            rawAuthority = null;
        }
        else if (rawAuthority.length() > 0 && template.getAuthority().equals(template.getHost())) {
            rawAuthority = null;
        }
        else if (rawAuthority.length() > 0 && template.getAuthority().equals(template.getHost() + ":" + template.getPort())) {
            rawAuthority = null;
        }
        return this.toURI(scheme, rawAuthority, template.getRawPath(), template.getRawQuery(), template.getRawFragment());
    }
    
    public URI toURI(final String scheme, final String rawAuthority, final String rawPath, final String rawQuery, final String rawFragment) {
        Objects.requireNonNull(scheme);
        if (rawPath != null && !rawPath.isEmpty() && !rawPath.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute: " + rawPath);
        }
        return URI.create(scheme + "://" + ((rawAuthority == null) ? "" : (rawAuthority + "@")) + urlEncode(this.getHostname()).replace("%2C", ",") + ((this.port <= 0) ? "" : (":" + this.port)) + ((rawPath == null) ? "" : rawPath) + ((rawQuery == null) ? "" : ("?" + rawQuery)) + ((rawFragment == null) ? "" : ("#" + rawFragment)));
    }
    
    static {
        PAT_HOST_AND_PORT = Pattern.compile("^//((?<userinfo>[^/\\@]*)\\@)?(?<host>[^/\\:]+)(?:\\:(?<port>[0-9]+))?");
    }
}
