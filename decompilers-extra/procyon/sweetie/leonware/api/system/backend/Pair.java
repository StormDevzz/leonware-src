// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.backend;

public class Pair<A, B>
{
    private final A left;
    private final B right;
    
    public Pair(final A left, final B right) {
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
