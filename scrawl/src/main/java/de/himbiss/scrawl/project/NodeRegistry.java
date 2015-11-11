package de.himbiss.scrawl.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class has all instances of nodes mapped to their identifiers. <br>
 * If a new node is created or destroyed via the {@link NodeFactory}, it is registered here.
 */
public class NodeRegistry {

	private static final Map<String, Scene> sceneMap = new HashMap<String, Scene>();
	private static final Map<String, Person> personMap = new HashMap<String, Person>();
	private static final Map<String, Location> locationMap = new HashMap<String, Location>();
	private static final Map<String, Object> objectMap = new HashMap<String, Object>();
	
	public static boolean sceneExists(String identifier) {
		return sceneMap.containsKey(identifier);
	}
	
	public static boolean personExists(String identifier) {
		return personMap.containsKey(identifier);
	}
	
	public static boolean locationExists(String identifier) {
		return locationMap.containsKey(identifier);
	}
	
	public static boolean objectExists(String identifier) {
		return objectMap.containsKey(identifier);
	}

	public static Set<String> getTakenScenes() {
		return sceneMap.keySet();
	}
	
	public static Set<String> getTakenPersons() {
		return personMap.keySet();
	}
	
	public static Set<String> getTakenLocations() {
		return locationMap.keySet();
	}
	
	public static Set<String> getTakenObjects() {
		return objectMap.keySet();
	}
	
	public static void registerNode(Node<?> n) {
		if(n != null) {
		switch(n.getNodeType()) {
			case SCENE:
				sceneMap.put(n.getIdentifier(),(Scene) n);
				break;
			case LOCATION:
				locationMap.put(n.getIdentifier(),(Location) n);
				break;
			case PERSON:
				personMap.put(n.getIdentifier(),(Person) n);
				break;
			case OBJECT:
				objectMap.put(n.getIdentifier(),(Object) n);
				break;
			default:
				break;
			}
		}
	}
	
	public static void freeNode(String identifier, ContentType nodeType) {
		switch(nodeType) {
		case SCENE:
			sceneMap.remove(identifier);
			break;
		case LOCATION:
			locationMap.remove(identifier);
			break;
		case PERSON:
			personMap.remove(identifier);
			break;
		case OBJECT:
			objectMap.remove(identifier);
			break;
		default:
			break;
		}
	}
}
