package de.himbiss.scrawl.model.project;


public class Person extends Node<Person> {

	public Person(String identifier) {
		super(identifier, NodeType.PERSON);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
