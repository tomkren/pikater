package pikater.ontology.description.boxes.visualizers;

import pikater.ontology.description.Box;
import pikater.ontology.description.boxesWrappers.DataSourceDescription;

/**
 * Created by marti_000 on 28.12.13.
 */
public class DifferenceVisualizer extends Box implements IVisualizer {

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
