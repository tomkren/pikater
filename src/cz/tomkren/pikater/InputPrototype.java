package cz.tomkren.pikater;

import cz.tomkren.helpers.F;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchdescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/** Created by tom on 3. 6. 2015.*/
public class InputPrototype extends BoxPrototype {


    public InputPrototype() {
        super("input", null, at->null, (dp,sources)->{}, null );
    }


    @Override
    public List<DataSourceDescription> mkOutputs(DataProcessing dataProcessing, int numOut) {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        return F.fill(numOut, fileDataSource);
    }

}
