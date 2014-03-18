package pikater.ontology.description;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import pikater.agents.PikaterAgent;

import com.thoughtworks.xstream.XStream;



/**
 * Created by stepan
 */
public class Experiment {


/*
		Reflections reflections = new Reflections("pikater");

		Set<Class<? extends PikaterAgent>> allClassesAgents = 
			     reflections.getSubTypesOf(pikater.agents.PikaterAgent.class);
		
		List<Class<? extends PikaterAgent>> listAgents =
				new ArrayList<Class<? extends PikaterAgent>>();
		listAgents.addAll(allClassesAgents);

		for (int i = 0; i < listAgents.size(); i++) {

			Class<? extends PikaterAgent> classI = listAgents.get(i);
			String classNameI = classI.toString();
			classNameI = classNameI.substring("class ".length());

			Class<?> c = null;
			try {
				c = Class.forName(classNameI);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			Method lMethod = null;
			try {
				lMethod = c.getDeclaredMethod("getAgentType", null);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			System.out.println("method = " + lMethod.toString());			
		}
*/
	
}

