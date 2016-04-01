package eu.vlaurin.hamcrest.dbunit.matcher;

import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * Convenient base class for comparator Matchers that require an actual non-null value of a specific type.
 * This simply implements the null check, checks the type and then casts.
 *
 * @since 0.1.0
 */
public abstract class ComparatorTypeSafeMatcher<T> extends TypeSafeMatcher<T> implements ComparatorMatcher<T> {
    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);
    private T expected;

    protected ComparatorTypeSafeMatcher(T expected) {
        super(TYPE_FINDER);
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        return matchesSafely(expected, actual);
    }

    @Override
    public boolean matches(T expected, T actual) {
        this.expected = expected;
        return matches(actual);
    }

    protected abstract boolean matchesSafely(T expected, T actual);

    protected T getExpected() {
        return expected;
    }
}
