package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "person")
@XmlRootElement(name = "person")
public class Person extends Node<Person> {

	private static final long serialVersionUID = 3432993778254731881L;

	public Person() {
		super(Constants.NEW_PERSON, NodeType.PERSON);
		// for jaxb / serialization
	}
	
	public Person(String identifier) {
		super(identifier, NodeType.PERSON);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
