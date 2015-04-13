package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "location")
@XmlRootElement(name = "location")
public class Location extends Node<Location> {

	private static final long serialVersionUID = 5158405417755517140L;

	private String name;
	private String summary;
	private String inhabitants;
	private String shortSummary;
	
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

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@XmlElement
	public String getShortSummary() {
		return shortSummary;
	}

	public void setShortSummary(String shortSummary) {
		this.shortSummary = shortSummary;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
