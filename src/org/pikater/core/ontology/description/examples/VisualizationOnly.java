package org.pikater.core.ontology.description.examples;

import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileVisualizer;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class VisualizationOnly {


    //sets only the visualization of iris.arff
    public static void createDescription() {

        ComputationDescription cd = new ComputationDescription();

        FileVisualizer fv = new FileVisualizer();
        DataSourceDescription dsd = new DataSourceDescription();
        FileDataProvider fdp = new FileDataProvider();
        fdp.setFileURI("iris.arff");
        dsd.setDataProvider(fdp);
        fv.setDataSource(dsd);

        cd.setRootElement(fv);

    }


}
