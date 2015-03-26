package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
public class Project extends Folder<Node<?>> {

	private static final long serialVersionUID = 5901512498873443281L;
	
	private Folder<Scene> scenes;
	private Folder<Person> persons;
	private Folder<Object> objects;
	private Folder<Location> locations;
	
	public Project() {
		// for jaxb / serialization
	}
	
	public Project(String identifier) {
		super(identifier, null);
		scenes = new Folder<>("Scenes", NodeType.SCENE, false);
		persons = new Folder<>("Persons", NodeType.PERSON, false);
		objects = new Folder<>("Objects", NodeType.OBJECT, false);
		locations = new Folder<>("Locations", NodeType.LOCATION, false);
	}
	
	@XmlElement
	public Folder<Location> getLocations() {
		return locations;
	}
	
	@XmlElement
	public Folder<Scene> getScenes() {
		return scenes;
	}
	
	@XmlElement
	public Folder<Person> getPersons() {
		return persons;
	}
	
	@XmlElement
	public Folder<Object> getObjects() {
		return objects;
	}
	
	@XmlAttribute(name = "title")
	@Override
	public String getIdentifier() {
		return super.getIdentifier();
	}

	@Override
	public boolean isFolder() {
		return true;
	}
	
	@Override
	public boolean isDeletable() {
		return false;
	}
}
