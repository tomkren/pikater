package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.util.List;

/** Created by tom on 7. 6. 2015 */

public class Query {

    private final Type type;
    private final int treeSize;

    private QuerySolver solver;

    public Query(Type type, int treeSize) {
        this.type = type;
        this.treeSize = treeSize;
    }

    public Query(Sub sub, Query sonQuery) {
        this(sub.apply(sonQuery.type), sonQuery.treeSize);
        setSolver(sonQuery.getSolver());
    }

    public void setSolver(QuerySolver solver) {this.solver = solver;}
    public QuerySolver getSolver() {return solver;}

    public QueryResult query(Query q) {
        return getSolver().query(q);
    }

    public List<SmartSym> getAllSyms() {
        return solver.getLib().getSyms();
    }

    @Override
    public String toString() {
        return type.toString() + " ; " + treeSize ;
    }

    public Type getType() {return type;}
    public int getTreeSize() {return treeSize;}

}
