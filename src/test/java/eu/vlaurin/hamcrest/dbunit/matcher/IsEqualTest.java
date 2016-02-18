package eu.vlaurin.hamcrest.dbunit.matcher;

import eu.vlaurin.hamcrest.dbunit.DbUnitMatcherTest;
import org.dbunit.dataset.ITable;
import org.hamcrest.Matcher;
import org.junit.Test;

import static eu.vlaurin.hamcrest.dbunit.DbUnitMatchers.equalTo;
import static eu.vlaurin.hamcrest.test.TestMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * @since 1.0.0
 */
public class IsEqualTest extends DbUnitMatcherTest {

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
        final ITable expectedTable = createExpectedTable();

        assertThat(equalTo(expectedTable), hasDescription("table: name=user_table, rows=2, columns=[id, username, email]"));
    }

    @Test
    public void matchesEqualTables() {
        final ITable expectedTable = createExpectedTable();
        final ITable actualTable = createExpectedTable();

        assertThat(actualTable, equalTo(expectedTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_rowCount() {
        final ITable expectedTable = createExpectedTable();
        final ITable actualTable = createNotEqualTable_rowCount();

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("row count (table=user_table) is: \"2\""));
        assertThat(equalToMatcher, hasMismatchDescription("row count (table=user_table) was: \"1\"", actualTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_columnDiff() {
        final ITable expectedTable = createExpectedTable();
        final ITable actualTable = createNotEqualTable_columnDiff();

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("column count (table=user_table, expectedColCount=3, actualColCount=2) is: \"[email, id, username]\""));
        assertThat(equalToMatcher, hasMismatchDescription("column count (table=user_table, expectedColCount=3, actualColCount=2) was: \"[email, username]\"", actualTable));
    }

    @Test
    public void doesNotMatchNotEqualTables_data() {
        final ITable expectedTable = createExpectedTable();
        final ITable actualTable = createNotEqualTable_data();

        final Matcher<ITable> equalToMatcher = equalTo(expectedTable);

        assertThat(actualTable, not(equalToMatcher));
        assertThat(equalToMatcher, hasDescription("value (table=user_table, row=1, col=email) is: \"jlannister@kingsguard\""));
        assertThat(equalToMatcher, hasMismatchDescription("value (table=user_table, row=1, col=email) was: \"clannister@regent\"", actualTable));
    }

}