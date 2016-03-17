package eu.vlaurin.hamcrest.dbunit.matcher;

import eu.vlaurin.hamcrest.dbunit.DbUnitMatcherTest;
import eu.vlaurin.hamcrest.dbunit.matcher.decorator.filtered.Filtered;
import org.dbunit.dataset.ITable;
import org.hamcrest.Matcher;
import org.junit.Test;

import static eu.vlaurin.hamcrest.dbunit.DbUnitMatchers.equalTo;
import static eu.vlaurin.hamcrest.test.TestMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * @since 0.1.0
 */
public class IsEqualTest extends DbUnitMatcherTest {
    private static final String EXPECTED_DATASET = "IsEqualTest.dataset.xml";
    private static final String EXPECTED_TABLE_NAME = "user_table";
    private static final String NOT_EQUAL_ROW_COUNT_TABLE_NAME = "not_row_count_user_table";
    private static final String NOT_EQUAL_COL_DIFF_TABLE_NAME = "not_column_diff_user_table";
    private static final String NOT_EQUAL_DATA_TABLE_NAME = "not_data_user_table";

    public IsEqualTest() {
        super(EXPECTED_DATASET);
    }

    @Test
    public void isNullSafe() {
        final ITable expectedTable = mock(ITable.class);

        assertThat(equalTo(expectedTable), is(nullSafe()));
    }

    @Test
    public void isUnknownTypeSafe() {
        final ITable expectedTable = mock(ITable.class);

        assertThat(equalTo(expectedTable), is(unknownTypeSafe()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void requiresNonNullValue() {
        equalTo(null);
    }

    @Test
    public void describesItself() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);

        assertThat(equalTo(expectedTable), hasDescription("table: name=user_table, rows=2, columns=[id, username, email]"));
    }

    @Test
    public void matchesEqualTables() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);
        final ITable actualTable = getTable(EXPECTED_TABLE_NAME);

        assertThat(actualTable, equalTo(expectedTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_rowCount() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);
        final ITable actualTable = getTable(NOT_EQUAL_ROW_COUNT_TABLE_NAME);

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("row count (table=user_table) is: \"2\""));
        assertThat(equalToMatcher, hasMismatchDescription("row count (table=user_table) was: \"1\"", actualTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_columnDiff() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);
        final ITable actualTable = getTable(NOT_EQUAL_COL_DIFF_TABLE_NAME);

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("column count (table=user_table, expectedColCount=3, actualColCount=2) is: \"[email, id, username]\""));
        assertThat(equalToMatcher, hasMismatchDescription("column count (table=user_table, expectedColCount=3, actualColCount=2) was: \"[email, username]\"", actualTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_data() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);
        final ITable actualTable = getTable(NOT_EQUAL_DATA_TABLE_NAME);

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("value (table=user_table, row=1, col=email) is: \"jlannister@kingsguard\""));
        assertThat(equalToMatcher, hasMismatchDescription("value (table=user_table, row=1, col=email) was: \"clannister@regent\"", actualTable));
    }

    @Test
    public void supportsFilteredDecorator() {
        final ITable expectedTable = getTable(EXPECTED_TABLE_NAME);

        final IsEqual equalToMatcher = equalTo(expectedTable);

        final Filtered filteredMatcher = equalToMatcher.filtered();
        assertThat(filteredMatcher, is(notNullValue()));
        assertThat(equalToMatcher, sameInstance(filteredMatcher.getMatcher()));
    }

}