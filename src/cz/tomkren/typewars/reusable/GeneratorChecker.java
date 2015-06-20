package cz.tomkren.typewars.reusable;

import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;

import java.util.*;

/** Created by tom on 19.6.2015. */

public class GeneratorChecker {

    private Map<Integer,Set<String>> checkMap;
    private int maxSizeSoFar;
    private boolean isWholeSizes;

    public GeneratorChecker(List<PolyTree> trees, boolean isWholeSizes) {
        checkMap = new HashMap<>();
        maxSizeSoFar = 0;
        this.isWholeSizes = isWholeSizes;

        trees.forEach(this::add);
    }

    public void add(PolyTree tree) {
        int size = tree.getSize();

        if (size > maxSizeSoFar) {maxSizeSoFar = size;}

        Set<String> set = checkMap.get(size);

        if (set == null) {
            set = new TreeSet<>();
            checkMap.put(size, set);
        }

        set.add(tree.toString());
    }

    public List<Integer> getNumsForSizes() {
        List<Integer> ret = new ArrayList<>();
        for (int i = 1; i < (isWholeSizes ? maxSizeSoFar+1 : maxSizeSoFar); i++) {
            Set<String> set = checkMap.get(i);
            ret.add( set == null ? 0 : set.size() );
        }
        return ret;
    }

    public List<String> toNormalizedList() {
        List<String> ret = new ArrayList<>();

        // maxSizeSoFar nebereme, proto�e m��e b�t nekompletn�:
        for (int i = 1; i < (isWholeSizes ? maxSizeSoFar+1 : maxSizeSoFar); i++) {
            Set<String> set = checkMap.get(i);
            if (set != null) {
                for (String treeStr : set) {
                    ret.add(treeStr);
                }
            }
        }

        return ret;
    }

    public boolean check(List<PolyTree> trees, boolean isWholeSizes) {
        boolean isOk = true;


        GeneratorChecker tempChecker = new GeneratorChecker(trees, isWholeSizes);

        List<String> shouldBeList = toNormalizedList();
        List<String> resultList   = tempChecker.toNormalizedList();

        /*if (shouldBeList.size() != resultList.size()) {
            Log.it("GeneratorChecker: Sizes do not match: " + shouldBeList.size() +" != "+ resultList.size());
            return false;
        }*/

        int minLen = Math.min(shouldBeList.size() , resultList.size());

        Log.it("Num trees to be checked: " + minLen +" = min("+shouldBeList.size() +","+ resultList.size()+")" );

        for (int i = 0; i < minLen; i++) {
            if (!shouldBeList.get(i).equals(resultList.get(i))) {
                isOk = false;
                Log.it("  GeneratorChecker: Trees do not match:\n    " + shouldBeList.get(i) +"\n    "+ resultList.get(i));
            }
        }

        Log.it( "RESULT : " +(isOk ? "OK" : "KO") );


        return isOk;
    }




}
