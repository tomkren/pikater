package cz.tomkren.typewars;


import cz.tomkren.helpers.Comb0;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.basicgen.Generator;

import java.util.*;

//todo možná udělat že implements Lib a tu dát i k NodeLib kterou přejmenovat na ProtoLib

public class CodeLib extends NodeLib {

    Map<String,Comb0> codeMap = new HashMap<>();

    public  CodeLib(List<CodeNode> lib) {
        super(F.map(lib,x->x));
        lib.forEach(node -> codeMap.put(node.getName(), node.getCode()));
    }

    public CodeLib(CodeNode... lib) {
        this(Arrays.asList(lib));
    }

    public Comb0 getCode(String name) {
        return codeMap.get(name);
    }

    public List<CodeNode> getCodeNodes() {
        return F.map( getNodeList() , n -> (CodeNode)n );
    }

    public static CodeLib mk(String semicolonSeparatedLines) {
        return mk(semicolonSeparatedLines.split(";"));
    }

    public static CodeLib mk(String... codeNodeLines) {
        List<CodeNode> libList = new ArrayList<>(codeNodeLines.length);
        for (String line : codeNodeLines) {
            String trimLine = line.trim();
            if (!"".equals(trimLine)) {
                libList.add(CodeNode.mk(trimLine));
            }
        }
        return new CodeLib(libList);
    }

    public List<PolyTree> generate(String goal, int n) {
        return Generator.generate(goal, this, n);
    }

    public List<PolyTree> generate(Type goal, int n) {
        return Generator.generate(goal, this, n);
    }



}


