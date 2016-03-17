package eu.vlaurin.hamcrest.dbunit;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

/**
 * @since 0.1.0
 */
public abstract class DbUnitMatcherTest {

    private final FlatXmlDataSet dataSet;

    public DbUnitMatcherTest(String dataSetFileName) {
        try {
            dataSet = new FlatXmlDataSetBuilder().build(DbUnitMatcherTest.class.getResourceAsStream(dataSetFileName));
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }

    protected ITable getTable(String tableName) {
        try {
            return dataSet.getTable(tableName);
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }
}
