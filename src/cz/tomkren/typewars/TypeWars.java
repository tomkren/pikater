package cz.tomkren.typewars;


import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;

public class TypeWars {

    public static void main(String[] args) {
        Checker check = new Checker();

        // todo asi sem přesunout to co je v Types ty testy...

        Log.it("Hello world!");

        TypeSym int_ = new TypeSym("Int");
        TypeSym ar = new TypeSym("->");
        TypeVar x1 = new TypeVar(1);
        TypeVar x2 = new TypeVar(2);

        TypeTerm t1 = new TypeTerm(ar,x1,x2);
        TypeTerm t2 = new TypeTerm(ar,x1,int_);
        TypeTerm t3 = new TypeTerm(ar, t1, t2);

        check.it(t1, "-> x1 x2");
        check.it(t2, "-> x1 Int");
        check.it(t3, "-> -> x1 x2 -> x1 Int");

        check.it( Sub.mgu(t2, t1) );

        check.results();
    }

}
