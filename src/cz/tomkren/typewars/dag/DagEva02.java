package cz.tomkren.typewars.dag;

import com.martinpilat.DagEvalInterface;
import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import org.json.JSONArray;
import org.json.JSONObject;

/** Created by tom on 3.7.2015. */

public class DagEva02 {

    public static void main(String[] args) {
        Checker ch = new Checker();
        try {



            DagEvalInterface evaluator = new DagEvalInterface("http://127.0.0.1:8080");

            String datasetFile = "winequality-white.csv";
            //String datasetFile = "wilt.csv";

            JSONObject allParams = new JSONObject(evaluator.getMethodParams(datasetFile));

            JSONObject pcaParams = allParams.getJSONObject("PCA");

            JSONArray oneParamValues = pcaParams.getJSONArray("n_components");

            //pcaParams.

            oneParamValues.length();


            Log.it(allParams.toString());
            Log.it(pcaParams.toString(2));
            Log.it(oneParamValues.toString(2));






        } catch (Exception e) {
            throw new Error(e);
        }
        ch.results();
    }

}
