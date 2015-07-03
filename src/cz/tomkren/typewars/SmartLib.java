package cz.tomkren.typewars;


import cz.tomkren.helpers.F;

import java.util.List;

public class SmartLib {

    //private final CodeLib codeLib;
    private final List<SmartSym> symLib;

    public SmartLib(CodeLib codeLib) {
        //this.codeLib = codeLib;
        symLib = F.map(codeLib.getCodeNodes() , SmartSym::new);
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
