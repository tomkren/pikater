package cz.tomkren.typewars;


import cz.tomkren.helpers.F;
import org.json.JSONObject;

import java.util.List;

public class SmartLib {

    //private final CodeLib codeLib;
    private final List<SmartSym> symLib;

    public SmartLib(CodeLib codeLib) {
        //this.codeLib = codeLib;
        symLib = F.map(codeLib.getCodeNodes() , SmartSym::new);

        for (SmartSym smartSym : symLib) {
            smartSym.initApplicableSons(symLib);
        }
    }

    public static SmartLib mk(JSONObject paramsInfo, String... codeNodeLines) {
        return new SmartLib(CodeLib.mk(paramsInfo, codeNodeLines));
    }

    public static SmartLib mk(String... codeNodeLines) {
        return new SmartLib(CodeLib.mk(codeNodeLines));
    }

    public List<SmartSym> getSyms(){
        return symLib;
    }

    public SmartSym getSym(int i){
        return symLib.get(i);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("sym \t -> \t applicableSons\n");
        sb.append("-------------------------------------------------------------------------------\n");


        for (SmartSym smartSym : symLib) {

            String asStr = F.list(smartSym.getApplicableSons()).foldr("",(symList,str)-> F.list(symList).foldr(" ",(sym,s)-> sym.getName() +" "+s ) +"| "+ str );

            sb.append( smartSym.getName() ).append(" \t -> \t ").append(asStr).append("\n");
        }
        return sb.toString();
    }

    public static SmartLib mkDataScientistLib01FromParamsInfo(String paramsInfo) {
        return mkDataScientistLib01FromParamsInfo(new JSONObject(paramsInfo));
    }

    public static SmartLib mkDataScientistLib01FromParamsInfo(JSONObject paramsInfo) {
        return SmartLib.mk(paramsInfo,
                "TypedDag.dia( TypedDag: D => D , TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
                "TypedDag.dia0( TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
                "TypedDag.split( TypedDag: D => (V D n) , MyList: V (D => LD) n ) : D => (V LD n)",
                "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
                "MyList.nil : V a 0",
                "PCA : D => D",
                "kBest : D => D",
                "kMeans : D => (V D (S(S n)))",
                "copy : D => (V D (S(S n)))",
                "SVC        : D => LD",
                "logR       : D => LD",
                "gaussianNB : D => LD",
                "DT         : D => LD",
                "vote : (V LD (S(S n))) => LD"
        );
    }

    public static final SmartLib DATA_SCIENTIST_WITH_PARAMS_01 = mkDataScientistLib01FromParamsInfo("{\"DT\":{\"max_features\":[0.05,0.1,0.25,0.5,0.75,1],\"criterion\":[\"gini\",\"entropy\"],\"min_samples_split\":[1,2,5,10,20],\"max_depth\":[1,2,5,10,15,25,50,100],\"min_samples_leaf\":[1,2,5,10,20]},\"SVC\":{\"tol\":[1.0E-4,0.001,0.01],\"C\":[0.1,0.5,1,2,5,10,15],\"gamma\":[0,1.0E-4,0.001,0.01,0.1,0.5]},\"kMeans\":{},\"union\":{},\"copy\":{},\"kBest\":{\"k\":[1,3,6,9,12]},\"gaussianNB\":{},\"logR\":{\"tol\":[1.0E-4,0.001,0.01],\"C\":[0.1,0.5,1,2,5,10,15],\"penalty\":[\"l1\",\"l2\"],\"solver\":[\"newton-cg\",\"lbfgs\",\"liblinear\"]},\"vote\":{},\"PCA\":{\"n_components\":[1,3,6,9,12],\"whiten\":[false,true]}}");

    public static final SmartLib DATA_SCIENTIST_01 = SmartLib.mk(
            "TypedDag.dia( TypedDag: D => D , TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
            "TypedDag.dia0( TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
            "TypedDag.split( TypedDag: D => (V D n) , MyList: V (D => LD) n ) : D => (V LD n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "kBest : D => D",
            "kMeans : D => (V D (S(S n)))",
            "copy : D => (V D (S(S n)))",
            "SVC        : D => LD",
            "logR       : D => LD",
            "gaussianNB : D => LD",
            "DT         : D => LD",
            "vote : (V LD (S(S n))) => LD"
    );



}
