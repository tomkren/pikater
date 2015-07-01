package cz.tomkren.typewars;


import cz.tomkren.helpers.Listek;

import java.util.List;

public class SubtreePos {

    private final Listek<Integer> path;
    private final Type type;

    private SubtreePos(Type type) {
        path = null;
        this.type = type;
    }

    private SubtreePos(int sonIndex, SubtreePos subtreePosInSon) {
        path = new Listek<>(sonIndex, subtreePosInSon.path);
        type = subtreePosInSon.getType();
    }

    private SubtreePos(Listek<Integer> path, Type type) {
        this.path = path;
        this.type = type;
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
        return new SubtreePos(path.getTail(), type);
    }

    public List<Integer> getPath() {
        return Listek.toList(path);
    }

    public Type getType() {
        return type;
    }

    public static SubtreePos root(Type type) {
        return new SubtreePos(type);
    }

    public static SubtreePos step(int sonIndex, SubtreePos subtreePosInSon) {
        return new SubtreePos(sonIndex, subtreePosInSon);
    }

    @Override
    public String toString() {
        return Listek.toList(path).toString();
    }
}
