package pikater.ontology.description.boxes.visualizers;

import pikater.ontology.description.Box;
import pikater.ontology.description.boxesWrappers.DataSourceDescription;

/**
 * Created by marti_000 on 28.12.13.
 */
public class FileVisualizer extends Box implements IVisualizer {

    DataSourceDescription dataSource;

    public DataSourceDescription getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceDescription dataSource) {
        this.dataSource = dataSource;
    }
    
}
