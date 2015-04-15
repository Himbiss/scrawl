package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "location")
@XmlRootElement(name = "location")
public class Location extends Node<Location> {

	private static final long serialVersionUID = 5158405417755517140L;

	private String summary;
	private String inhabitants;
	
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

	public String getName() {
		return getIdentifier();
	}

	public void setName(String name) {
		setIdentifier(name);
	}

	@XmlElement
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@XmlElement
	public String getInhabitants() {
		return inhabitants;
	}

	public void setInhabitants(String inhabitants) {
		this.inhabitants = inhabitants;
	}

	public String getShortSummary() {
		return getDescription();
	}

	public void setShortSummary(String shortSummary) {
		setDescription(shortSummary);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void copyAttributes(Location location) {
		setName(location.getName());
		setShortSummary(location.getShortSummary());
		setSummary(location.getSummary());
		setInhabitants(location.getInhabitants());
	}

}
