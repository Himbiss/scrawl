package de.himbiss.scrawl.model.project;


public class Project extends Folder<Node<?>> {

	private static final long serialVersionUID = 5901512498873443281L;
	
	private Folder<Scene> scenes;
	private Folder<Person> persons;
	private Folder<Object> objects;
	private Folder<Location> locations;
	
	public Project(String identifier) {
		super(identifier);
		scenes = new Folder<>("Scenes");
		persons = new Folder<>("Persons");
		objects = new Folder<>("Objects");
		locations = new Folder<>("Locations");
	}
	
	public Folder<Location> getLocations() {
		return locations;
	}
	
	public Folder<Scene> getScenes() {
		return scenes;
	}
	
	public Folder<Person> getPersons() {
		return persons;
	}
	
	public Folder<Object> getObjects() {
		return objects;
	}

	@Override
	public boolean isFolder() {
		return true;
	}
}
