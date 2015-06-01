package cz.tomkren.pikater;

import cz.tomkren.helpers.F;
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




        throw new TODO();
    }



    public static class ConverterError extends Exception {
        public ConverterError(String message) {super(message);}

        public static ConverterError unknownName(String name) {
            return new ConverterError("There is no BoxPrototype with name: "+name);
        }

        public static ConverterError unexpectedFail(String msg) {
            return new ConverterError("Unexpected fatal FAIL: "+(msg==null ? "N/A" : msg));
        }
    }

}
