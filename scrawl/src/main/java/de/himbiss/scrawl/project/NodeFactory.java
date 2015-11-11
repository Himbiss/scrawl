package de.himbiss.scrawl.project;

import java.util.Set;

import de.himbiss.scrawl.util.Constants;

public class NodeFactory {
	
	public static Project createProject(String identifier) {
		return new Project(identifier);
	}
	
	public static Folder<?> createFolder(String identifier, ContentType nodeType) {
		return new Folder<>(identifier, nodeType);
	}
	
	public static Scene  createScene(String identifier) {
		Scene scene = new Scene(identifier);
		NodeRegistry.registerNode(scene);
		return scene;
	}

	public static Person createPerson(String identifier) {
		Person person = new Person(identifier);
		person.setConflict("");
		person.setEpiphany("");
		person.setGoal("");
		person.setMotivation("");
		person.setOneParagraphStoryline("");
		person.setOneSentenceSummary("");
		NodeRegistry.registerNode(person);
		return person;
	}
	
	public static Location createLocation(String identifier) {
		Location location = new Location(identifier);
		NodeRegistry.registerNode(location);
		return location;
	}
	
	public static Object createObject(String identifier) {
		Object object = new Object(identifier);
		NodeRegistry.registerNode(object);
		return object;
	}
	
	
	
	public static <T> Node<?> createUniqueNode(ContentType nodeType) {
		if(nodeType != null) {
			switch(nodeType) {
			case SCENE:
				return createUniqueScene();
			case LOCATION:
				return createUniqueLocation();
			case PERSON:
				return createUniquePerson();
			case OBJECT:
				return createUniqueObject();
			}
		}
		return null;
	}
	
	public static String generateUniqueIdentifier(String prefix, Set<String> takenIdentifiers) {
		int index = 0;
		while(takenIdentifiers.contains(prefix+index)) {
			index++;
		}
		return prefix+index;
	}

	public static Location createUniqueLocation() {
		return createLocation(generateUniqueIdentifier(Constants.LOCATION_NODE, NodeRegistry.getTakenLocations()));
	}

	public static Person createUniquePerson() {
		return createPerson(generateUniqueIdentifier(Constants.PERSON_NODE, NodeRegistry.getTakenPersons()));
	}

	public static Object createUniqueObject() {
		return createObject(generateUniqueIdentifier(Constants.OBJECT_NODE, NodeRegistry.getTakenObjects()));
	}

	public static Scene createUniqueScene() {
		return createScene(generateUniqueIdentifier(Constants.SCENE_NODE, NodeRegistry.getTakenScenes()));
	}
	

	

	public static String generateUniqueIdentifier(String pre,
			ContentType nodeType) {
		switch(nodeType) {
		case SCENE:
			return generateUniqueIdentifier(pre, NodeRegistry.getTakenScenes());
		case LOCATION:
			return generateUniqueIdentifier(pre, NodeRegistry.getTakenLocations());
		case PERSON:
			return generateUniqueIdentifier(pre, NodeRegistry.getTakenPersons());
		case OBJECT:
			return generateUniqueIdentifier(pre, NodeRegistry.getTakenObjects());
		default:
			return pre;
		}
	}
}
