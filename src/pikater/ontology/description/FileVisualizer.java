package pikater.ontology.description;

/**
 * Created by marti_000 on 28.12.13.
 */
public class FileVisualizer implements IVisualizer {

    DataSourceDescription dataSource;

    public DataSourceDescription getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceDescription dataSource) {
        this.dataSource = dataSource;
    }
}
