package cz.tomkren.typewars.dag;

import com.martinpilat.DagEvalInterface;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.eva.FitVal;
import cz.tomkren.typewars.eva.TogetherFitFun;
import javassist.bytecode.stackmap.TypeData;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
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

    public JSONObject getAllParamsInfo() {
        try {
            return new JSONObject(evaluator.getMethodParams(datasetFile));
        } catch (XmlRpcException e) {
            throw new Error(e);
        }
    }

    @Override
    public List<FitVal> getFitVals(List<Object> os) {

        String populationInJson = F.list(os).map(o->(TypedDag)o).reduce(TypedDag::toJson);

        //Log.it(populationInJson); // TODO logovat do souboru zde asi

        try {

            Log.it("Evaluating ...");
            List<double[]> scores = evaluator.eval(populationInJson, datasetFile);


            if (scores.size() != os.size()) {
                throw new Error("There must be same number of individuals and fitness values! "+ scores.size() +" != "+ os.size()  );
            }

            Log.it("Evolution operations ...");


            List<FitVal> fitVals = new ArrayList<>(scores.size());
            int i = 0;
            for (double[] scoreArr : scores) {

                double score = 0.0;
                if (scoreArr.length == 0) {
                    System.err.println("Evaluation error !!!");
                    System.err.println(((TypedDag) os.get(i)).toJson());

                } else {
                    score = scoreArr[0];
                }

                if (score < 0.0) {
                    System.err.println("Warning: Score < 0 ... "+score);
                    //System.err.println(((TypedDag) os.get(i)).toJson());
                    score = 0.0;
                }

                fitVals.add(new FitVal.Basic(score));

                i++;
            }

            return fitVals;

            /*return F.map(scores, scoreArr -> {

                double score = 0.0;
                if (scoreArr.length == 0) {
                    System.err.println("Evaluation error !!!");
                } else {
                    score = scoreArr[0];
                }

                return new FitVal.Basic(score);
            });*/


        } catch (XmlRpcException e) {
            throw new Error(e);
        }

    }
}
