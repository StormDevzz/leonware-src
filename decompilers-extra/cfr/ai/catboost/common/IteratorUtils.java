/*
 * Decompiled with CFR 0.152.
 */
package ai.catboost.common;

import java.util.Iterator;
import java.util.function.BiPredicate;

public class IteratorUtils {
    public static <T, S> boolean elementsEqual(Iterator<T> lhs, Iterator<S> rhs, BiPredicate<T, S> equalFunction) {
        while (lhs.hasNext() && rhs.hasNext()) {
            if (equalFunction.test(lhs.next(), rhs.next())) continue;
            return false;
        }
        return !lhs.hasNext() && !rhs.hasNext();
    }
}

