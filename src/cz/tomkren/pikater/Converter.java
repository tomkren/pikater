package cz.tomkren.pikater;

import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.TypedDag;
import org.pikater.core.ontology.subtrees.batchdescription.*;

import cz.tomkren.helpers.TODO;

import javax.activation.DataSource;
import java.util.*;


/** Created by tom on 1.6.2015. */

public class Converter {

    private Map<String, BoxPrototype> nameToProto;

    private Map<Integer, BoxInstance> idToBox;

    public Converter(List<BoxPrototype> prototypes) {
        nameToProto = new HashMap<>();

        for (BoxPrototype prototype: prototypes) {
            nameToProto.put(prototype.getName(), prototype);
        }

        reset();
    }

    public Converter(BoxPrototype... prototypes) {
        this(Arrays.asList(prototypes));
    }

    private void reset() {
        idToBox = new HashMap<>();
    }


    public ComputationDescription convert(TypedDag dag) throws ConverterError {

        List<SimpleVertex> graph = new ArrayList<>();

        List<SimpleVertex> coreGraph = dag.toSimpleGraph();

        int minId = Integer.MAX_VALUE;
        int maxId = -Integer.MAX_VALUE;

        for (SimpleVertex sv : coreGraph) {
            int id = sv.getId();
            if (id > maxId) {maxId = id;}
            if (id < minId) {minId = id;}
        }

        SimpleVertex begin = coreGraph.get(0);
        SimpleVertex end = coreGraph.get(coreGraph.size()-1);


        SimpleVertex output = new SimpleVertex(maxId+2, "output", 1, 0, new ArrayList<>(0));

        SimpleVertex err    = new SimpleVertex(maxId+1, "err", 2, 1,
                F.mkSingleton(new SimpleVertex.LinkTarget(output.getId(),0)
                ));

        end.addTarget(new SimpleVertex.LinkTarget(err.getId(),0));

        SimpleVertex input  = new SimpleVertex(minId-1,"input", 0, 2,
                Arrays.asList(new SimpleVertex.LinkTarget(begin.getId(),0),
                              new SimpleVertex.LinkTarget(err.getId(),1)
                ));


        graph.add(input);
        graph.addAll(coreGraph);
        graph.add(err);
        graph.add(output);

        Log.list(graph);

        return convert(graph);
    }

    public ComputationDescription convert(List<SimpleVertex> graph) throws ConverterError {

        reset();

        // Vytvoøí vrcholy..
        for (SimpleVertex v : graph) {

            String name = v.getName();
            BoxPrototype prototype = nameToProto.get(name);

            if (prototype == null) {throw ConverterError.unknownName(name);}

            // Vytvoøí DataProcessing a pro výstupy List<DataSourceDescription>, který pak napojíme v druhým prùchodu
            BoxInstance box = new BoxInstance(v.getId(), prototype, v.getNumIn(), v.getNumOut());
            idToBox.put(v.getId(), box);
        }



        Map<Integer, List<DataSourceDescription>> inputSourcesForTargets = new HashMap<>();

        // Pøidáme hrany.. fáze I (pøedpøipravíme Mapu vstupù pro jednotlivé tárgety)
        for (SimpleVertex sourceVertex : graph) {

            int fromId = sourceVertex.getId();
            int fromPort = 0;
            BoxInstance fromBox = idToBox.get(fromId);

            for (SimpleVertex.LinkTarget t : sourceVertex.getTargets()) {
                if (t != null) {

                    int toId = t.getId();
                    int toPort = t.getPort();
                    BoxInstance toBox = idToBox.get(toId);

                    if (toBox == null) {throw ConverterError.absentBox(toId);}

                    List<DataSourceDescription> inputSources = inputSourcesForTargets.get(toId);
                    if (inputSources == null) {
                        int n = toBox.getNumIn();
                        inputSources = F.fill(n, null);
                        inputSourcesForTargets.put(toId, inputSources);
                    }

                    inputSources.set(toPort, fromBox.getOutputSource(fromPort));
                }
                fromPort ++;
            }
        }

        for (Map.Entry<Integer, List<DataSourceDescription>> e : inputSourcesForTargets.entrySet()) {

            int targetId = e.getKey();
            List<DataSourceDescription> inputSources = e.getValue();

            BoxInstance targetBox = idToBox.get(targetId);

            if (targetBox == null) {throw ConverterError.unexpectedFail("targetBox must be not null!");}

            targetBox.setInputSources(inputSources);
        }



        //Our requirements for the description are ready, lets create new computation description


        SimpleVertex lastVertex = graph.get(graph.size() - 1);
        BoxInstance lastBox = idToBox.get(lastVertex.getId());
        DataProcessing lastDataProcessing = lastBox.getDataProcessing();

        if (lastDataProcessing instanceof FileDataSaver) {

            List<FileDataSaver> roots = F.singleton((FileDataSaver)lastDataProcessing);
            ComputationDescription comDescription = new ComputationDescription();
            comDescription.setRootElements(roots);

            return comDescription;

        } else {
            throw ConverterError.wrongLast(lastDataProcessing);
        }

    }



    public static class ConverterError extends Exception {
        public ConverterError(String message) {super(message);}

        public static ConverterError unknownName(String name) {
            return new ConverterError("There is no BoxPrototype with name: "+name);
        }

        public static ConverterError absentBox(int id) {
            return new ConverterError("There is no BoxInstance with id: "+id);
        }

        public static ConverterError wrongLast(DataProcessing last) {
            return new ConverterError("Last DataProcessing must be instance of FileDataSaver: "+last);
        }

        public static ConverterError unexpectedFail(String msg) {
            return new ConverterError("Unexpected fatal FAIL: "+(msg==null ? "N/A" : msg));
        }
    }

}
