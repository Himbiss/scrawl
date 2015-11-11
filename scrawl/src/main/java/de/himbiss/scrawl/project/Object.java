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

	private String type;
	private String abilities;
	private String location;
	
	public Object() {
		super(Constants.NEW_OBJECT, ContentType.OBJECT);
		// for jaxb / serialization
	}
	
	public Object(String identifier) {
		super(identifier, ContentType.OBJECT);
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	public String getName() {
		return getIdentifier();
	}

	public void setName(String name) {
		setIdentifier(name);
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

	public String getSummary() {
		return getDescription();
	}

	public void setSummary(String summary) {
		setDescription(summary);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void copyAttributes(Object object) {
		setName(object.getName());
		setLocation(object.getLocation());
		setType(object.getType());
		setAbilities(object.getAbilities());
		setSummary(object.getSummary());
	}
}
