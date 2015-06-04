package cz.tomkren.typewars;


import com.google.common.base.Joiner;
import cz.tomkren.helpers.Comb0;
import cz.tomkren.helpers.AA;
import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.TriFun;
import cz.tomkren.kutil2.items.Int2D;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TypedDag {

    private List<Vertex> ins, outs;
    private Type inType, outType;
    private int width, height;


    public TypedDag(String name, Type inType, Type outType) {

        this.inType = inType;
        this.outType = outType;

        Vertex v = new Vertex(name);

        ins  = makeInterfaceList(inType, v);
        outs = makeInterfaceList(outType, v);

        width  = 1;
        height = 1;
    }

    public TypedDag copy() {
        return new TypedDag(this);
    }

    public TypedDag(TypedDag oldDag) {
        Map<Integer, Vertex> oldToOld = new HashMap<>();
        Map<Integer, Vertex> oldToNew = new HashMap<>();

        oldDag.forEachVertex(v -> {
            oldToOld.put(v.getId(), v);
            oldToNew.put(v.getId(), v.pseudoCopy());
        });

        for (Map.Entry<Integer,Vertex> e : oldToNew.entrySet()) {
            int   oldId = e.getKey();
            Vertex vNew = e.getValue();
            Vertex vOld = oldToOld.get(oldId);

            for (AB<Vertex,Integer> oldSuccWithPort : vOld.getSuccessorsWithPorts()) {
                Vertex v = oldSuccWithPort._1();
                int port = oldSuccWithPort._2();
                vNew.addSuccessor( oldToNew.get(v.getId()) , port );
            }
        }

        inType  = oldDag.inType;
        outType = oldDag.outType;

        width  = oldDag.width;
        height = oldDag.height;

        ins = new ArrayList<>();
        outs = new ArrayList<>();

        oldDag. ins.forEach(vOld ->  ins.add(oldToNew.get(vOld.getId())));
        oldDag.outs.forEach(vOld -> outs.add(oldToNew.get(vOld.getId())));

    }

    private List<Vertex> makeInterfaceList(Type type, Vertex v) {
        List<Vertex> ret = new ArrayList<>();
        int arity = getArity(type);
        for (int i = 0; i < arity; i++) {
            ret.add(v);
        }
        return ret;
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public int getPxWidth()  {return width  * Vertex.X_1SIZE;}
    public int getPxHeight() {return height * Vertex.Y_1SIZE;}

    private int getArity(Type type) {

        if (type instanceof TypeTerm) {
            List<Type> args = ((TypeTerm)type).getArgs();

            if (args.size() < 3) {
                throw new Error("Type must have at least 3 parts: "+type);
            }

            Type op   = args.get(0);
            Type arg1 = args.get(1);
            Type arg2 = args.get(2);
            if (Types.PAIR.equals(op)) {
                return getArity(arg1) + getArity(arg2);
            } else if (Types.VECTOR.equals(op)) {
                return getArity(arg1) * Types.fromNat(arg2);
            } else {
                throw new Error("Unsupported type constructor: "+op);
            }
        } else {
            return 1;
        }
    }


    public static TypedDag para(TypedDag dag1, TypedDag dag2) {
        return dag1.copy().para(dag2.copy());
    }

    public static TypedDag seri(TypedDag dag1, TypedDag dag2) {
        return dag1.copy().seri(dag2.copy());
    }

    public static TypedDag dia(TypedDag dag1, TypedDag dag2, TypedDag dag3) {
        return dag1.copy().seri(dag2.copy()).seri(dag3.copy());
    }

    public static TypedDag split(TypedDag dag, MyList dagList) {
        return dag.copy().seri( fromMyList(dagList) );
    }

    public static TypedDag fromMyList(MyList dagList) {
        List<TypedDag> dags = dagList.toList(TypedDag.class);
        return paraList(dags);
    }

    public static TypedDag paraList(List<TypedDag> dags) {
        if (dags.isEmpty()) {return null;}
        Iterator<TypedDag> it = dags.iterator();
        TypedDag acc = it.next().copy();
        while (it.hasNext()) {
            acc = acc.para(it.next().copy());
        }
        return acc;
    }


    public TypedDag split(MyList dagList) {
        seri(fromMyList_noCopy(dagList) );
        return this;
    }

    public static TypedDag fromMyList_noCopy(MyList dagList) {
        List<TypedDag> dags = F.map(dagList.toList(), o->(TypedDag)o );
        return paraList_noCopy(dags);
    }

    public static TypedDag paraList_noCopy(List<TypedDag> dags) {
        return F.reduce(dags, (x,y)->x.para(y));
    }

    public TypedDag para(TypedDag dag2) {

        parallelMove(dag2);

        ins .addAll(dag2.ins);
        outs.addAll(dag2.outs);

        inType = new TypeTerm(Types.PAIR, inType, dag2.inType);
        outType = new TypeTerm(Types.PAIR, outType, dag2.outType);
        return this;
    }


    public TypedDag dia(TypedDag dag2, TypedDag dag3) {
        seri(dag2);
        seri(dag3);
        return this;
    }

    public TypedDag seri(TypedDag dag2) {


        if (getArity(outType) != getArity(dag2.inType)) { // moc silný, chcem napojovat ty se stejnou aritou (!outType.equals(dag2.inType)) {

            throw new Error("TypedDag.seri : incompatible types " + outType + " & " + dag2.inType);
        }

        List<Vertex> ins2 = dag2.ins;
        int n = outs.size();

        if (n != ins2.size()) {
            throw new MergeException("Serial merge needs outs.size() == dag.ins.size(),"+
                    " but it was outs.size(): "+outs.size()+" dag.ins.size(): "+ins2.size());
        }

        serialMove(dag2);

        for (int i = 0; i < n; i++) {
            outs.get(i).addSuccessor( ins2.get(i) );
        }

        outs = dag2.outs;


        outType = dag2.outType;
        return this;
    }




    private void serialMove(TypedDag dag2) {
        dag2.move(0, height);
        height += dag2.height;

        double xMove = 0.5 * Math.abs(width - dag2.width);

        if (width < dag2.width) {
            move(xMove, 0);
        } else if (width > dag2.width) {
            dag2.move(xMove,0);
        }

        width = Math.max(width, dag2.width);
    }

    private void parallelMove(TypedDag dag2) {
        dag2.move(width, 0);
        width += dag2.width;

        double yMove = 0.5 * Math.abs(height - dag2.height);

        if (height < dag2.height) {
            move(0,yMove);
        } else if (height > dag2.height) {
            dag2.move(0,yMove);
        }

        height = Math.max(height, dag2.height);
    }

    public void move(double dx, double dy) {
        forEachVertex(v -> {
            v.moveX(dx);
            v.moveY(dy);
        });
    }


    public void forEachVertex(Consumer<Vertex> f) {
        Set<Vertex> vSet = new HashSet<>();
        Set<Vertex> processed = new HashSet<>();
        vSet.addAll(ins);
        while (!vSet.isEmpty()) {
            Set<Vertex> vSet_new = new HashSet<>();
            for (Vertex v1 : vSet) {
                f.accept(v1);
                processed.add(v1);
                v1.getSuccessors().forEach(v2 -> {
                    if (!processed.contains(v2)) {
                        vSet_new.add(v2);
                    }
                });
            }
            vSet = vSet_new;
        }
    }



    public static CodeNode mkAtomicDagNode(String name, String inType, String outType) {
        return mkAtomicDagNode(name, Types.parse(inType), Types.parse(outType));
    }

    public static CodeNode mkAtomicDagNode(String name, Type inType, Type outType) {
        Comb0 comb = haxTypeInput -> {
            Type t = (Type) haxTypeInput.get(0);
            AA<Type> p = getBoxInOutTypes(t);
            return new TypedDag(name, p._1(), p._2());
        };
        ProtoNode protoNode = new ProtoNode(name, inType + " => " + outType);
        return new CodeNode(protoNode, comb);
    }

    public static AA<Type> getBoxInOutTypes(Type type) {
        if (type instanceof TypeTerm) {
            TypeTerm tt = (TypeTerm) type;
            List<Type> args = tt.getArgs();
            if (args.size() == 3 && Types.BOX_ARROW.equals(args.get(1))) {
                return new AA<>(args.get(0),args.get(2));
            }
        }
        throw new Error("Type "+type+" was expected to be box type!");
    }

    public static CodeNode mkDagOperationNode(String name, Comb0 comb, String outType, String... inTypes) {
        ProtoNode protoNode = new ProtoNode(name, outType, inTypes);
        return new CodeNode(protoNode, comb);
    }


    /*public static CodeNode mkCodeNode(String... args) {
        return mkCodeNode(args);
    }*/



    public static CodeNode mkSplit(String name, String outType, String inType1, String inType2) {
        Comb0 comb = xs -> ((TypedDag)xs.get(0)).split((MyList)xs.get(1));
        return mkDagOperationNode(name, comb, outType, inType1, inType2);
    }

    public static CodeNode mkDia(String name, String outType, String inType1, String inType2, String inType3) {
        Comb0 comb = xs -> ((TypedDag)xs.get(0)).dia((TypedDag) xs.get(1), (TypedDag) xs.get(2));
        return mkDagOperationNode(name, comb, outType, inType1, inType2, inType3);
    }


    public static CodeNode mkCodeNode(String... args) {
        if (args.length < 2) {throw new Error("Too few arguments.");}

        String name    = args[0].trim();
        String outType = args[1].trim();

        if (args.length == 2) {
            String[] ps = outType.split("=>");
            if (ps.length != 2) {throw new Error("Atom type must have 2 parts (split by =>).");}
            return mkAtomicDagNode(name, ps[0].trim(), ps[1].trim());
        } else {
            return mkDagOperationNode(name, outType, Arrays.copyOfRange(args,2,args.length) );
        }
    }

    public static CodeNode mkDagOperationNode(String name, String outType, String... inTypes) {
        int n = inTypes.length;
        if (n == 2) {
            return mkDagOperationNode(name, Comb0.mkTypedDagFun2(mkBiFun(name)), outType, inTypes);
        } else if (n == 3) {
            return mkDagOperationNode(name, Comb0.mkTypedDagFun3(mkTriFun(name)), outType, inTypes);
        }

        throw new Error("Unsupported arity "+n+".");
    }





    public static BiFunction<TypedDag,TypedDag,TypedDag> mkBiFun(String name) {
        try {
            Method method = TypedDag.class.getMethod(name, TypedDag.class);
            return (x,y)-> {
                try {
                    Object ret = method.invoke(x, y);
                    return (TypedDag) ret;
                }
                catch (IllegalArgumentException e)  {throw new Error("IllegalArgumentException !");}
                catch (IllegalAccessException e)    {throw new Error("IllegalAccessException !");}
                catch (InvocationTargetException e) {
                    throw new Error("InvocationTargetException! : "+e.getCause().getMessage());}
            };
        }
        catch (SecurityException e)     {throw new Error("SecurityException !");}
        catch (NoSuchMethodException e) {throw new Error("NoSuchMethodException !");}
    }

    public static TriFun<TypedDag,TypedDag,TypedDag,TypedDag> mkTriFun(String name) {
        try {
            Method method = TypedDag.class.getMethod(name, TypedDag.class, TypedDag.class);
            return (x,y,z)-> {
                try {
                    Object ret = method.invoke(x, y, z);
                    return (TypedDag) ret;
                }
                catch (IllegalArgumentException e)  {throw new Error("IllegalArgumentException !");}
                catch (IllegalAccessException e)    {throw new Error("IllegalAccessException !");}
                catch (InvocationTargetException e) {
                    throw new Error("InvocationTargetException! : " +e.getCause().getMessage());}
            };
        }
        catch (SecurityException e)     {throw new Error("SecurityException !");}
        catch (NoSuchMethodException e) {throw new Error("NoSuchMethodException !");}
    }


    public String toKutilXML(Int2D pos) {
        StringBuilder sb = new StringBuilder();

        forEachVertex( v -> {
            v.toKutilXML(sb, pos);
            sb.append('\n');
        });

        return sb.toString();
    }

    @Override
    public String toString() {
        return toKutilXML(new Int2D(0,0));
    }

    public String toOldSchoolString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        List<Vertex> vsList = new ArrayList<>();
        Set<Vertex> vsSet   = new HashSet<>();


        vsList.addAll(ins);

        while (!vsList.isEmpty()) {

            sb.append(Joiner.on(' ').join(vsList)).append('\n');

            sb2.append(Joiner.on(' ').join(F.map(vsList, Vertex::successorsStr))).append('\n');


            List<Vertex> temp = new ArrayList<>();

            for (Vertex v1 : vsList) {
                for (Vertex v2 : v1.getSuccessors()) {
                    if (!vsSet.contains(v2)) {
                        temp.add(v2);
                        vsSet.add(v2);
                    }
                }
            }

            vsList = temp;
            vsSet  = new HashSet<>();

        }

        return sb.toString() +"\n"+ sb2.toString() ; // +"\n"+toKutilXML(new Int2D(0,0));
    }




    public static class MergeException extends RuntimeException {
        public MergeException(String message) {
            super(message);
        }
    }

}