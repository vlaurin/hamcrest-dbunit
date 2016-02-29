package eu.vlaurin.hamcrest.dbunit;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

/**
 * @since 0.1.0
 */
public abstract class DbUnitMatcherTest {

    private static final String EXPECTED_DATASET = "IsEqualTest.dataset.xml";
    private static final String EXPECTED_TABLE_NAME = "user_table";
    private static final String NOT_EQUAL_ROW_COUNT_TABLE_NAME = "not_row_count_user_table";
    private static final String NOT_EQUAL_COL_DIFF_TABLE_NAME = "not_column_diff_user_table";
    private static final String NOT_EQUAL_DATA_TABLE_NAME = "not_data_user_table";

    private FlatXmlDataSet expectedDatasSet;

    protected ITable createExpectedTable() {
        try {
            return getExpectedDataSet().getTable(EXPECTED_TABLE_NAME);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    protected ITable createNotEqualTable_rowCount() {
        try {
            return getExpectedDataSet().getTable(NOT_EQUAL_ROW_COUNT_TABLE_NAME);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    protected ITable createNotEqualTable_columnDiff() {
        try {
            return getExpectedDataSet().getTable(NOT_EQUAL_COL_DIFF_TABLE_NAME);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    protected ITable createNotEqualTable_data() {
        try {
            return getExpectedDataSet().getTable(NOT_EQUAL_DATA_TABLE_NAME);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    private FlatXmlDataSet getExpectedDataSet() {
        try {
            if (null == expectedDatasSet) {
                expectedDatasSet = new FlatXmlDataSetBuilder().build(DbUnitMatcherTest.class.getResourceAsStream(EXPECTED_DATASET));
            }
            return expectedDatasSet;
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }
}
