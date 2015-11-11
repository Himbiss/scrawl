package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.himbiss.scrawl.util.Constants;

@XmlRootElement(name = "project")
public class Project extends Folder<Node<?>> {

	private static final long serialVersionUID = 5901512498873443281L;
	
	private Folder<Scene> scenes;
	private Folder<Person> persons;
	private Folder<Object> objects;
	private Folder<Location> locations;
	
	public Project() {
		super(Constants.NEW_PROJECT, null);
		// for jaxb / serialization
	}
	
	public Project(String identifier) {
		super(identifier, null);
		scenes = new Folder<>("Scenes", ContentType.SCENE, false);
		persons = new Folder<>("Persons", ContentType.PERSON, false);
		objects = new Folder<>("Objects", ContentType.OBJECT, false);
		locations = new Folder<>("Locations", ContentType.LOCATION, false);
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

	public void setScenes(Folder<Scene> scenes) {
		this.scenes = scenes;
	}

	public void setPersons(Folder<Person> persons) {
		this.persons = persons;
	}

	public void setObjects(Folder<Object> objects) {
		this.objects = objects;
	}

	public void setLocations(Folder<Location> locations) {
		this.locations = locations;
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
