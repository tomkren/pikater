package cz.tomkren.helpers;

import java.util.function.Function;

// modifikovatelný Pár stejného typu
public class AA<A> {

    private A a1;
    private A a2;

    public AA(A a1, A a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

    public A _1() {return a1;}
    public A _2() {return a2;}

    public void set1(A a1) {this.a1 = a1;}
    public void set2(A a2) {this.a2 = a2;}

    public void update(Function<A,A> f) {
        a1 = f.apply(a1);
        a2 = f.apply(a2);
    }

    @Override
    public String toString() {
        return "<" + a1 +","+ a2 + ">";
    }
}
