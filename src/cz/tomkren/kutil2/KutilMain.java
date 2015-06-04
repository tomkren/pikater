package cz.tomkren.kutil2;

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
}
