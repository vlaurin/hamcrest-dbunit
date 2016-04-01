package eu.vlaurin.hamcrest.dbunit.matcher;

import org.hamcrest.Matcher;

/**
 * A matcher for comparing two objects of a same type.
 *
 * Matcher implementations <strong>should not</strong> directly implement this interface. Instead, extend the {@link ComparatorTypeSafeMatcher} abstract class.
 *
 * @since 0.1.0
 */
public interface ComparatorMatcher<T> extends Matcher<T> {
    boolean matches(T expected, T actual);
}
