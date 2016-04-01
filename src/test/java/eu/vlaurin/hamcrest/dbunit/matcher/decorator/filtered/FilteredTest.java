package eu.vlaurin.hamcrest.dbunit.matcher.decorator.filtered;

import eu.vlaurin.hamcrest.dbunit.DbUnitMatcherTest;
import eu.vlaurin.hamcrest.dbunit.decorator.filtered.Filtered;
import eu.vlaurin.hamcrest.dbunit.matcher.ComparatorMatcher;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static eu.vlaurin.hamcrest.test.TestMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * @since 0.1.0
 */
public class FilteredTest extends DbUnitMatcherTest {

    private static final String DATASET = "FilteredTest.dataset.xml";
    private static final String ACTUAL_TABLE = "actual_user_table";
    private static final String EXPECTED_TABLE = "expected_user_table";
    private ComparatorMatcher<ITable> innerMatcher;

    public FilteredTest() {
        super(DATASET);
    }

    @Before
    public void setUp() {
        innerMatcher = mock(ComparatorMatcher.class);
        when(innerMatcher.matches(any())).thenReturn(false);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Description description = (Description) invocationOnMock.getArguments()[0];
                description.appendText("inner description");
                return null;
            }
        }).when(innerMatcher).describeTo(Mockito.any(Description.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Description description = (Description) invocationOnMock.getArguments()[1];
                description.appendText("inner mismatch");
                return null;
            }
        }).when(innerMatcher).describeMismatch(any(), Mockito.any(Description.class));
    }

    @Test
    public void isNullSafe() {
        assertThat(Filtered.filtered(innerMatcher, getTable(EXPECTED_TABLE)), is(nullSafe()));
    }

    @Test
    public void isUnknownTypeSafe() {
        assertThat(Filtered.filtered(innerMatcher, getTable(EXPECTED_TABLE)), is(unknownTypeSafe()));
    }

    @Test
    public void decoratesInnerMatcher() {
        final Filtered filtered = Filtered.filtered(innerMatcher, getTable(EXPECTED_TABLE));
        assertThat(innerMatcher, sameInstance(filtered.getMatcher()));
    }

    @Test
    public void failingInnerMatcherCausesMatcherToFail() {
        final ITable actualTable = getTable(ACTUAL_TABLE);
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        final Filtered filtered = Filtered.filtered(innerMatcher, expectedTable);

        assertThat(filtered.matches(actualTable), is(false));
        verify(innerMatcher).matches(Mockito.any(ITable.class), Mockito.any(ITable.class));
    }

    @Test
    public void successfulInnerMatcherCausesMatcherToSucceed() {
        final ITable actualTable = getTable(ACTUAL_TABLE);
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        final ComparatorMatcher<ITable> successfulMatcher = mock(ComparatorMatcher.class);
        when(successfulMatcher.matches(Mockito.any(ITable.class), Mockito.any(ITable.class))).thenReturn(true);
        final Filtered filtered = Filtered.filtered(successfulMatcher, expectedTable);

        assertThat(filtered.matches(actualTable), is(true));
        verify(successfulMatcher).matches(Mockito.any(ITable.class), Mockito.any(ITable.class));
    }

    @Test
    public void describesItselfUsingInnerMatcher() {
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        final Filtered filtered = Filtered.filtered(innerMatcher, expectedTable);

        assertThat(filtered, hasDescription("filtered: inner description"));
        verify(innerMatcher).describeTo(Mockito.any(Description.class));
    }

    @Test
    public void describesMismatchUsingInnerMatcher() {
        final ITable actualTable = getTable(ACTUAL_TABLE);
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        final Filtered filtered = Filtered.filtered(innerMatcher, expectedTable);

        assertThat(filtered, hasMismatchDescription("filtered: inner mismatch", actualTable));
        verify(innerMatcher).describeMismatch(any(), Mockito.any(Description.class));
    }

    @Test
    public void columnsAreExcludedFromComparisonBasedOnExpectedTable() throws Exception {
        final ITable actualTable = getTable(ACTUAL_TABLE);
        assumeThat(getColumnNames(actualTable), contains("id", "created_at", "username", "name", "email"));
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        assumeThat(getColumnNames(expectedTable), contains("username", "name", "email"));

        final Filtered filtered = Filtered.filtered(innerMatcher, expectedTable);

        filtered.matches(actualTable);

        final ArgumentCaptor<ITable> expectedCaptor = ArgumentCaptor.forClass(ITable.class);
        final ArgumentCaptor<ITable> actualCaptor = ArgumentCaptor.forClass(ITable.class);
        verify(innerMatcher).matches(expectedCaptor.capture(), actualCaptor.capture());
        assertThat(getColumnNames(expectedCaptor.getValue()), contains("username", "name", "email"));
        assertThat(getColumnNames(actualCaptor.getValue()), contains("username", "name", "email"));
    }

    @Test
    public void columnsAreExcludedFromComparisonBasedOnListProvided() throws Exception {
        final ITable actualTable = getTable(ACTUAL_TABLE);
        assumeThat(getColumnNames(actualTable), contains("id", "created_at", "username", "name", "email"));
        final ITable expectedTable = getTable(EXPECTED_TABLE);
        assumeThat(getColumnNames(expectedTable), contains("username", "name", "email"));

        final Filtered filtered = Filtered.filtered(innerMatcher, expectedTable, new String[]{"username", "email"});

        filtered.matches(actualTable);

        final ArgumentCaptor<ITable> expectedCaptor = ArgumentCaptor.forClass(ITable.class);
        final ArgumentCaptor<ITable> actualCaptor = ArgumentCaptor.forClass(ITable.class);
        verify(innerMatcher).matches(expectedCaptor.capture(), actualCaptor.capture());
        assertThat(getColumnNames(expectedTable), contains("username", "name", "email"));
        assertThat(getColumnNames(expectedCaptor.getValue()), contains("username", "email"));
        assertThat(getColumnNames(actualCaptor.getValue()), contains("username", "email"));
    }

    protected List<String> getColumnNames(ITable table) throws DataSetException {
        final Column[] columns = table.getTableMetaData().getColumns();
        final List<String> names = new ArrayList<>(columns.length);
        for (Column column : columns) {
            names.add(column.getColumnName());
        }
        return names;
    }
}