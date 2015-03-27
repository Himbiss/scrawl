package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "location")
@XmlRootElement(name = "location")
public class Location extends Node<Location> {

	private static final long serialVersionUID = 5158405417755517140L;

	public Location() {
		super(Constants.NEW_LOCATION, NodeType.LOCATION);
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
