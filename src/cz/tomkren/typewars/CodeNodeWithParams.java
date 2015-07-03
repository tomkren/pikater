package cz.tomkren.typewars;

import cz.tomkren.helpers.AA;
import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Comb0;
import cz.tomkren.helpers.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Function;

/** Created by pejsek on 3.7.2015. */

public class CodeNodeWithParams extends CodeNode {

    private final Function<JSONObject,Comb0> params2code;
    private final JSONObject paramsInfo;
    private final Map<String,Integer> selectedParamIndices;


    public CodeNodeWithParams(ProtoNode protoNode, Function<JSONObject,Comb0> params2code, JSONObject paramsInfo, Map<String,Integer> selectedParamIndices) {
        super(protoNode, null);
        this.params2code = params2code;
        this.paramsInfo = paramsInfo;
        this.selectedParamIndices = selectedParamIndices;
    }

    @Override
    public Comb0 getCode() {
        JSONObject params = getParams();
        Log.it("BOND HERE 2! "+params);
        return params2code.apply(params);
    }

    /*public CodeNodeWithParams(ProtoNode protoNode, Comb0 code, JSONObject paramsInfo, Map<String,Integer> selectedParamIndices) {
        super(protoNode, code);
        this.paramsInfo = paramsInfo;
        this.selectedParamIndices = selectedParamIndices;
    }*/

    public CodeNodeWithParams randomCopy(Random rand) {

        Map<String,Integer> newIndices = new HashMap<>();

        for (Object oKey : paramsInfo.keySet()) {
            String key = (String) oKey;

            JSONArray possibleValues = paramsInfo.getJSONArray(key);
            int newIndex = rand.nextInt(possibleValues.length());

            newIndices.put(key, newIndex);
        }

        return new CodeNodeWithParams(this, params2code, paramsInfo, newIndices );
    }

    public JSONObject getParams() {

        JSONObject params = new JSONObject();

        if (selectedParamIndices != null) {
            for (Map.Entry<String,Integer> e : selectedParamIndices.entrySet()) {
                String paramName = e.getKey();
                int selectedIndex = e.getValue();
                params.put(paramName, paramsInfo.getJSONArray(paramName).get(selectedIndex) );
            }
        }

        return params;
    }


    public static void main(String[] args) {
        Checker ch = new Checker();
        Random r = ch.getRandom();

        String testAllParamsInfo = "{\"DT\":{\"max_features\":[0.05,0.1,0.25,0.5,0.75,1],\"criterion\":[\"gini\",\"entropy\"],\"min_samples_split\":[1,2,5,10,20],\"max_depth\":[1,2,5,10,15,25,50,100],\"min_samples_leaf\":[1,2,5,10,20]},\"SVC\":{\"tol\":[1.0E-4,0.001,0.01],\"C\":[0.1,0.5,1,2,5,10,15],\"gamma\":[0,1.0E-4,0.001,0.01,0.1,0.5]},\"kMeans\":{},\"union\":{},\"copy\":{},\"kBest\":{\"k\":[1,3,6,9,12]},\"gaussianNB\":{},\"logR\":{\"tol\":[1.0E-4,0.001,0.01],\"C\":[0.1,0.5,1,2,5,10,15],\"penalty\":[\"l1\",\"l2\"],\"solver\":[\"newton-cg\",\"lbfgs\",\"liblinear\"]},\"vote\":{},\"PCA\":{\"n_components\":[1,3,6,9,12],\"whiten\":[false,true]}}";

        JSONObject allParams = new JSONObject(testAllParamsInfo);
        JSONObject pcaParams = allParams.getJSONObject("PCA");

        Log.it(pcaParams.toString(2));

        String name = "PCA";
        Type type = Types.parse("D => D");

        Function<JSONObject,Comb0> params2comb = params -> (haxTypeInput -> {
            Type t = (Type) haxTypeInput.get(0);
            AA<Type> p = TypedDag.getBoxInOutTypes(t);
            Log.it("BOND HERE! "+params);
            return new TypedDag(name, p._1(), p._2(), params);
        });


        CodeNodeWithParams node = new CodeNodeWithParams(new ProtoNode(name, type),params2comb,pcaParams, null);

        ch.it( node.getParams() , "{}" );

        ch.it( ((TypedDag)node.getCode().compute1(type)).toJson() );

        for (int i = 0; i < 10; i++) {
            Log.it("---------------------------------------------------------");
            CodeNodeWithParams node2 = node.randomCopy(r);
            ch.it(node2.getParams() );
            ch.it( ((TypedDag)node2.getCode().compute1(type)).toJson() );
        }


        ch.results();
    }


}
