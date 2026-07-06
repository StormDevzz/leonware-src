package org.newsclub.net.unix;

import java.net.SocketOption;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketOption.class */
public final class AFSocketOption<T> implements SocketOption<T> {
    private final String name;
    private final Class<T> type;
    private final int level;
    private final int optionName;

    public AFSocketOption(String name, Class<T> type, int level, int optionName) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.optionName = optionName;
    }

    @Override // java.net.SocketOption
    public String name() {
        return this.name;
    }

    @Override // java.net.SocketOption
    public Class<T> type() {
        return this.type;
    }

    int level() {
        return this.level;
    }

    int optionName() {
        return this.optionName;
    }

    public String toString() {
        return getClass() + ":" + this.name;
    }
}
