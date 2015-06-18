package cz.tomkren.kutil2;

import com.google.common.base.Joiner;
import cz.tomkren.helpers.Log;
import cz.tomkren.kutil2.core.Kutil;
import cz.tomkren.kutil2.core.XmlLoader;

/**
 * KUTIL V2
 * V2 started by Tomáš Křen on 31. 10. 2014.
 */

public class KutilMain {

    private static final String VERSION = "alpha 2.0.2";
    private static final String defaultXmlFile = "funwars.xml"; // "minimal.xml"; // "minimal_problem.xml"; //  "fun.xml"; //  // todo udělat spíš nějakej "config file"

    public static void main(String[] args) {
        Log.it("KUTIL (v "+VERSION+"), hello!\n");

        boolean isArgsInput = args.length > 0;
        XmlLoader.LoadMethod loadMethod = isArgsInput ? XmlLoader.LoadMethod.FILE : XmlLoader.LoadMethod.RESOURCE ;
        String loadInput = isArgsInput ? args[0] : defaultXmlFile;

        new Kutil().start(loadMethod, loadInput);

        Log.it("Good bye!");
    }

    public static String wrapLibInKutil(String[] lib, String goalType, String numTrees) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kutil>\n" +
                "    <o type=\"time\" ups=\"80\">\n" +
                "\n" +
                "        <o type=\"frame\" title=\"Pokusy s DAGama\" id=\"$window\">\n" +
                "\n" +
                "            <o type=\"frame\" title=\"Pohled na frame okna\" showXML=\"true\" target=\"$window\" pos=\"221 -579\" size=\"640 480\" />\n" +
                "            <o type=\"frame\" title=\"Pohled na frame s resultem\" showXML=\"true\" target=\"$pokus3\" pos=\"-455 -580\" size=\"640 480\" />\n" +
                "\n" +
                "\n" +
                "            <macro type=\"TypedDagGenerator\" id=\"$pokus3\" title=\"Title...\" size=\"2500 2000\" pos=\"4 4\">\n" +
                "                <n>"+numTrees+"</n>\n" +
                "                <goal>"+goalType+"</goal>\n" +
                "                <lib>"+ Joiner.on(";\n").join(lib) +"</lib>\n" +
                "            </macro>\n" +
                "\n" +
                "        </o>\n" +
                "\n" +
                "\n" +
                "    </o>\n" +
                "</kutil>";
    }

    public static void starLib(String[] lib, String goalType, String numTrees) {
        new Kutil().start(XmlLoader.LoadMethod.STRING, wrapLibInKutil(lib,goalType,numTrees));
    }

    public static void starLib(String[] lib, String goalType, Integer numTrees) {
        new Kutil().start(XmlLoader.LoadMethod.STRING, wrapLibInKutil(lib,goalType,numTrees.toString()));
    }
}
