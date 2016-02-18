package eu.vlaurin.hamcrest.dbunit.assertion;

import org.dbunit.assertion.DbUnitAssert;
import org.dbunit.assertion.DefaultFailureHandler;
import org.dbunit.assertion.FailureHandler;
import org.dbunit.dataset.Column;

/**
 * Extends {@link DbUnitAssert} to return a {@link DefaultFailureHandler} initialised with an {@link
 * HamcrestFailureFactory} when the {@link #getDefaultFailureHandler(Column[])} method is called.
 *
 * @see DbUnitAssert
 * @see HamcrestFailureFactory
 * @since 1.0.0
 */
public class HamcrestDbUnitAssert extends DbUnitAssert {
    @Override
    protected FailureHandler getDefaultFailureHandler(Column[] additionalColumnInfo) {
        final DefaultFailureHandler failureHandler = new DefaultFailureHandler(additionalColumnInfo);
        failureHandler.setFailureFactory(new HamcrestFailureFactory());

        return failureHandler;
    }
}
