package de.himbiss.scrawl.project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.himbiss.scrawl.util.Constants;

@XmlType(name = "folder")
@XmlRootElement(name = "folder")
public class Folder extends Node {

	private static final long serialVersionUID = 2807640596345882562L;
	
	private List<Node> components;
	
	public Folder(String identifier, ContentType nodeType) {
		super(identifier, true, true);
		components = new ArrayList<Node>();
	}
	 
	public Folder(String identifier, ContentType nodeType, boolean isDeletable) {
		super(identifier, true, isDeletable);
		components = new ArrayList<Node>();
	}

	public Folder() {
		super(Constants.NEW_FOLDER, true, true);
		components = new ArrayList<Node>();
	}

	public void add(Node component) {
		component.setParent(this);
		components.add(component);
	}
	
	public void remove(Node component) {
		component.setParent(null);
		components.remove(component);
	}
	
	@XmlElementWrapper(name = "components")
	@XmlElement
	public List<Node> getComponents() {
		return components;
	}
	
	public void setComponents(List<Node> components) {
		this.components = components;
	}
	
	@Override
	public boolean isFolder() {
		return true;
	}

}
