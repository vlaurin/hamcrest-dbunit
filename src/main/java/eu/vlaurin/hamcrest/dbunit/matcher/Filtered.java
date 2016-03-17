package eu.vlaurin.hamcrest.dbunit.matcher;

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
public class Filtered extends TypeSafeMatcher<ITable> {

    private final Matcher<ITable> matcher;
    private final ITable expectedTable;

    public Filtered(Matcher<ITable> matcher, ITable expectedTable) {
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
}
