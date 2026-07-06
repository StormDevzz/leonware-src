/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.system.backend;

public class Pair<A, B> {
    private final A left;
    private final B right;

    public Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public A left() {
        return this.left;
    }

    public B right() {
        return this.right;
    }
}

