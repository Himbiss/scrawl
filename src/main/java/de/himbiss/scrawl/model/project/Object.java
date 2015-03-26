package de.himbiss.scrawl.model.project;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "object")
@XmlRootElement
public class Object extends Node<Object> {

	private static final long serialVersionUID = -1248837597572107378L;

	public Object() {
		// for jaxb / serialization
	}
	
	public Object(String identifier) {
		super(identifier, NodeType.OBJECT);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}
