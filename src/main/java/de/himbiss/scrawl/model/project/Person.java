package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "person")
@XmlRootElement
public class Person extends Node<Person> {

	private static final long serialVersionUID = 3432993778254731881L;

	public Person() {
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
