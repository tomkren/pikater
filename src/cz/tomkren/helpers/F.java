package cz.tomkren.helpers;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class F {

    public static <A,B,C> Function<A,C> dot(Function<B,C> g,  Function<A,B> f) {
        return x -> g.apply( f.apply(x) );
    }


    public static class ListResult <T> {

        private Stream<T> stream;

        public ListResult(Stream<T> stream) {
            this.stream = stream;
        }

        public ListResult(List<T> xs) {
            this.stream = xs.stream();
        }

        public List<T> get() {
            return stream.collect(Collectors.toList());
        }

        public <B> ListResult<B> map(Function<? super T,B> f) {
            return new ListResult<>( stream.map(f) );
        }

        public ListResult<T> filter(Predicate<? super T> f) {
            stream = stream.filter(f);
            return this;
        }

        public ListResult<T> sort() {
            stream = stream.sorted();
            return this;
        }

        public ListResult<T> sort(Function<T,Double> f) {
            stream = stream.sorted( (o1, o2) -> Double.compare( f.apply(o1) , f.apply(o2) ) );
            return this;
        }

        public ListResult<T> sort_(Function<T, Integer> f) {
            stream = stream.sorted( (o1, o2) -> Integer.compare( f.apply(o1) , f.apply(o2) ) );
            return this;
        }

        public <B> B foldr(B z, BiFunction<T,B,B> f) {
            List<T> xs = get();
            ListIterator<T> li = xs.listIterator(xs.size());
            B acc = z;
            while(li.hasPrevious()) {acc = f.apply(li.previous(),acc);}
            return acc;
        }

        public T max() {
            List<T> xs = get();
            if (xs.isEmpty()) { throw new Error("max() of empty list...");}
            T head = xs.get(0);
            if (!(head instanceof Comparable)) {throw new Error("max() of non-comparable");}
            Comparable<T> best = (Comparable<T>) head;
            for (T x : xs) {best = best.compareTo(x) > 0 ? best : (Comparable<T>) x;}
            return (T) best;
        }

        public T min() {
            List<T> xs = get();
            if (xs.isEmpty()) { throw new Error("min() of empty list...");}
            T head = xs.get(0);
            if (!(head instanceof Comparable)) {throw new Error("min() of non-comparable");}
            Comparable<T> best = (Comparable<T>) head;
            for (T x : xs) {best = best.compareTo(x) < 0 ? best : (Comparable<T>) x;}
            return (T) best;
        }

        @Override
        public String toString() {
            return "<"+ get().toString() +">";
        }
    }

    public static <T> ListResult<T> list(List<T> xs) {
        return new ListResult<>(xs);
    }

    public static <T> ListResult<T> list(T... xs) {
        return new ListResult<>(Arrays.asList(xs));
    }

    public static <T> void each(List<T> xs, Consumer<? super T> f) {
        xs.stream().forEach(f);
    }

    public static <A,B> List<B> map(Function<A,B> f,  List<A> xs) {
        return xs.stream().map(f).collect(Collectors.toList());
    }



    public static <A,B> List<B> map(List<A> xs, Function<A,B> f) {
        return xs.stream().map(f).collect(Collectors.toList());
    }

    public static <A,B> List<B> map(A[] xs, Function<A,B> f) {
        return Arrays.asList(xs).stream().map(f).collect(Collectors.toList());
    }


    public static void adjustArray(int[] xs, Function<Integer,Integer> f) {
        int len = xs.length;
        for (int i = 0; i < len; i++) {
            xs[i] = f.apply(xs[i]);
        }
    }

    public static <A> A reduce(List<A> xs, BiFunction<A,A,A> f) {
        if (xs.isEmpty()) {return null;}
        Iterator<A> it = xs.iterator();
        A acc = it.next();
        while (it.hasNext()) {
            acc = f.apply(acc,it.next());
        }
        return acc;
    }

    public static <T> List<T> subList(List<T> xs, int from, int len) {
        List<T> ret = new ArrayList<>(len);
        for (int i=0; i<len; i++) {
            ret.add( xs.get(from+i) );
        }
        return ret;
    }

    public static <T> List<T> singleton(T x) {
        List<T> ret = new ArrayList<>(1);
        ret.add(x);
        return ret;
    }


    public static <T> List<T> sort(List<T> xs) {
        return xs.stream().sorted().collect(Collectors.toList());
    }

    public static <T> List<T> sort(List<T> xs, Function<T,Double> f) {
        return xs.stream().sorted( (a,b)-> Double.compare(f.apply(a),f.apply(b)) ).collect(Collectors.toList());
    }


    private static class ElemInfo<T> {
        public final int i;
        public final T el;
        public ElemInfo(int i, T el) {this.i = i; this.el = el;}
    }

    public static <T> Function<Integer,Integer> getRearrangeFun(List<T> xs, Function<T,Double> f) {
        int n = xs.size();
        List<ElemInfo<T>> infos = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            infos.add( new ElemInfo<>(i,xs.get(i))  );
        }
        final List<ElemInfo<T>> sortedInfos = sort( infos , info -> f.apply(info.el) );
        return j -> sortedInfos.get(j).i;
    }

    public static <T> List<T> rearrange(List<T> xs, Function<Integer,Integer> newToOldIndex) {
        int n = xs.size();
        List<T> ret = new ArrayList<>(n);

        for (int i=0; i<n; i++) {
            ret.add( xs.get(newToOldIndex.apply(i)) );
        }

        return ret;
    }

    public static <T,A> List<T> rearrange(List<T> xs, List<A> byList , Function<A,Double> sortFun ) {
        return rearrange(xs, getRearrangeFun(byList,sortFun) );
    }

    public static <T> List<T> rearrange(List<T> xs, List<Double> byList ) {
        return rearrange(xs, getRearrangeFun(byList,x->x) );
    }


    public static <T> List<T> filter(List<T> xs, Predicate<T> f) {
        return xs.stream().filter(f).collect(Collectors.toList());
    }

    public static class Partition<T> {
        private final List<T> ok;
        private final List<T> ko;

        public Partition(List<T> xs, Predicate<T> isOK) {
            ok = new ArrayList<>();
            ko = new ArrayList<>();
            for (T x : xs) {
                (isOK.test(x) ? ok : ko).add(x);
            }
        }

        public List<T> getOK() {return ok;}
        public List<T> getKO() {return ko;}
    }


    public static <T> T randomElement(List<T> xs, Random r) {
        return xs.get(r.nextInt(xs.size()));
    }

    public static <T> List<T> nTimes(int n, Supplier<T> f) {
        List<T> ret = new ArrayList<>(n);
        for (int i=0; i<n; i++) {
            ret.add(f.get());
        }
        return ret;
    }

    public static double sum(List<Double> xs) {
        double sum = 0;
        for (Double x : xs) {
            sum += x;
        }
        return sum;
    }

    public static int sumInt(List<Integer> xs) {
        int sum = 0;
        for (int x : xs) {
            sum += x;
        }
        return sum;
    }

    public static <A> List<A> take(int n, List<A> xs) {
        int n_ = Math.min(n, xs.size());
        List<A> ret = new ArrayList<>(n_);

        Iterator<A> it = xs.iterator();
        int i = 0;
        while (i<n_ && it.hasNext()) {
            ret.add( it.next() );
            i++;
        }

        return ret;
    }

    // todo otestovat
    public static <A> List<A> drop(int n, List<A> xs) {
        return xs.subList(n, xs.size());
    }

    public static <A> List<A> mkSingleton(A x) {
        List<A> ret = new ArrayList<>(1);
        ret.add(x);
        return ret;
    }


    public static void main(String[] args) {

        Checker check = new Checker(424242L);

        List<Integer> xs = Arrays.asList(1,2,3,4,5,6);
        check.it(xs, "[1, 2, 3, 4, 5, 6]");
        check.it(take(2,xs), "[1, 2]" );
        check.it(take(0,xs), "[]" );
        check.it(take(200,xs), "[1, 2, 3, 4, 5, 6]" );
        check.it(xs);

        check.it( nTimes(6,()->7) , "[7, 7, 7, 7, 7, 7]" );
        check.it( nTimes(6,()->check.getRandom().nextInt(10)) , "[0, 3, 3, 6, 5, 5]" );


        check.it( list(xs) , "<[1, 2, 3, 4, 5, 6]>" );
        check.it( list(xs).get() , "[1, 2, 3, 4, 5, 6]" );
        check.it( list(xs).max() , "6" );
        check.it( list(xs).min() , "1" );

        check.eq(list(xs).foldr(0, (x, y) -> x + y), sumInt(xs) );

        check.eq( list(xs).foldr(-Integer.MAX_VALUE, (x,acc)-> x>acc ? x : acc ) , Collections.max(xs) );
        check.eq( list(xs).foldr( Integer.MAX_VALUE, (x,acc)-> x<acc ? x : acc ) , Collections.min(xs) );

        check.eq( list(xs).foldr(-Integer.MAX_VALUE, Math::max ) , Collections.max(xs) );
        check.eq( list(xs).foldr( Integer.MAX_VALUE, Math::min) , Collections.min(xs) );

        check.it(list(xs).filter(x -> x % 2 == 0).get(), "[2, 4, 6]");

        check.it( list(9,8,3,2,1,2,6,5,0,7,4).sort_(x -> -x) , "<[9, 8, 7, 6, 5, 4, 3, 2, 2, 1, 0]>" );
        check.it( list(9,8,3,2,1,4,2,6,5,0,7).sort() , "<[0, 1, 2, 2, 3, 4, 5, 6, 7, 8, 9]>" );
        check.it( list(9.,8.,3.,2.,1.,4.,2.,6.,5.,0.,7.).sort(x->x).get() , "[0.0, 1.0, 2.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0]" );

        List<Double> xs1 = Arrays.asList(9.,8.,3.,2.,1.,4.,2.,6.,5.,0.,7.);
        List<Character> xs2 = Arrays.asList('J','I','D','C','B','E','C','G','F','A','H');

        check.it(xs1, "[9.0, 8.0, 3.0, 2.0, 1.0, 4.0, 2.0, 6.0, 5.0, 0.0, 7.0]");
        check.it(list(xs1).sort().get(), "[0.0, 1.0, 2.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0]");
        check.it(xs1, "[9.0, 8.0, 3.0, 2.0, 1.0, 4.0, 2.0, 6.0, 5.0, 0.0, 7.0]");

        check.it( sort(xs1) , "[0.0, 1.0, 2.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0]" );
        check.it( sort(xs1,x->-x) , "[9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 2.0, 1.0, 0.0]" );

        Function<Integer,Integer> rear1 = getRearrangeFun(xs1,x->x);
        check.it( rear1.apply(0) , "9" );
        check.it( rear1.apply(1) , "4" );
        check.it( rearrange(xs2, rear1), "[A, B, C, C, D, E, F, G, H, I, J]" );
        check.it( rearrange(xs2, xs1, x->x), "[A, B, C, C, D, E, F, G, H, I, J]" );
        check.it( rearrange(xs2, xs1), "[A, B, C, C, D, E, F, G, H, I, J]" );


        check.it( F.map(new Integer[]{1, 2, 3}, x -> x + 1) , "[2, 3, 4]" );

        check.it( F.reduce(new ArrayList<Integer>(), (x, y) -> x + y) , "null" );
        check.it( F.reduce(Arrays.asList(1, 2, 3, 4, 5), (x, y) -> x + y) , "15" );


        check.results();
    }
}
