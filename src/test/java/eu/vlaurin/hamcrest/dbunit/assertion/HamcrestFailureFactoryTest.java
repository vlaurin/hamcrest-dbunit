package eu.vlaurin.hamcrest.dbunit.assertion;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @since 1.0.0
 */
public class HamcrestFailureFactoryTest {
    private static final String MESSAGE = "Failure message";
    private static final String EXPECTED = "Expected string";
    private static final String ACTUAL = "Actual string";

    private HamcrestFailureFactory failureFactory;

    @Before
    public void setUp() {
        failureFactory = new HamcrestFailureFactory();
    }

    @Test
    public void createFailureWithActualAndExcepted() throws Exception {
        final Error failure = failureFactory.createFailure(MESSAGE, EXPECTED, ACTUAL);

        assertThat(failure, is(notNullValue()));
        assertThat(failure, is(instanceOf(HamcrestFailure.class)));
        assertThat(failure.getMessage(), equalTo(MESSAGE));

        final HamcrestFailure hamcrestFailure = (HamcrestFailure) failure;
        assertThat(hamcrestFailure.getActual(), equalTo(ACTUAL));
        assertThat(hamcrestFailure.getExpected(), equalTo(EXPECTED));
    }

    @Test
    public void createFailureWithMessageOnly() throws Exception {
        final Error failure = failureFactory.createFailure(MESSAGE);

        assertThat(failure, is(notNullValue()));
        assertThat(failure, is(instanceOf(HamcrestFailure.class)));
        assertThat(failure.getMessage(), equalTo(MESSAGE));

        final HamcrestFailure hamcrestFailure = (HamcrestFailure) failure;
        assertThat(hamcrestFailure.getActual(), isEmptyString());
        assertThat(hamcrestFailure.getExpected(), isEmptyString());
    }
}