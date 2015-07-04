package cz.tomkren.typewars.dag;

import com.martinpilat.DagEvalInterface;
import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.SmartLib;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.reusable.QuerySolver;
import org.json.JSONObject;

/** Created by tom on 3.7.2015. */

public class DagEva02 {

    public static void main(String[] args) {
        Checker ch = new Checker();
        try {



            DagEvalInterface evaluator = new DagEvalInterface("http://127.0.0.1:8080");

            String datasetFile = "winequality-white.csv";
            //String datasetFile = "wilt.csv";

            JSONObject allParamsInfo = new JSONObject(evaluator.getMethodParams(datasetFile));

            Log.it(allParamsInfo.toString(2) + "\n\n");


            SmartLib lib = SmartLib.mkDataScientistLib01FromParamsInfo(allParamsInfo);

            QuerySolver qs = new QuerySolver(lib, ch.getRandom());

            PolyTree tree = qs.generateOneWithRandomizedParams("D => LD", 20);
            TypedDag dag = (TypedDag)tree.computeValue();

            Log.it( dag.toJson() );



        } catch (Exception e) {
            throw new Error(e);
        }
        ch.results();
    }

}
