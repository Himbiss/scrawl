package de.himbiss.scrawl.model.project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class Project extends Node<Project> {

	private ObjectProperty<Folder<Scene>> scenes;
	private ObjectProperty<Folder<Person>> persons;
	private ObjectProperty<Folder<Object>> objects;
	private ObjectProperty<Folder<Location>> locations;
	
	public Project(String identifier) {
		super(identifier);
		scenes = new SimpleObjectProperty<>(new Folder<>("Scenes"));
		persons = new SimpleObjectProperty<>(new Folder<>("Persons"));
		objects = new SimpleObjectProperty<>(new Folder<>("Objects"));
		locations = new SimpleObjectProperty<>(new Folder<>("Locations"));
	}
	
	public Folder<Location> getLocations() {
		return locations.getValue();
	}
	
	public Folder<Scene> getScenes() {
		return scenes.getValue();
	}
	
	public Folder<Person> getPersons() {
		return persons.getValue();
	}
	
	public Folder<Object> getObjects() {
		return objects.getValue();
	}
}
