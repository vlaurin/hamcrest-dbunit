package eu.vlaurin.hamcrest.dbunit.matcher;

import eu.vlaurin.hamcrest.dbunit.assertion.HamcrestDbUnitAssert;
import eu.vlaurin.hamcrest.dbunit.assertion.HamcrestFailure;
import eu.vlaurin.hamcrest.dbunit.decorator.filtered.Filterable;
import eu.vlaurin.hamcrest.dbunit.decorator.filtered.Filtered;
import org.dbunit.DatabaseUnitException;
import org.dbunit.assertion.DbUnitAssert;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.hamcrest.Description;

/**
 * Matches DbUnit tables equal to the expected table (rows, columns and data).
 *
 * @see ITable
 * @since 0.1.0
 */
public final class IsEqual extends ComparatorTypeSafeMatcher<ITable> implements Filterable {

    private static final DbUnitAssert DB_UNIT_ASSERT = new HamcrestDbUnitAssert();
    private static final String COLUMN_SEPARATOR = ", ";

    private HamcrestFailure hamcrestFailure;

    private IsEqual(ITable expected) {
        super(expected);
        if (null == expected) {
            throw new IllegalArgumentException("Non-null expected table required by IsEqual()");
        }
    }

    @Override
    protected boolean matchesSafely(ITable expectedTable, ITable actualTable) {
        Boolean matches = true;

        try {
            DB_UNIT_ASSERT.assertEquals(expectedTable, actualTable);
        } catch (HamcrestFailure failure) {
            hamcrestFailure = failure;
            matches = false;
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        }

        return matches;
    }

    @Override
    protected void describeMismatchSafely(ITable actualTable, Description mismatchDescription) {
        if (null != hamcrestFailure) {
            mismatchDescription.appendText(hamcrestFailure.getMessage())
                    .appendText(" was: ")
                    .appendValue(hamcrestFailure.getActual());
        } else {
            mismatchDescription.appendText("table: ")
                    .appendText(describeTable(actualTable));
        }
    }

    public void describeTo(Description description) {
        if (null != hamcrestFailure) {
            description.appendText(hamcrestFailure.getMessage())
                    .appendText(" is: ")
                    .appendValue(hamcrestFailure.getExpected());
        } else {
            description.appendText("table: ")
                    .appendText(describeTable(getExpected()));
        }
    }

    private String describeTable(ITable expectedTable) {
        final StringBuilder description = new StringBuilder();

        if (null != expectedTable) {
            final ITableMetaData tableMetaData = expectedTable.getTableMetaData();
            description.append("name=")
                    .append(tableMetaData.getTableName())
                    .append(", rows=")
                    .append(expectedTable.getRowCount())
                    .append(", columns=[");
            try {
                String separator = "";
                for (Column column : tableMetaData.getColumns()) {
                    description.append(separator)
                            .append(column.getColumnName());
                    separator = COLUMN_SEPARATOR;
                }

            } catch (DataSetException e) {
                description.append(e.getMessage());
            }
            description.append("]");
        }

        return description.toString();
    }

    /**
     * Creates an {@link IsEqual} matcher of {@link ITable} that matches when two DbUnit tables are equal. For example:
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable));
     * </pre>
     *
     * @param expectedTable
     *         Not null. The expected state of the tables compared
     */
    public static IsEqual equalTo(ITable expectedTable) {
        return new IsEqual(expectedTable);
    }

    /*
        Decorators
     */

    /**
     * Creates a {@link Filtered} matcher decorator of {@link ITable} that only includes in the comparison the <strong>columns of the expected table</strong>.
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable).filtered());
     * </pre>
     *
     * @return current matcher decorated with {@link Filtered}
     */
    @Override
    public Filtered filtered() {
        return Filtered.filtered(this, getExpected());
    }

    /**
     * Creates a {@link Filtered} matcher decorator of {@link ITable} that only includes in the comparison the <strong>columns specified</strong>.
     * <pre>
     *     ITable actualTable = ...;
     *     ITable expectedTable = ...;
     *
     *     assertThat(actualTable, equalTo(expectedTable).filtered("col1", "col2"));
     * </pre>
     *
     * @param columns
     *         Names of the columns to include in the comparison
     * @return current matcher decorated with {@link Filtered}
     */
    @Override
    public Filtered filtered(String... columns) {
        return Filtered.filtered(this, getExpected(), columns);
    }
}
