package cz.tomkren.typewars;


import cz.tomkren.helpers.Listek;

import java.util.List;

public class SubtreePos {

    private final Listek<Integer> path;

    private SubtreePos() {
        path = null;
    }

    private SubtreePos(int sonIndex, SubtreePos subtreePosInSon) {
        path = new Listek<>(sonIndex, subtreePosInSon.path);
    }

    private SubtreePos(Listek<Integer> path) {
        this.path = path;
    }

    public boolean isRoot() {
        return path == null;
    }

    public Integer getSonIndex() {
        if (path == null) {throw new Error("Empty path has no don index!");}
        return path.getHead();
    }

    public SubtreePos getTail() {
        if (path == null) {throw new Error("Empty path has tail!");}
        return new SubtreePos(path.getTail());
    }

    public List<Integer> getPath() {
        return Listek.toList(path);
    }

    public static SubtreePos root() {
        return new SubtreePos();
    }

    public static SubtreePos step(int sonIndex, SubtreePos subtreePosInSon) {
        return new SubtreePos(sonIndex, subtreePosInSon);
    }

    @Override
    public String toString() {
        return Listek.toList(path).toString();
    }
}
