package cz.tomkren.pikater;

import cz.tomkren.helpers.F;
import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchdescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/** Created by tom on 3. 6. 2015.*/
public class InputPrototype extends BoxPrototype {

    private String filename;

    public InputPrototype(String filename) {
        super("input", null, (at,id)->null, (dp,sources)->{}, null );
        this.filename = filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public DataProcessing mkDataProcessing(int id) {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setId(id);
        fileDataProvider.setFileURI(filename);

        return fileDataProvider;
    }

    @Override
    public List<DataSourceDescription> mkOutputs(DataProcessing fileDataProvider, int numOut) {

        if (filename == null) {throw new Error("Filename must be specified.");}

        DataSourceDescription trainData = new DataSourceDescription();
        trainData.setDataProvider(fileDataProvider);
        trainData.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());

        DataSourceDescription testData = new DataSourceDescription();
        testData.setDataProvider(fileDataProvider);
        testData.setInputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());

        DataSourceDescription validationData = new DataSourceDescription();
        validationData.setDataProvider(fileDataProvider);
        validationData.setInputType(CoreConstant.SlotContent.VALIDATION_DATA.getSlotName());

        DataSourceDescription dataToLabelData = new DataSourceDescription();
        dataToLabelData.setDataProvider(fileDataProvider);
        dataToLabelData.setInputType(CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName());

        return Arrays.asList(trainData,testData,validationData,dataToLabelData,dataToLabelData);
    }

    /*@Override
    public List<DataSourceDescription> mkOutputs(DataProcessing dataProcessing, int numOut) {

        if (filename == null) {throw new Error("Filename must be specified.");}

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI(filename);

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        return F.fill(numOut, fileDataSource);
    }*/

}
