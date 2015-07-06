package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.SmartSym;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.Types;

import java.util.List;
import java.util.Random;

/** Created by tom on 7. 6. 2015 */

public class Query {

    private final Type type;
    private final int treeSize;

    private SmartSym dadSym;
    private Integer  sonIndexInDad;

    private QuerySolver solver;

    //private SmartLib lib;

    public Query(Type type, int treeSize) {
        this.type = type;
        this.treeSize = treeSize;
        dadSym = null;
        sonIndexInDad = null;
    }

    public Query(String type, int treeSize) {
        this(Types.parse(type), treeSize);
    }

    public Query(Type type, int treeSize, Query dadQuery, SmartSym dadSym, int sonIndexInDad) {
        this(type,treeSize);
        setSolver(dadQuery.getSolver());
        setDadSymAndSonIndex(dadSym, sonIndexInDad);
    }

    public Query(Sub sub, Query sonQuery) {
        this(sub.apply(sonQuery.type), sonQuery.treeSize);
        setSolver(sonQuery.getSolver());
        setDadSymAndSonIndex( sonQuery.dadSym, sonQuery.sonIndexInDad );
    }
    public void setSolver(QuerySolver solver) {this.solver = solver;}

    public QuerySolver getSolver() {return solver;}


    public void setDadSymAndSonIndex(SmartSym dadSym, int sonIndexInDad) {
        this.dadSym = dadSym;
        this.sonIndexInDad = sonIndexInDad;
    }
    public SmartSym getDadSym() {
        return dadSym;
    }
    public Integer getSonIndexInDad() {
        return sonIndexInDad;
    }


    public Random getRand() {
        return solver.getRand();
    }





    /*public QueryResult_old query(Query q) {
        return getSolver().query_old(q);
    }*/

    public List<SmartSym> getAllSyms() {
        return solver.getLib().getSyms();
    }

    @Override
    public String toString() {
        return type.toString() + " ; " + treeSize ;
    }
    public Type getType() {return type;}


    public int getTreeSize() {return treeSize;}

    /*public void setLib(SmartLib lib) {this.lib = lib;}
    public SmartLib getLib() {return lib;}*/

}
