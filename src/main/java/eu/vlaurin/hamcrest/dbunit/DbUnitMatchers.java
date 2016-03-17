package eu.vlaurin.hamcrest.dbunit;

import eu.vlaurin.hamcrest.dbunit.matcher.IsEqual;
import org.dbunit.dataset.ITable;
import org.hamcrest.Matcher;

/**
 * Provides easy access to the matchers defined in <strong>hamcrest-dbunit</strong>.
 *
 * @since 0.1.0
 */
public class DbUnitMatchers {

    /**
     * Creates an {@link IsEqual} matcher of {@link ITable} that matches when two DbUnit tables are equal. For example:
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable));
     * </pre>
     *
     * @param expectedTable Not null. The expected state of the tables compared
     */
    public static Matcher<ITable> equalTo(ITable expectedTable) {
        return IsEqual.equalTo(expectedTable);
    }
}
