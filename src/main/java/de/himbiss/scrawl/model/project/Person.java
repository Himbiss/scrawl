package de.himbiss.scrawl.model.project;


public class Person extends Node<Person> {

	private static final long serialVersionUID = 3432993778254731881L;

	public Person(String identifier) {
		super(identifier, NodeType.PERSON);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
