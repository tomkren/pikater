package cz.tomkren.helpers;


public interface TriFun<A,B,C,D> {
    D apply(A x, B y, C z);
}
