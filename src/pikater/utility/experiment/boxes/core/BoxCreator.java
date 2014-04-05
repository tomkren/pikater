package pikater.utility.experiment.boxes.core;

/**
 * Class to generate different boxes using its static methods.
 */
public class BoxCreator {
	public static LeafBox createRegressionBox(String displayName,String picture,String description,String agentClassName,String ontologyClassName){
		return new LinearRegressionBox(displayName, picture, description, agentClassName, ontologyClassName);
	}
	
	public static void log(String text){
		System.out.println(text);
	}
	
	public static void main(String[] args){
		LeafBox lrgb=BoxCreator.createRegressionBox("dummy", "no_pic", "A dummy linear regression box", "no-agent-associated", "no-ontology");
		log(lrgb.toString());
	}
	
}
