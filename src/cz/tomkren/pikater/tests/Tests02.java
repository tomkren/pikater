package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Checker;

/** Created by tom on 12. 6. 2015. */

public class Tests02 {

    public static void main(String[] args) {
        Checker check = new Checker();

        check.eqStrSilent(
                new Net_PCA_RBF_gen().createDescription().exportXML(),
                new Net_PCA_RBF_onto().createDescription().exportXML()
        );

        check.results();
    }

}
