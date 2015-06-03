package cz.tomkren.helpers;

// modifikovatelný Pár
public class AB<A,B> {

    private A a;
    private B b;

    public AB(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A _1() {return a;}
    public B _2() {return b;}

    public void set_1(A a) {this.a = a;}
    public void set_2(B b) {this.b = b;}

    @Override
    public String toString() {
        return "<" + a +","+ b + ">";
    }
}
