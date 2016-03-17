package eu.vlaurin.hamcrest.dbunit.matcher;

import eu.vlaurin.hamcrest.dbunit.assertion.HamcrestDbUnitAssert;
import eu.vlaurin.hamcrest.dbunit.assertion.HamcrestFailure;
import eu.vlaurin.hamcrest.dbunit.matcher.decorator.filtered.Filterable;
import eu.vlaurin.hamcrest.dbunit.matcher.decorator.filtered.Filtered;
import org.dbunit.DatabaseUnitException;
import org.dbunit.assertion.DbUnitAssert;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matches DbUnit tables equal to the expected table (rows, columns and data).
 *
 * @see ITable
 * @since 0.1.0
 */
public final class IsEqual extends TypeSafeMatcher<ITable> implements Filterable {

    private static final DbUnitAssert DB_UNIT_ASSERT = new HamcrestDbUnitAssert();
    private static final String COLUMN_SEPARATOR = ", ";

    private final ITable expectedTable;
    private HamcrestFailure hamcrestFailure;

    public IsEqual(ITable expectedTable) {
        if (null == expectedTable) {
            throw new IllegalArgumentException("Non-null value required by IsEqual()");
        }
        this.expectedTable = expectedTable;
    }

    @Override
    protected boolean matchesSafely(ITable actualTable) {
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
                       .appendText(describeTable(expectedTable));
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
    @Override
    public Filtered filtered() {
        return Filtered.filtered(this, expectedTable);
    }
}
