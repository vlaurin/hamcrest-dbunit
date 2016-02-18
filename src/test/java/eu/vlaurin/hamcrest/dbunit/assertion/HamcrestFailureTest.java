package eu.vlaurin.hamcrest.dbunit.assertion;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @since 1.0.0
 */
public class HamcrestFailureTest {

    private static final String MESSAGE = "Failure message";
    private static final String EXPECTED = "Expected string";
    private static final String ACTUAL = "Actual string";

    private HamcrestFailure hamcrestFailure;

    @Before
    public void setUp() {
        hamcrestFailure = new HamcrestFailure(MESSAGE, EXPECTED, ACTUAL);
    }

    @Test
    public void getExpectedAsProvided() throws Exception {
        assertThat(hamcrestFailure.getExpected(), equalTo(EXPECTED));
    }

    @Test
    public void getActualAsProvided() throws Exception {
        assertThat(hamcrestFailure.getActual(), equalTo(ACTUAL));
    }

    @Test
    public void getMessageAsProvided() throws Exception {
        assertThat(hamcrestFailure.getMessage(), equalTo(MESSAGE));
    }
}