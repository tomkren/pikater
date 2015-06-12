package cz.tomkren.helpers;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// nil pomocí null

public class Listek <T> {
    private T head;
    private Listek<T> tail;

    public Listek(T head, Listek<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> Listek<A> fromList(List<A> xs) {
        return F.list(xs).foldr(null, Listek::new);
    }

    public T getHead() {
        return head;
    }

    public Listek<T> getTail() {
        return tail;
    }

    @Override
    public String toString() {
        return "[ "+ toString_() +" ]";
    }

    private String toString_() {
        return head.toString() + (tail == null ? "" : " "+tail.toString_());
    }




    public static void main(String[] args) {
        Checker ch = new Checker();

        ch.it(Listek.fromList(Collections.emptyList()), "null");
        ch.it(Listek.fromList(Arrays.asList(1,2,3)), "[ 1 2 3 ]");


        ch.results();
    }
}
