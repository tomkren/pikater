package org.pikater.core.agents.system.computationDescriptionParser.edges;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:03
 */
public class DataSourceEdge extends EdgeValue {
    private String dataSourceId;
    private boolean isFile=false;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }
}
