package cz.tomkren.typewars.dag;

import com.martinpilat.DagEvalInterface;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.eva.FitVal;
import cz.tomkren.typewars.eva.TogetherFitFun;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.List;

/** Created by tom on 1. 7. 2015. */

public class DataScientistFitness implements TogetherFitFun {

    private final DagEvalInterface evaluator;
    private final String datasetFile;


    public DataScientistFitness(String datasetFile) {this("http://127.0.0.1:8080", datasetFile);}

    public DataScientistFitness(String url, String datasetFile) {
        this.datasetFile = datasetFile;

        try {

            evaluator = new DagEvalInterface(url);

        } catch (MalformedURLException e) {
            throw new Error(e);
        }

    }

    @Override
    public List<FitVal> getFitVals(List<Object> os) {

        String populationInJson = F.list(os).map(o->(TypedDag)o).reduce(TypedDag::toJson);

        try {
            List<double[]> scores = evaluator.eval(populationInJson, datasetFile);

            return F.map(scores, scoreArr -> {

                double score = 0.0;
                if (scoreArr.length == 0) {
                    System.err.println("Evaluation error !!!");
                } else {
                    score = scoreArr[0];
                }

                return new FitVal.Basic(score);
            });


        } catch (XmlRpcException e) {
            throw new Error(e);
        }

    }
}
