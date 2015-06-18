package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.PolyTree;

import java.util.*;

/** Created by tom on 19.6.2015. */

// TODO !!! dodìlat

public class GeneratorChecker {

    private Map<Integer,Set<String>> checkMap;
    private int maxSizeSoFar;

    public GeneratorChecker(List<PolyTree> trees) {
        checkMap = new HashMap<>();
        maxSizeSoFar = 0;

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




}
