package pikater.ontology.description.examples;

import pikater.ontology.description.boxes.providers.FileDataProvider;
import pikater.ontology.description.boxes.visualizers.FileVisualizer;
import pikater.ontology.description.boxesWrappers.ComputationDescription;
import pikater.ontology.description.boxesWrappers.DataSourceDescription;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class VisualizationOnly {


    //sets only the visualization of iris.arff
    public static ComputationDescription createDescription() {

        ComputationDescription cd = new ComputationDescription();

        FileVisualizer fv = new FileVisualizer();
        DataSourceDescription dsd = new DataSourceDescription();
        FileDataProvider fdp = new FileDataProvider();
        fdp.setFileURI("iris.arff");
        dsd.setDataProvider(fdp);
        fv.setDataSource(dsd);

        cd.setRootElement(fv);

        return cd;
    }


}
