package sweetie.leonware.api.system.backend;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/backend/Pair.class */
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
