package org.pikater.core.agents.system.computation.graph.edges;

/**
 * Edge with datasource id
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:03
 */
public class DataSourceEdge extends EdgeValue {
    private String dataSourceId;
    private boolean isFile=false;

    /**
     * Gets id of the datasource
     * @return Datasource id
     */
    public String getDataSourceId() {
        return dataSourceId;
    }

    /**
     * Sets datasource id
     * @param dataSourceId Id of the datasource
     */
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    /**
     * Is file or is virtual
     * @return True if dataset
     */
    public boolean isFile() {
        return isFile;
    }

    /**
     * Sets if is dataset or preprocess
     * @param isFile if is file
     */
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }
}
