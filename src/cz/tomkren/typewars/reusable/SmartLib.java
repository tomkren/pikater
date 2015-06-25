package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.typewars.CodeLib;

import java.util.List;

public class SmartLib {

    private final CodeLib codeLib;
    private final List<SmartSym> symLib;

    public SmartLib(CodeLib codeLib) {
        this.codeLib = codeLib;
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





}
