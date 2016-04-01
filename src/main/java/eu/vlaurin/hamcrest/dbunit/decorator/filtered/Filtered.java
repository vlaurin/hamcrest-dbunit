package eu.vlaurin.hamcrest.dbunit.decorator.filtered;

import eu.vlaurin.hamcrest.dbunit.decorator.MatcherDecorator;
import eu.vlaurin.hamcrest.dbunit.matcher.ComparatorMatcher;
import eu.vlaurin.hamcrest.dbunit.matcher.ComparatorTypeSafeMatcher;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.hamcrest.Description;

/**
 * Uses DbUnit's {@link DefaultColumnFilter} to exclude columns during comparison of {@link ITable}.
 *
 * @see DefaultColumnFilter
 * @see ITable
 * @since 0.1.0
 */
public final class Filtered extends ComparatorTypeSafeMatcher<ITable> implements MatcherDecorator<ITable> {

    private final ComparatorMatcher<ITable> matcher;
    private final String[] columns;

    private Filtered(ComparatorMatcher<ITable> matcher, ITable expectedTable) {
        super(expectedTable);
        this.matcher = matcher;
        this.columns = getColumnNames(expectedTable);
    }

    private Filtered(ComparatorMatcher<ITable> matcher, ITable expectedTable, String[] columns) {
        super(expectedTable);
        this.matcher = matcher;
        this.columns = columns;
    }

    @Override
    protected boolean matchesSafely(ITable expectedTable, ITable actualTable) {
        try {
            final ITable filteredActual = DefaultColumnFilter.includedColumnsTable(actualTable, columns);
            final ITable filteredExpected = DefaultColumnFilter.includedColumnsTable(expectedTable, columns);
            return matcher.matches(filteredExpected, filteredActual);
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

    protected String[] getColumnNames(ITable table) {
        try {
            final Column[] columns = table.getTableMetaData().getColumns();
            final String[] names = new String[columns.length];
            for (Integer i = 0; i < columns.length; i++) {
                names[i] = columns[i].getColumnName();
            }
            return names;
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the decorated matcher
     */
    @Override
    public ComparatorMatcher<ITable> getMatcher() {
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
    public static Filtered filtered(ComparatorMatcher<ITable> matcher, ITable expectedTable) {
        return new Filtered(matcher, expectedTable);
    }

    /**
     * Creates a {@link Filtered} decorator for matchers of {@link ITable} that only includes in the comparison the columns specified.
     * For better readability, this method should only be called through {@link Filterable}. For example:
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable).filtered("col1", "col2"));
     * </pre>
     *
     * @param matcher
     *         Not null. Matcher decorated by {@link Filtered}
     * @param expectedTable
     *         Not null. Expected state of the tables compared, ignored columns will be filtered out
     * @param columns
     *         Names of the columns to include in the comparison, all other columns will be excluded
     */
    public static Filtered filtered(ComparatorMatcher<ITable> matcher, ITable expectedTable, String[] columns) {
        return new Filtered(matcher, expectedTable, columns);
    }
}
