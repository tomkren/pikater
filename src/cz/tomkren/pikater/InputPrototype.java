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

    private String filename;

    public InputPrototype() {
        this(null);
    }

    public InputPrototype(String filename) {
        super("input", null, at->null, (dp,sources)->{}, null );
        this.filename = filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    @Override
    public List<DataSourceDescription> mkOutputs(DataProcessing dataProcessing, int numOut) {

        if (filename == null) {throw new Error("Filename must be specified.");}

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI(filename);
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        return F.fill(numOut, fileDataSource);
    }

}
