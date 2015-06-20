package cz.tomkren.typewars.reusable;

import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Listek;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/** Created by user on 19. 6. 2015. */

public class AndGadget {

    private Query dadQuery;
    private SmartSym sym;
    private Listek<Query> sonQueries;
    private Sub sub;

    private Map<Type,BigInteger> nums;
    private BigInteger num;

    public AndGadget(Query dadQuery, SmartSym sym, Listek<Query> sonQueries, Sub sub) {
        this.dadQuery = dadQuery;
        this.sym = sym;
        this.sonQueries = sonQueries;
        this.sub = sub;

        nums = computeNums(sonQueries, sub, BigInteger.ONE);
        num = sum(nums);
    }

    private Map<Type,BigInteger> computeNums(Listek<Query> queries, Sub sub, BigInteger acc) {

        if (F.isZero(acc)) {throw new Error("acc should never be zero!");}

        if (queries == null) {
            Type originalType = dadQuery.getType();
            Type rootType = sub.apply(originalType);

            Map<Type,BigInteger> ret = new HashMap<>();
            ret.put(rootType, acc);
            return ret;
        }

        Query sonQuery = new Query(sub, queries.getHead());
        QueryResult sonQueryResult = getSolver().query(sonQuery);

        Map<Type,BigInteger> ret = new HashMap<>();

        if (F.isZero(sonQueryResult.getNum())) {return ret;}

        for (Map.Entry<Type,BigInteger> e : sonQueryResult.getNums().entrySet()) {

            Type moreSpecificType = e.getKey();
            BigInteger numSonTrees = e.getValue();

            Sub sonSpecificSub = Sub.mgu( moreSpecificType, sonQuery.getType() );

            Sub newSub = Sub.dot(sonSpecificSub,sub);
            BigInteger newAcc = numSonTrees.multiply(acc);

            Map<Type,BigInteger> subRet = computeNums(queries.getTail(), newSub, newAcc);
            mergeAllByAdd(ret,subRet);
        }

        return ret;
    }

    public Map<Type, BigInteger> getNums() {return nums;}
    public BigInteger getNum() {return num;}


    public static <K> BigInteger sum(Map<K,BigInteger> mapa) {
        BigInteger sum = BigInteger.ZERO;
        for (Map.Entry<K,BigInteger> e : mapa.entrySet()) {
            sum = sum.add( e.getValue() );
        }
        return sum;
    }

    public static <K> void mergeAllByAdd(Map<K,BigInteger> target, Map<K,BigInteger> source) {
        mergeAll(target, source, BigInteger::add);
    }

    public static <K,V> void mergeAll(Map<K,V> target, Map<K,V> source, BiFunction<? super V, ? super V, ? extends V> f) {
        for (Map.Entry<K,V> e : source.entrySet()) {
            target.merge(e.getKey(), e.getValue(), f);
        }
    }


    public QuerySolver getSolver() {
        return dadQuery.getSolver();
    }


}
