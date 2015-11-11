package de.himbiss.scrawl.project;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.project.content.IContent;
import de.himbiss.scrawl.util.Constants;

@XmlType(name = "location")
@XmlRootElement(name = "location")
public class Location implements IContent {

	private static final long serialVersionUID = 5158405417755517140L;

	private String summary;
	private String inhabitants;
	
	public Location() {
		// for jaxb / serialization
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentifier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentType getContentType() {
		return ContentType.LOCATION;
	}

}
