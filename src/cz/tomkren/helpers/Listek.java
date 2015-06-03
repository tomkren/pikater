package cz.tomkren.helpers;

public class Listek <T> {
    private T head;
    private Listek<T> tail;

    public Listek(T head, Listek<T> tail) {
        this.head = head;
        this.tail = tail;
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
}
