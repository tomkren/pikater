package pikater.ontology.description;

/**
 * Created by marti_000 on 28.12.13.
 */
public class DifferenceVisualizer implements IVisualizer {

    DataSourceDescription targetData;
    DataSourceDescription modelData;

    public DataSourceDescription getTargetData() {
        return targetData;
    }

    public void setTargetData(DataSourceDescription targetData) {
        this.targetData = targetData;
    }

    public DataSourceDescription getModelData() {
        return modelData;
    }

    public void setModelData(DataSourceDescription modelData) {
        this.modelData = modelData;
    }
}
