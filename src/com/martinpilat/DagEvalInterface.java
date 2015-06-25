package com.martinpilat;

/**
 * Created by Martin on 25.6.2015.
 */

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;


public class DagEvalInterface {

    XmlRpcClient server;

    /**
     * Contructor of the evaluator interface.
     *
     * @param url The URL of the evaluating server e.g. http://localhost:8080
     * @throws java.net.MalformedURLException
     */
    public DagEvalInterface(String url) throws java.net.MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url));
        this.server = new XmlRpcClient();
        this.server.setConfig(config);

    }

    /**
     * Evaluates the list of machine learning workflows. Calls the Python server internally.
     *
     * @param jsonString The list of workflows encoded as a JSON string.
     * @param dataFile The dataset to use for evaluation
     * @return List of arrays of scores
     * @throws XmlRpcException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<double[]> eval(String jsonString, String dataFile) throws XmlRpcException{

        Vector params = new Vector();
        params.addElement(jsonString);
        params.addElement(dataFile);

        Object[] result = (Object[])this.server.execute("eval", params);

        ArrayList<double[]> res = new ArrayList<>();
        for (Object o : result) {
            Object[] oarr = (Object[])o;
            double[] darr = new double[oarr.length];
            for (int i = 0; i < oarr.length; i++) {
                darr[i] = (Double)oarr[i];
            }
            res.add(darr);
        }

        return res;
    }

    public static void main(String[] args) {

        try {
            DagEvalInterface evaluator = new DagEvalInterface("http://localhost:8080");

            String dagStr   =  "[";
            dagStr          += "{\"input\" : [ [], \"input\", [\"1:0\"] ], \"1\" : [ [\"1:0\"], \"SVC\", [] ]},";
            dagStr          += "{\"input\" : [ [], \"input\", [\"1:0\"] ], \"1\" : [ [\"1:0\"], \"lsdfogR\", [] ]},";
            dagStr          += "{\"input\" : [ [], \"input\", [\"1:0\"] ], \"1\" : [ [\"1:0\"], \"DT\", [] ]},";
            dagStr          += "{\"input\" : [ [], \"input\", [\"1:0\"] ], \"1\" : [ [\"1:0\"], \"gaussianNB\", [] ]}";
            dagStr          += "]";

            ArrayList<double[]> scores = evaluator.eval(dagStr, "winequality-white.csv");

            for (double[] darr : scores) {
                if (darr.length == 0)
                    System.out.println("Evaluation error");
                else
                    System.out.println(darr[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
