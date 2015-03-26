package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "location")
@XmlType(name = "location")
public class Location extends Node<Location> {

	private static final long serialVersionUID = 5158405417755517140L;

	public Location() {
		// for jaxb / serialization
	}
	
	public Location(String identifier) {
		super(identifier, NodeType.LOCATION);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
