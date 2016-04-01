package eu.vlaurin.hamcrest.dbunit.decorator;

import org.hamcrest.Matcher;

/**
 * Describes a class that decorates a Hamcrest {@link Matcher}.
 *
 * @since 0.1.0
 */
public interface MatcherDecorator<T> extends Matcher<T> {
    /**
     * @return the decorated matcher
     */
    Matcher<T> getMatcher();
}
