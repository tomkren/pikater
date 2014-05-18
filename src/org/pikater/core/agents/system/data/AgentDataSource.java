package org.pikater.core.agents.system.data;

import jade.util.leap.Serializable;
import org.apache.commons.io.FileUtils;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.shared.logging.Severity;

import java.io.*;
import java.util.HashSet;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 11:38
 * AgentData source maintains a list of files in pathToLocals directory and provides a path to datasource on request.
 * If the datasource is not in the local the agent tries to get it from other containers(TODO).
 */
public class AgentDataSource extends PikaterAgent {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2175414070329509621L;

	private static final String pathToLocalSources =
            "core" + System.getProperty("file.separator") +
                    "data" + System.getProperty("file.separator") +
                    "dataSources" + System.getProperty("file.separator");
    private HashSet<String> ownedDataSources=new HashSet<>();
    public static String SERVICE_TYPE ="AgentDataSource";

    //TODO: move to more appropriate location
    public static  void SerializeFile(Serializable toSerialize,String fileName) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(pathToLocalSources+fileName);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(toSerialize);
        out.close();
        fileOut.close();
    }

    protected void setup() {
        try {
            initDefault();
            registerWithDF(SERVICE_TYPE);
            cleanup();
        } catch (Exception e) {
                    logError("Failed to complete setup", Severity.Critical);
        }
    }

    public String getPathToDataSource(String dataSourceName)
    {
      if (ownedDataSources.contains(dataSourceName))
      {
              return pathToLocalSources+dataSourceName;
      }
        else
      {
          throw new IllegalArgumentException();
          //TODO: obtain from other containers
      }
    }

    public void addDataSourceToOwned(String dataSourceName)
    {
          ownedDataSources.add(dataSourceName);
    }

    private void cleanup() throws IOException {
        log("Cleaning local datasources directory from the datasources of the previous run");
        File localDataSourcesDirectory=new File(pathToLocalSources);
        FileUtils.cleanDirectory(localDataSourcesDirectory);
    }
}

