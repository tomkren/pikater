package cz.tomkren.typewars;


import cz.tomkren.helpers.AA;

import java.util.*;
import java.util.function.Function;

public class Sub implements Function<Type,Type> {

    private Map<Integer,Type> table;
    private String failMsg;

    private Deque<AA<Type>> agenda; // používá se při konstrukci mgu

    public Sub() {
        table = new TreeMap<>(); // aby pak bylo popořadě, dá se ale nahradit HashMapou, pokud by to bylo poznatelně lepší, jen to pak ale rozbije testy.
        failMsg = null;
    }

    public Sub copy() {return new Sub(this);}

    private Sub(Sub subToCopy) {
        table = new TreeMap<>(subToCopy.table);
        failMsg = subToCopy.failMsg;
    }

    public void setFail(String error) {
        failMsg = error;
    }

    public boolean isFail() {
        return failMsg != null;
    }

    public String getFailMsg() {
        return failMsg;
    }

    @Override
    public Type apply(Type input) {
        return input.applySub(this);
    }

    public Type get(int varId) {
        return table.get(varId);
    }

    public void add(int varId, Type t) {
        table.put(varId, t);
    }

    public void removeAllBut(Set<Integer> varsSet) {

        Iterator<Map.Entry<Integer,Type>> it = table.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<Integer,Type> e = it.next();
            int varId = e.getKey();
            if (!varsSet.contains(varId)) {
                it.remove();
            }
        }
    }

    // TODO otestovat dot nějak pořádně generativně :

    /*composeSubsti :: Substi -> Substi -> Substi
      composeSubsti new old =
       let old' = fmap (applySubsti new) old
        in Map.filterWithKey (\ x t -> t /= TypVar x ) $ Map.union old' new */

    // Nejdřív jsem myslel že: results in updating this = f to f = g.f    ... where g.f(t) = g(f(t))
    // ALE : this = g ... g <- g.f ... g <- g.dot(f) ... tzn ta stará zůstane a ta nová dočasná se předělá na novou

    //nejdřív aplikujeme g na všechny typy v f
    // pak odfiltrujeme identický přiřazení tvaru x = x
    // sjednotíme (putAll se na to nehodí ten bijasuje obráceně) tu modifikovanou f' a g s tim že preferujeme z f'

    /** nejdřív aplikujeme g na všechny typy v f který putujem rovnou do g-čka ale jen když to neni identickej šit, v tom případě prostě smažeme co v g bylo
     *
     * @param f
     */

    public void dot(Sub f) {
        for(Map.Entry<Integer,Type> e : f.table.entrySet()) {
            int varId = e.getKey();
            Type updated = apply(e.getValue());
            if (updated instanceof TypeVar && varId == ((TypeVar)updated).getId()) {
                table.remove(varId);
            } else {
                table.put(varId,updated);
            }
        }
    }

    public static Sub dot(Sub g, Sub f) {
        Sub ret = g.copy();
        ret.dot(f);
        return ret;
    }


    public static Sub mgu(Type type1, Type type2) {
        return new Sub(type1, type2);
    }

    public Sub(Type type1, Type type2) {
        this();
        agenda = new ArrayDeque<>();
        agenda.add(new AA<>(type1, type2));

        while (!agenda.isEmpty() && !isFail()) {
            AA<Type> p = agenda.poll();
            mgu_process( p._1(), p._2() );
        }

        if (isFail()) {table = null;}
        agenda = null;
    }

    private void mgu_process(Type t1, Type t2) {

        if (t1 == t2 || areSameVars(t1,t2)) {return;}

        if (t1 instanceof TypeSym && t2 instanceof TypeSym) {
            if (!t1.toString().equals(t2.toString())) {setFail("Symbols " + t1 + " & " + t2 + " do not match.");}
            return;
        }

        if (t1 instanceof TypeVar) {mgu_process_TypeVar((TypeVar)t1, t2); return;}
        if (t2 instanceof TypeVar) {mgu_process_TypeVar((TypeVar)t2, t1); return;}

        if (t1 instanceof TypeTerm && t2 instanceof TypeTerm) {
            mgu_process_TypeTerm((TypeTerm)t1,(TypeTerm)t2);
            return;
        }

        setFail("Types " + t1 + " & " + t2 + " can not be unified.");
    }

    private void mgu_process_TypeVar(TypeVar var, Type type) {

        if (occurCheck(var,type)) {
            setFail("Occur check fail: " + var + " = " + type);
            return;
        }

        int varId = var.getId();

        // update ret by {var = type}
        for(Map.Entry<Integer,Type> entry : table.entrySet()) {
            entry.setValue( entry.getValue().applyMiniSub(varId, type) );
        }

        // update agenda by {var = type}
        for (AA<Type> p : agenda) {
            p.update( t -> t.applyMiniSub(varId, type) );
        }

        // insert {var = type} to ret
        add(varId, type);
    }

    private void mgu_process_TypeTerm(TypeTerm t1, TypeTerm t2) {
        List<Type> args1 = t1.getArgs();
        List<Type> args2 = t2.getArgs();

        int len = args1.size();
        if (len != args2.size()) {setFail("Type term length mismatch for " + t1 + " & " + t2 + "."); return;}

        for (int i = 0; i < len; i++) {
            agenda.add(new AA<>(args1.get(i), args2.get(i)));
        }
    }


    private static boolean areSameVars(Type t1, Type t2) {
        return t1 instanceof TypeVar && t2 instanceof TypeVar && ((TypeVar)t1).getId() == ((TypeVar)t2).getId();
    }


    private static boolean occurCheck(TypeVar var, Type t) {

        if (t instanceof TypeVar) {
            return ((TypeVar)t).getId() == var.getId();
        }

        if (t instanceof TypeSym) {
            return false;
        }

        if (t instanceof TypeTerm) {
            TypeTerm tt = (TypeTerm)t;

            for (Type arg : tt.getArgs()) {
                if (occurCheck(var,arg)) {return true;}
            }

            return false;
        }

        throw new Error("occurCheck : should be unreachable");
    }

    @Override
    public String toString() {

        if (isFail()) {
            return "fail";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(Map.Entry<Integer,Type> entry : table.entrySet()) {
            int id = entry.getKey();
            Type type = entry.getValue();

            sb.append(" ").append('x').append(id).append(" = ").append(type).append(" ,");
        }

        if (!table.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("}");

        return sb.toString();
    }

}
