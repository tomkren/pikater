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

    public List<SmartSym> getSyms(){
        return symLib;
    }




}
