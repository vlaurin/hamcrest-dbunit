package eu.vlaurin.hamcrest.dbunit.matcher.decorator.filtered;

import eu.vlaurin.hamcrest.dbunit.matcher.decorator.MatcherDecorator;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Uses DbUnit's {@link DefaultColumnFilter} to exclude columns during comparison of {@link ITable}.
 *
 * @see DefaultColumnFilter
 * @see ITable
 * @since 0.1.0
 */
public final class Filtered extends TypeSafeMatcher<ITable> implements MatcherDecorator<ITable> {

    private final Matcher<ITable> matcher;
    private final ITable expectedTable;

    private Filtered(Matcher<ITable> matcher, ITable expectedTable) {
        this.matcher = matcher;
        this.expectedTable = expectedTable;
    }

    @Override
    protected boolean matchesSafely(ITable actualTable) {
        try {
            final ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
            return matcher.matches(filteredTable);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    public void describeTo(Description description) {
        description.appendText("filtered: ");
        matcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(ITable item, Description mismatchDescription) {
        mismatchDescription.appendText("filtered: ");
        matcher.describeMismatch(item, mismatchDescription);
    }

    /**
     * @return the decorated matcher
     */
    @Override
    public Matcher<ITable> getMatcher() {
        return matcher;
    }

    /**
     * Creates a {@link Filtered} decorator for matchers of {@link ITable} that only includes in the comparison the columns of the expected table.
     * For better readability, this method should only be called through {@link Filterable}. For example:
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable).filtered());
     * </pre>
     *
     * @param matcher
     *         Not null. Matcher decorated by {@link Filtered}
     * @param expectedTable
     *         Not null. The expected state of the tables compared, used as reference for columns inclusion
     */
    public static Filtered filtered(Matcher<ITable> matcher, ITable expectedTable) {
        return new Filtered(matcher, expectedTable);
    }
}
