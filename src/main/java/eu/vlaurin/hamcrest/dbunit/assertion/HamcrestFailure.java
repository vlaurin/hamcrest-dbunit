package eu.vlaurin.hamcrest.dbunit.assertion;

import org.dbunit.dataset.ITable;

/**
 * Thrown when a DbUnit {@link HamcrestDbUnitAssert#assertEquals(ITable, ITable) assertEquals(...)} fails. Similar to
 * JUnit {@link junit.framework.ComparisonFailure ComparisonFailure} except that the {@link AssertionError#getMessage()}
 * returns the message unchanged.
 *
 * @see junit.framework.ComparisonFailure
 * @see HamcrestDbUnitAssert
 * @since 0.1.0
 */
public class HamcrestFailure extends AssertionError {
    private final String expected;
    private final String actual;

    /**
     * @param message
     *         Detail message saved for later retrieval by the {@link #getMessage()} method. Nullable.
     * @param expected
     *         String detailing what was expected by the assertion. Saved for later retrieval by the {@link
     *         #getExpected()} method. Nullable.
     * @param actual
     *         String detailing what was the actual value that failed the assertion. Saved for later retrieval by the
     *         {@link #getActual()} method. Nullable.
     */
    HamcrestFailure(String message, String expected, String actual) {
        super(message);
        this.expected = expected;
        this.actual = actual;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }
}
