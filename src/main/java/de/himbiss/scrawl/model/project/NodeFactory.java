package de.himbiss.scrawl.model.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.himbiss.scrawl.util.Constants;

public class NodeFactory {

	private static final Map<String, Scene> sceneMap = new HashMap<String, Scene>();
	private static final Map<String, Person> personMap = new HashMap<String, Person>();
	private static final Map<String, Location> locationMap = new HashMap<String, Location>();
	private static final Map<String, Object> objectMap = new HashMap<String, Object>();
	
	public static Project createProject(String identifier) {
		return new Project(identifier);
	}
	
	public static Folder<?> createFolder(String identifier) {
		return new Folder<>(identifier);
	}
	
	public static Scene  createScene(String identifier) {
		Scene scene = new Scene(identifier);
		sceneMap.put(identifier, scene);
		return scene;
	}

	public static Person createPerson(String identifier) {
		Person person = new Person(identifier);
		personMap.put(identifier, person);
		return person;
	}
	
	public static Location createLocation(String identifier) {
		Location location = new Location(identifier);
		locationMap.put(identifier, location);
		return location;
	}
	
	public static Object createObject(String identifier) {
		Object object = new Object(identifier);
		objectMap.put(identifier, object);
		return object;
	}
	
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
	
	public static <T> Node<?> createUniqueNode(NodeType nodeType) {
		switch(nodeType) {
		case SCENE:
			return createUniqueScene();
		case FOLDER:
			return createFolder("New ".concat(Constants.FOLDER_NODE));
		case LOCATION:
			return createUniqueLocation();
		case PERSON:
			return createUniquePerson();
		case OBJECT:
			return createUniqueObject();
		case PROJECT:
			return createProject("New ".concat(Constants.PROJECT_NODE));
		default:
			return null;
		}
	}
	
	public static String generateUniqueIdentifier(String pre, Set<String> keySet) {
		String identifier = pre;
		int index = 0;
		pre.replaceAll("/[0-9]", "");
		while(keySet.contains(identifier)) {
			identifier += String.valueOf(index);
			index++;
		}
		return identifier;
	}

	public static Location createUniqueLocation() {
		return createLocation(generateUniqueIdentifier(Constants.LOCATION_NODE, locationMap.keySet()));
	}

	public static Person createUniquePerson() {
		return createPerson(generateUniqueIdentifier(Constants.PERSON_NODE, personMap.keySet()));
	}

	public static Object createUniqueObject() {
		return createObject(generateUniqueIdentifier(Constants.OBJECT_NODE, objectMap.keySet()));
	}

	public static Scene createUniqueScene() {
		return createScene(generateUniqueIdentifier(Constants.SCENE_NODE, sceneMap.keySet()));
	}
	

	public static void registerNode(Node<?> n) {
		switch(n.getNodeType()) {
		case SCENE:
			sceneMap.put(n.getIdentifier(),(Scene) n);
			break;
		case LOCATION:
			locationMap.remove(n.getIdentifier(),(Location) n);
			break;
		case PERSON:
			personMap.remove(n.getIdentifier(),(Person) n);
			break;
		case OBJECT:
			objectMap.remove(n.getIdentifier(),(Object) n);
			break;
		default:
			break;
		}
	}
	
	public static void freeNode(String identifier, NodeType nodeType) {
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

	public static String generateUniqueIdentifier(String pre,
			NodeType nodeType) {
		switch(nodeType) {
		case SCENE:
			return generateUniqueIdentifier(pre, sceneMap.keySet());
		case LOCATION:
			return generateUniqueIdentifier(pre, locationMap.keySet());
		case PERSON:
			return generateUniqueIdentifier(pre, personMap.keySet());
		case OBJECT:
			return generateUniqueIdentifier(pre, objectMap.keySet());
		default:
			return pre;
		}
	}
}
