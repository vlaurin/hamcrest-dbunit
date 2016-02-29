package eu.vlaurin.hamcrest.dbunit.assertion;

import org.dbunit.assertion.FailureHandler;
import org.dbunit.dataset.Column;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @since 0.1.0
 */
public class HamcrestDbUnitAssertTest {

    @Test
    public void shouldInitializeDefaultFailureHandlerWithHamcrestFailureFactory() {
        final HamcrestDbUnitAssert dbUnitAssert = new HamcrestDbUnitAssert();
        final FailureHandler defaultFailureHandler = dbUnitAssert.getDefaultFailureHandler(new Column[]{});
        final Error failure = defaultFailureHandler.createFailure("irrelevant", "irrelevant", "irrelevant");

        assertThat(failure, is(notNullValue()));
        assertThat(failure, is(instanceOf(HamcrestFailure.class)));
    }

}