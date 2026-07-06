// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost.common;

import java.util.function.BiPredicate;
import java.util.Iterator;

public class IteratorUtils
{
    public static <T, S> boolean elementsEqual(final Iterator<T> lhs, final Iterator<S> rhs, final BiPredicate<T, S> equalFunction) {
        while (lhs.hasNext() && rhs.hasNext()) {
            if (!equalFunction.test(lhs.next(), rhs.next())) {
                return false;
            }
        }
        return !lhs.hasNext() && !rhs.hasNext();
    }
}
