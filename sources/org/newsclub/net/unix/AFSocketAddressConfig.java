package org.newsclub.net.unix;

import java.net.SocketException;
import java.net.URI;
import java.util.Set;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketAddressConfig.class */
public abstract class AFSocketAddressConfig<A extends AFSocketAddress> {
    protected abstract A parseURI(URI uri, int i) throws SocketException;

    protected abstract AFSocketAddress.AFSocketAddressConstructor<A> addressConstructor();

    protected abstract String selectorProviderClassname();

    protected abstract Set<String> uriSchemes();

    protected AFSocketAddressConfig() {
    }
}
