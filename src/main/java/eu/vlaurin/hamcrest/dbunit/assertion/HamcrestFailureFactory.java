package eu.vlaurin.hamcrest.dbunit.assertion;

import org.dbunit.assertion.FailureFactory;

/**
 * Implementation of DbUnit's {@link FailureFactory} used by {@link HamcrestDbUnitAssert} to throw {@link
 * HamcrestFailure} when an assertion fails.
 *
 * @see FailureFactory
 * @see HamcrestDbUnitAssert
 * @see HamcrestFailure
 * @since 1.0.0
 */
class HamcrestFailureFactory implements FailureFactory {
    public Error createFailure(String message, String expected, String actual) {
        return new HamcrestFailure(message, expected, actual);
    }

    public Error createFailure(String message) {
        return new HamcrestFailure(message, "", "");
    }
}
