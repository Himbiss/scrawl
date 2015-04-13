package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "object")
@XmlRootElement(name = "object")
public class Object extends Node<Object> {

	private static final long serialVersionUID = -1248837597572107378L;

	private String name;
	private String type;
	private String abilities;
	private String location;
	private String summary;
	
	public Object() {
		super(Constants.NEW_OBJECT, NodeType.OBJECT);
		// for jaxb / serialization
	}
	
	public Object(String identifier) {
		super(identifier, NodeType.OBJECT);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getAbilities() {
		return abilities;
	}

	public void setAbilities(String abilities) {
		this.abilities = abilities;
	}

	@XmlElement
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@XmlElement
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
